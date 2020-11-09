package info.unterrainer.commons.jreutils;

import org.junit.jupiter.api.Test;

public class ExceptionsTests {

	@Test
	public void BeforeTest() {
		try {
			// Some method you don't have control over throwing an exception.
			throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
			// NOOP.
		}
	}

	@Test
	public void AfterTest() {
		Exceptions.swallow(() -> {
			throw new IllegalArgumentException();
		}, IllegalArgumentException.class, NumberFormatException.class);
	}
}
