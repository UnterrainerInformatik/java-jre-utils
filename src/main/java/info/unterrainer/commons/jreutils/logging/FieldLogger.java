package info.unterrainer.commons.jreutils.logging;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import info.unterrainer.commons.jreutils.Reflecting;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FieldLogger {

	public static String log(final Object instance, final Class<?> clazz) {
		List<String> paths = Reflecting.getPathsOf(clazz, LogField.class);
		StringBuilder sb = new StringBuilder();
		for (String path : paths) {
			try {
				Object o = Reflecting.getFieldByPath(path, instance, LogField.class);
				sb.append(path);
				sb.append(": ");
				if (o == null)
					sb.append("null");
				else if (o instanceof List) {
					String s = ((List<?>) o).stream().map(Object::toString).collect(Collectors.joining(", "));
					sb.append("[");
					sb.append(s);
					sb.append("]");
				} else if (o.getClass().isArray()) {
					String s = Arrays.stream((Object[]) o).map(Object::toString).collect(Collectors.joining(", "));
					sb.append("[");
					sb.append(s);
					sb.append("]");
				} else
					sb.append(o.toString());
				sb.append(System.lineSeparator());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// NOOP
			}
		}
		return sb.toString();
	}
}
