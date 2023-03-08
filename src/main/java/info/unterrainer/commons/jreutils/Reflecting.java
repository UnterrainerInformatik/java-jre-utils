package info.unterrainer.commons.jreutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Reflecting {

	/**
	 * Scans object tree connected by annotated fields for a given class using
	 * reflection and returns found paths.<br>
	 * <br>
	 * Resolves generic Lists and Arrays as well (example for indexing:
	 * 'myField.myArray:0.myVar')
	 *
	 * @param classToScan         the class to start scanning in
	 * @param annotationToScanFor the annotation the fields we should scan are
	 *                            annotated with
	 * @return a list of paths where the given fields have been found
	 */
	public List<String> getPathsOf(final Class<?> classToScan, final Class<? extends Annotation> annotationToScanFor) {
		return getPathsOf(classToScan, null, annotationToScanFor, "");
	}

	/**
	 * Scans object tree connected by annotated fields for a given class using
	 * reflection and returns found paths.<br>
	 * <br>
	 * Resolves generic Lists and Arrays as well (example for indexing:
	 * 'myField.myArray:0.myVar')
	 *
	 * @param classToScan         the class to start scanning in
	 * @param fieldTypeToFind     the type to search for or null, if you have no
	 *                            type-restriction for your search
	 * @param annotationToScanFor the annotation the fields we should scan are
	 *                            annotated with
	 * @return a list of paths where the given fields have been found
	 */
	public List<String> getPathsOf(final Class<?> classToScan, final Class<?> fieldTypeToFind,
			final Class<? extends Annotation> annotationToScanFor) {
		return getPathsOf(classToScan, fieldTypeToFind, annotationToScanFor, "");
	}

	private List<String> getPathsOf(final Class<?> classToScan, final Class<?> typeToFind,
			final Class<? extends Annotation> annotation, final String currentPath) {
		List<String> paths = new ArrayList<>();
		getInheritedFields(classToScan, typeToFind, annotation, currentPath, paths);
		getFields(classToScan, typeToFind, annotation, currentPath, paths);
		return paths;
	}

	private void getFields(final Class<?> classToScan, final Class<?> typeToFind,
			final Class<? extends Annotation> annotation, final String currentPath, final List<String> paths) {
		for (Field field : classToScan.getDeclaredFields()) {

			if (field.isAnnotationPresent(annotation)) {
				Class<?> currentType = resolveTypeOf(field);
				if (typeToFind == null || typeToFind.isAssignableFrom(currentType)) {
					paths.add(currentPath + field.getName());
				}
				paths.addAll(getPathsOf(currentType, typeToFind, annotation, currentPath + field.getName() + "."));
			}
		}
	}

	private Class<?> resolveTypeOf(Field field) {
		Class<?> currentType = field.getType();
		if (List.class.isAssignableFrom(currentType)) {
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			return (Class<?>) pt.getActualTypeArguments()[0];
		}
		if (currentType.isArray()) {
			currentType = currentType.getComponentType();
		}
		return currentType;
	}

	private void getInheritedFields(final Class<?> classToScan, final Class<?> typeToFind,
			final Class<? extends Annotation> annotation, final String currentPath, final List<String> paths) {
		Class<?> c = classToScan.getSuperclass();
		while (c != null) {
			paths.addAll(getPathsOf(c, typeToFind, annotation, currentPath));
			c = c.getSuperclass();
		}
	}

	/**
	 * Gets the instance of a specific field given by path.<br>
	 * <br>
	 * Resolves generic Lists and Arrays as well (example for indexing:
	 * 'myField.myList:2.myVar')
	 *
	 * @param <T>                 the type of the field to find
	 * @param path                The path to follow in order to get to the field
	 * @param instanceToSearchIn  the instance of the object to search for the field
	 *                            in
	 * @param annotationToScanFor the annotation that leads to the field to find
	 * @return the field
	 * @throws IllegalArgumentException if the specified object is not an instance
	 *                                  of the class or interface declaring the
	 *                                  underlying field (or a subclass or
	 *                                  implementor thereof).
	 * @throws IllegalAccessException   if this Field object is enforcing Java
	 *                                  language access control and the underlying
	 *                                  field is inaccessible.
	 */
	public <T> T getFieldByPath(final String path, final Object instanceToSearchIn,
			final Class<? extends Annotation> annotationToScanFor)
			throws IllegalArgumentException, IllegalAccessException {
		FieldParser p = new FieldParser(path, instanceToSearchIn, annotationToScanFor);
		return p.parse();
	}
}
