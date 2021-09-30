package info.unterrainer.commons.jreutils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ExceptionsTests {

	@Test
	public void codeBeforeTest() {
		try {
			// Some method you don't have control over throwing an exception.
			throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
			// NOOP.
		}
	}

	@Test
	public void throwsExceptionWithoutSwallow() {
		assertThrows(IllegalArgumentException.class, () -> {
			// Some method you don't have control over throwing an exception.
			throw new IllegalArgumentException();
		});
	}

	@Test
	public void swallowsException() {
		Exceptions.swallow(() -> {
			throw new IllegalArgumentException();
		}, IllegalArgumentException.class, NumberFormatException.class);
		assertTrue(true);
	}
}
