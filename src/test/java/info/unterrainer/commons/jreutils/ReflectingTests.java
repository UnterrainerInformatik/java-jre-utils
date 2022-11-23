package info.unterrainer.commons.jreutils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import info.unterrainer.commons.jreutils.dtos.ContainsMyType;
import info.unterrainer.commons.jreutils.dtos.MyType;
import info.unterrainer.commons.jreutils.dtos.Test1Class;
import info.unterrainer.commons.jreutils.dtos.Test2SubClass;
import info.unterrainer.commons.jreutils.dtos.Test3SubSubClass;

public class ReflectingTests {

	@Test
	public void TestReadingFields() {
		List<String> results = Reflecting.getPathsOf(Test1Class.class, MyType.class, ContainsMyType.class);
		assertThat(results).containsAll(List.of("myType", "usedClass.myType"));
	}
	
	@Test
	public void TestReadingFieldsContainsFieldsInArrays() {
		List<String> results = Reflecting.getPathsOf(Test1Class.class, MyType.class, ContainsMyType.class);
		assertThat(results).containsAll(List.of("myTypeArray", "usedClassArray.myType"));
	}
	
	@Test
	public void TestReadingFieldsContainsFieldsInGenericLists() {
		List<String> results = Reflecting.getPathsOf(Test1Class.class, MyType.class, ContainsMyType.class);
		assertThat(results).containsAll(List.of("myTypeList", "usedClassList.myType"));
	}

	@Test
	public void TestWritingOfField() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getUsedClass().getMyType().getName()).isNull();
	}

	@Test
	public void TestWritingOfFieldInArray() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("myTypeArray:0", tc, ContainsMyType.class);
		mt.setName("blubb");
		mt = Reflecting.getFieldByPath("myTypeArray:3", tc, ContainsMyType.class);
		mt.setName("gluppy");
		assertThat(tc.getMyTypeArray()[0].getName()).isEqualTo("blubb");
		assertThat(tc.getMyTypeArray()[1].getName()).isNull();
		assertThat(tc.getMyTypeArray()[3].getName()).isEqualTo("gluppy");
	}
	
	@Test
	public void TestWritingOfFieldInList() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("myTypeList:0", tc, ContainsMyType.class);
		mt.setName("blubb");
		mt = Reflecting.getFieldByPath("myTypeList:3", tc, ContainsMyType.class);
		mt.setName("gluppy");
		assertThat(tc.getMyTypeList().get(0).getName()).isEqualTo("blubb");
		assertThat(tc.getMyTypeList().get(1).getName()).isNull();
		assertThat(tc.getMyTypeList().get(3).getName()).isEqualTo("gluppy");
	}
	
	@Test
	public void TestWritingOfFieldInUsedClassInArray() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("usedClassArray:0.myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		mt = Reflecting.getFieldByPath("usedClassArray:2.myType", tc, ContainsMyType.class);
		mt.setName("gluppy");
		assertThat(tc.getUsedClassArray()[0].getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getUsedClassArray()[1].getMyType().getName()).isNull();
		assertThat(tc.getUsedClassArray()[2].getMyType().getName()).isEqualTo("gluppy");
	}
	
	@Test
	public void TestWritingOfFieldInUsedClassInList() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("usedClassList:0.myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		mt = Reflecting.getFieldByPath("usedClassList:2.myType", tc, ContainsMyType.class);
		mt.setName("gluppy");
		assertThat(tc.getUsedClassList().get(0).getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getUsedClassList().get(1).getMyType().getName()).isNull();
		assertThat(tc.getUsedClassList().get(2).getMyType().getName()).isEqualTo("gluppy");
	}
	
	@Test
	public void TestWritingOfFieldInUsedClass() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("usedClass.myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getUsedClass().getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getMyType().getName()).isNull();
	}

	@Test
	public void TestReadingFieldsOfSuperClass() {
		List<String> results = Reflecting.getPathsOf(Test2SubClass.class, MyType.class, ContainsMyType.class);
		assertThat(results).containsAll(List.of("mySuperType"));
	}

	@Test
	public void TestReadingFieldsOfSubClass() {
		List<String> results = Reflecting.getPathsOf(Test2SubClass.class, MyType.class, ContainsMyType.class);
		assertThat(results).containsAll(List.of("mySuperType", "mySubType"));
	}

	@Test
	public void TestWritingOfFieldsOfSuperClass() throws IllegalArgumentException, IllegalAccessException {
		Test2SubClass tc = new Test2SubClass();
		MyType mt = Reflecting.getFieldByPath("mySuperType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMySuperType().getName()).isEqualTo("blubb");
	}

	@Test
	public void TestWritingOfFieldsOfSubClass() throws IllegalArgumentException, IllegalAccessException {
		Test2SubClass tc = new Test2SubClass();
		MyType mt = Reflecting.getFieldByPath("mySubType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMySubType().getName()).isEqualTo("blubb");
	}

	@Test
	public void TestReadingFieldsOfSubSubClass() {
		List<String> results = Reflecting.getPathsOf(Test3SubSubClass.class, MyType.class, ContainsMyType.class);
		assertThat(results).containsAll(List.of("mySuperType", "mySubType", "mySubSubType"));
	}

	@Test
	public void TestWritingOfOverloadedFieldsOfSubSubClass() throws IllegalArgumentException, IllegalAccessException {
		Test3SubSubClass tc = new Test3SubSubClass();
		MyType mt = Reflecting.getFieldByPath("mySubType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMySubType().getName()).isEqualTo("blubb");
	}

	@Test
	public void TestWritingOfFieldsOfSubSubClass() throws IllegalArgumentException, IllegalAccessException {
		Test3SubSubClass tc = new Test3SubSubClass();
		MyType mt = Reflecting.getFieldByPath("mySubSubType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMySubSubType().getName()).isEqualTo("blubb");
	}
}
