package info.unterrainer.commons.jreutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class FieldParser {

	private Object instanceToSearchIn;
	private Class<? extends Annotation> annotation;

	private String[] pathArray;
	private int pos = -1;
	private String current;
	private String currentPlain;
	private Integer currentIndex;
	private boolean isLast;

	public FieldParser(final String path, final Object instanceToSearchIn,
			final Class<? extends Annotation> annotation) {
		this.instanceToSearchIn = instanceToSearchIn;
		this.annotation = annotation;
		this.pathArray = path.split("\\.");
		advance();
	}

	private void advance() {
		pos++;
		if (pos > pathArray.length - 2)
			isLast = true;
		if (pos > pathArray.length - 1)
			current = null;
		current = pathArray[pos];
		currentPlain = withoutIndex(current);
		currentIndex = index(current);
	}

	public <T> T parse() throws IllegalArgumentException, IllegalAccessException {
		return parse(instanceToSearchIn, instanceToSearchIn.getClass());
	}

	@SuppressWarnings("unchecked")
	private <T> T parse(final Object instance, final Class<?> clazz)
			throws IllegalArgumentException, IllegalAccessException {
		if (pathArray.length == 0)
			return null;

		for (Field field : clazz.getDeclaredFields())
			if (field.isAnnotationPresent(annotation) && field.getName().equals(currentPlain)) {
				field.setAccessible(true);
				Object fieldInstance = resolveInstanceOf(instance, field);
				if (isLast)
					return (T) fieldInstance;
				advance();
				return parse(fieldInstance, fieldInstance.getClass());
			}

		Class<?> c = clazz.getSuperclass();
		while (c != null) {
			T inst = parse(instance, c);
			if (inst != null)
				return inst;
			c = c.getSuperclass();
		}
		return null;
	}

	private Object resolveInstanceOf(final Object instance, final Field field)
			throws IllegalArgumentException, IllegalAccessException {
		Object fieldInstance = field.get(instance);
		Class<?> currentType = field.getType();

		if (List.class.isAssignableFrom(currentType)) {
			if (currentIndex == null)
				return (fieldInstance);
			int size = ((List<?>) fieldInstance).size();
			currentIndex = cap(currentIndex, size);
			return ((List<?>) fieldInstance).get(currentIndex);
		}
		if (currentType.isArray()) {
			if (currentIndex == null)
				return (fieldInstance);
			int size = ((Object[]) fieldInstance).length;
			currentIndex = cap(currentIndex, size);
			return ((Object[]) fieldInstance)[currentIndex];
		}
		return fieldInstance;
	}

	private int cap(final int index, final int size) {
		if (index >= 0)
			return index % size;
		return index % size + size;
	}

	private String withoutIndex(final String current) {
		int pos = current.indexOf(":");
		if (pos == -1)
			return current;
		return current.substring(0, pos);
	}

	private Integer index(final String current) {
		try {
			int pos = current.indexOf(":");
			if (pos == -1)
				return null;
			return Integer.parseInt(current.substring(pos + 1));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
