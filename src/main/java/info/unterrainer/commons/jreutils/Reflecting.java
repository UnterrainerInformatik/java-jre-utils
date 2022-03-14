package info.unterrainer.commons.jreutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Reflecting {

	/**
	 * Scans object tree connected by annotated fields for a given class using
	 * reflection and returns found paths.
	 *
	 * @param classToScan         the class to start scanning in
	 * @param fieldTypeToFind     the type to search for
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

		for (Field field : classToScan.getDeclaredFields()) {

			Class<?> currentType = field.getType();
			if (field.isAnnotationPresent(annotation)) {
				if (typeToFind.isAssignableFrom(currentType)) {
					paths.add(currentPath + field.getName());
					continue;
				}
				paths.addAll(getPathsOf(currentType, typeToFind, annotation, currentPath + field.getName() + "."));
			}
		}
		return paths;
	}

	/**
	 * Gets the instance of a specific field given by path.
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
