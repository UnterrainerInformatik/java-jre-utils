package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;

@Data
public class Test1OtherClass {

	private String name;
	@ContainsMyType
	private MyType myType = new MyType();
}
