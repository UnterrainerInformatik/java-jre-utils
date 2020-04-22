package info.unterrainer.commons.jreutils;

import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Exceptions {

	public static void swallow(final Runnable runnable, final Exception... throwables) {
		Supplier<Void> supplier = () -> {
			runnable.run();
			return null;
		};
		swallowReturning(supplier, throwables);
	}

	public static <T> T swallowReturning(final Supplier<T> supplier, final Exception... throwables) {
		try {
			return supplier.get();
		} catch (Exception throwable) {
			boolean swallow = false;
			for (Exception omit : throwables)
				if (omit.getClass().isAssignableFrom(throwable.getClass())) {
					swallow = true;
					break;
				}
			if (!swallow)
				throw throwable;
			return null;
		}
	}
}
