package info.unterrainer.commons.jreutils.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Test2SubClass extends Test2Class {

	@ContainsMyType
	private MyType mySubType = new MyType();
}
