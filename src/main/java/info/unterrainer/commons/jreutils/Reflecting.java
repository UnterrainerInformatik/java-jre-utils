package info.unterrainer.commons.jreutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Reflecting {

	/**
	 * Searches for annotated fields using reflection and returns their paths.
	 *
	 * @param classToScan
	 * @param fieldTypeToFind
	 * @param annotationToScanFor
	 * @return
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

	public <T> T getFieldByPath(final String path, final Object instanceToSearchIn,
			final Class<? extends Annotation> annotationToScanFor)
			throws IllegalArgumentException, IllegalAccessException {
		FieldParser p = new FieldParser(path, instanceToSearchIn, annotationToScanFor);
		return p.parse();
	}
}
