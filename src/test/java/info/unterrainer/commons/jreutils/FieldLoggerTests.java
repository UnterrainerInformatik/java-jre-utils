package info.unterrainer.commons.jreutils;

import org.junit.jupiter.api.Test;

import info.unterrainer.commons.jreutils.dtos.Test1Class;
import info.unterrainer.commons.jreutils.logging.FieldLogger;

public class FieldLoggerTests {

	@Test
	public void TestReadingFields() {
		Test1Class t = new Test1Class();
		t.setName("name");
		t.setVendor("vendor");
		t.setType(2L);
		t.setAddress("This is an address.");
		t.setClassName("Classname should not be visible because it's not annotated correctly.");
		StringBuilder sb = new StringBuilder();
		sb.append("=========================================");
		sb.append(System.lineSeparator());
		sb.append(FieldLogger.log(t, Test1Class.class));
		sb.append("=========================================");
		sb.append(System.lineSeparator());
		System.out.println(sb.toString());
	}

	@Test
	public void TestReadingLists() {
		Test1Class t = new Test1Class();
		StringBuilder sb = new StringBuilder();
		sb.append("=========================================");
		sb.append(System.lineSeparator());
		sb.append(FieldLogger.log(t, Test1Class.class));
		sb.append("=========================================");
		sb.append(System.lineSeparator());
		System.out.println(sb.toString());
	}
}
