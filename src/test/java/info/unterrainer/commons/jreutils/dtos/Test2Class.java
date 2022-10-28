package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;

@Data
public class Test2Class {

	@ContainsMyType
	private MyType mySuperType = new MyType();
}
