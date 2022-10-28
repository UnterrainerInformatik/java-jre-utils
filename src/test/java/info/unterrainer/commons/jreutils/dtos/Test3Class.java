package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;

@Data
public class Test3Class {

	@ContainsMyType
	private MyType mySuperType = new MyType();
}
