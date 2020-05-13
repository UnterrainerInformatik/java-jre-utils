package info.unterrainer.commons.jreutils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassParam {

	private final Class<?> clazz;
	private final Object instance;
}
