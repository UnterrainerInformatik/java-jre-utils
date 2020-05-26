package info.unterrainer.commons.jreutils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClassParam {

	private final Class<?> clazz;
	private final Object instance;
}
