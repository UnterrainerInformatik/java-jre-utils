package info.unterrainer.commons.jreutils;

import java.util.List;

import lombok.Builder;
import lombok.Singular;

@Builder()
public class ClassParams {

	@Singular
	private List<ClassParam> params;

	public ClassParam[] toArray() {
		return params.toArray(new ClassParam[0]);
	}
}
