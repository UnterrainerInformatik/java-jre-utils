package info.unterrainer.commons.jreutils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ShutdownHook {

	public static void register(final Runnable... runnables) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (Runnable runnable : runnables)
					runnable.run();
			}
		});
	}
}
