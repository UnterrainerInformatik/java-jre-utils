package info.unterrainer.commons.jreutils;

import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Exceptions {

	/**
	 * Swallows any given Exception (checked or unchecked alike) silently that is
	 * thrown when running the given supplier.
	 *
	 * @param runnable   the {@link Runnable} that could throw the exception to
	 *                   swallow
	 * @param exceptions a List of Exception-types
	 */
	@SafeVarargs
	public static void swallow(final Runnable runnable, final Class<?>... exceptions) {
		Supplier<Void> supplier = () -> {
			runnable.run();
			return null;
		};
		swallowReturning(supplier, exceptions);
	}

	/**
	 * Swallows any given Exception (checked or unchecked alike) silently that is
	 * thrown when running the given supplier and returns the return-value
	 * otherwise.
	 *
	 * @param <T>        the return-value of the {@link Supplier}
	 * @param supplier   the {@link Supplier} that could throw the exception to
	 *                   swallow
	 * @param exceptions a List of Exception-types
	 * @return the return-value of the {@link Supplier}
	 */
	@SafeVarargs
	public static <T> T swallowReturning(final Supplier<T> supplier, final Class<?>... exceptions) {
		try {
			return supplier.get();
		} catch (Throwable e) {
			if (!containsException(e, exceptions))
				throw e;
			return null;
		}
	}

	/**
	 * If one of the specified exceptions occur while running the given supplier, it
	 * retries running the supplier [times] times with [backOffInMillis]ms in
	 * between tries.
	 * <p>
	 * If the number of retries are used up and the supplier still throws an
	 * exception, that exception is re-thrown.
	 *
	 * @param runnable        the {@link Runnable} that could throw the exception to
	 *                        swallow
	 * @param times           the number of times to retry before throwing the
	 *                        exception for real
	 * @param backOffInMillis the time to wait in between retries
	 * @param exceptions      a List of Exception-types to retry upon
	 */
	@SafeVarargs
	public static void retry(final int times, final long backOffInMillis, final Runnable runnable,
			final Class<?>... exceptions) {
		Supplier<Void> supplier = () -> {
			runnable.run();
			return null;
		};
		retryReturning(times, backOffInMillis, supplier, exceptions);
	}

	/**
	 * If one of the specified exceptions occur while running the given supplier, it
	 * retries running the supplier [times] times with [backOffInMillis]ms in
	 * between tries. It returns the return-value of the given supplier.
	 * <p>
	 * If the number of retries are used up and the supplier still throws an
	 * exception, that exception is re-thrown.
	 *
	 * @param <T>             the return-value of the {@link Supplier}
	 * @param supplier        the {@link Supplier} that could throw the exception to
	 *                        retry upon
	 * @param times           the number of times to retry before throwing the
	 *                        exception for real
	 * @param backOffInMillis the time to wait in between retries
	 * @param exceptions      a List of Exception-types to retry upon
	 * @return the return-value of the {@link Supplier}
	 */
	@SafeVarargs
	public static <T> T retryReturning(final int times, final long backOffInMillis, final Supplier<T> supplier,
			final Class<?>... exceptions) {
		for (int i = 0; i < times; i++)
			try {
				return supplier.get();
			} catch (Throwable e) {
				if (!containsException(e, exceptions) || i == times - 1)
					throw e;
				try {
					Thread.sleep(backOffInMillis);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
				}
			}
		return null;
	}

	public static boolean containsException(final Throwable e, final Class<?>... exceptions) {
		for (Class<?> omit : exceptions)
			if (equalsOrCauseEquals(e, omit))
				return true;
		return false;
	}

	private static boolean equalsOrCauseEquals(final Throwable e, final Class<?> omit) {
		if (omit.isAssignableFrom(e.getClass()))
			return true;
		Throwable cause = e.getCause();
		if (cause != null && equalsOrCauseEquals(cause, omit))
			return true;
		return false;
	}
}
