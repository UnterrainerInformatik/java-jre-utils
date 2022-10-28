package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Test3SubSubClass extends Test3SubClass {

	@ContainsMyType
	private MyType mySubType = new MyType();
	@ContainsMyType
	private MyType mySubSubType = new MyType();
}
