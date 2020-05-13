package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;

@Data
public class TestSubClass {

	private String name;
	@ContainsMyType
	private MyType myType = new MyType();
}
