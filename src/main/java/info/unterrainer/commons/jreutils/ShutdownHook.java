package info.unterrainer.commons.jreutils;

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
