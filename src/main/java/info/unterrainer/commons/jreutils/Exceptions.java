package info.unterrainer.commons.jreutils;

import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Exceptions {

	/**
	 * Swallows any given Exception (checked or unchecked alike) silently.
	 *
	 * @param runnable   the {@link Runnable} that could throw the exception to
	 *                   swallow
	 * @param throwables a List of Exception-types
	 */
	@SafeVarargs
	public static void swallow(final Runnable runnable, final Class<?>... throwables) {
		Supplier<Void> supplier = () -> {
			runnable.run();
			return null;
		};
		swallowReturning(supplier, throwables);
	}

	/**
	 * Swallows any given Exception (checked or unchecked alike) silently.
	 *
	 * @param <T>        the return-value of the {@link Supplier}
	 * @param supplier   the {@link Supplier} that could throw the exception to
	 *                   swallow
	 * @param throwables a List of Exception-types
	 * @return the return-value of the {@link Supplier}
	 */
	@SafeVarargs
	public static <T> T swallowReturning(final Supplier<T> supplier, final Class<?>... throwables) {
		try {
			return supplier.get();
		} catch (Exception throwable) {
			boolean swallow = false;
			for (Class<?> omit : throwables)
				if (omit.isAssignableFrom(throwable.getClass())) {
					swallow = true;
					break;
				}
			if (!swallow)
				throw throwable;
			return null;
		}
	}
}
