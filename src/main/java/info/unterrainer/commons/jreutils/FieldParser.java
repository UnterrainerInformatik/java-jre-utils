package info.unterrainer.commons.jreutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class FieldParser {

	private Object instanceToSearchIn;
	private Class<? extends Annotation> annotation;

	private String[] pathArray;
	private int pos = -1;
	private String current;
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
	}

	public <T> T parse() throws IllegalArgumentException, IllegalAccessException {
		return parse(instanceToSearchIn);
	}

	@SuppressWarnings("unchecked")
	private <T> T parse(final Object instance) throws IllegalArgumentException, IllegalAccessException {
		if (pathArray.length == 0)
			return null;

		for (Field field : instance.getClass().getDeclaredFields())
			if (field.isAnnotationPresent(annotation) && field.getName().equals(current)) {
				field.setAccessible(true);
				Object fieldInstance = field.get(instance);
				if (isLast)
					return (T) fieldInstance;
				advance();
				return parse(fieldInstance);
			}
		return null;
	}
}
