package info.unterrainer.commons.jreutils;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder()
public class ClassParams {

	@Singular
	private Map<String, Object> params;
}
