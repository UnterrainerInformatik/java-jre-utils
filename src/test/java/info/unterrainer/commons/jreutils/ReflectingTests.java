package info.unterrainer.commons.jreutils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import info.unterrainer.commons.jreutils.dtos.ContainsMyType;
import info.unterrainer.commons.jreutils.dtos.MyType;
import info.unterrainer.commons.jreutils.dtos.TestClass;

public class ReflectingTests {

	@Test
	public void TestReadingFieldsWithAnnotation() {
		List<String> results = Reflecting.getPathsOf(TestClass.class, MyType.class, ContainsMyType.class);
		assertThat(results).contains("myType");
		assertThat(results).contains("subClass.myType");
	}

	@Test
	public void TestWritingOfFieldWithAnnotation() throws IllegalArgumentException, IllegalAccessException {
		TestClass tc = new TestClass();
		MyType mt = Reflecting.getFieldByPath("myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getSubClass().getMyType().getName()).isNull();
	}

	@Test
	public void TestWritingOfFieldInSubClassWithAnnotation() throws IllegalArgumentException, IllegalAccessException {
		TestClass tc = new TestClass();
		MyType mt = Reflecting.getFieldByPath("subClass.myType", tc, ContainsMyType.class);
		mt.setName("blubb");
		assertThat(tc.getSubClass().getMyType().getName()).isEqualTo("blubb");
		assertThat(tc.getMyType().getName()).isNull();
	}
}
