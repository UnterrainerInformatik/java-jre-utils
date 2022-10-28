package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Test3SubClass extends Test3Class {

	@ContainsMyType
	private MyType mySubType = new MyType();
}
