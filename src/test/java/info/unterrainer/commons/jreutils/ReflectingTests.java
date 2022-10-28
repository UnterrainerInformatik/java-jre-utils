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
	public void TestWritingOfField() throws IllegalArgumentException, IllegalAccessException {
		Test1Class tc = new Test1Class();
		MyType mt = Reflecting.getFieldByPath("myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getUsedClass().getMyType().getName()).isNull();
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
