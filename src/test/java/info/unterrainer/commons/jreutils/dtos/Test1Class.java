package info.unterrainer.commons.jreutils.dtos;

import java.util.List;

import lombok.Data;

@Data
public class Test1Class {

	private String name;
	@ContainsMyType
	private List<MyType> myTypeList = List.of(new MyType(), new MyType(), new MyType(), new MyType());
	private String vendor;
	private Long type;
	@ContainsMyType
	private MyType myType = new MyType();
	private String address;
	@ContainsMyType
	private MyType[] myTypeArray = List.of(new MyType(), new MyType(), new MyType(), new MyType(), new MyType())
			.toArray(new MyType[0]);
	private String className;
	@ContainsMyType
	private List<Test1OtherClass> usedClassList = List.of(new Test1OtherClass(), new Test1OtherClass(),
			new Test1OtherClass(), new Test1OtherClass());
	@ContainsMyType
	private Test1OtherClass[] usedClassArray = List
			.of(new Test1OtherClass(), new Test1OtherClass(), new Test1OtherClass()).toArray(new Test1OtherClass[0]);
	@ContainsMyType
	private Test1OtherClass usedClass = new Test1OtherClass();
}
