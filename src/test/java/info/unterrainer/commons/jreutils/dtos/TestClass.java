package info.unterrainer.commons.jreutils.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TestClass {

	private String name;
	@ContainsMyType
	private List<MyType> myTypeList = new ArrayList<>();
	private String vendor;
	private Long type;
	@ContainsMyType
	private MyType myType = new MyType();
	private String address;
	@ContainsMyType
	private MyType[] myTypeArray = new MyType[9];
	private String className;
	@ContainsMyType
	private TestSubClass subClass = new TestSubClass();
}
