package info.unterrainer.commons.jreutils;

import java.util.Random;

public class Randomizer {

	private final Random random = new Random();

	public double nextDouble() {
		return random.nextDouble();
	}

	public float nextFloat() {
		return random.nextFloat();
	}

	public int nextInt() {
		return random.nextInt();
	}

	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	public long nextLong() {
		return random.nextLong();
	}

	public double nextGaussian() {
		return random.nextGaussian();
	}

	public double getDoubleBetween(final double min, final double max) {
		return min + random.nextDouble() * (max - min);
	}

	public double getFloatBetween(final float min, final float max) {
		return (float) getDoubleBetween(min, max);
	}

	public double getIntBetween(final int min, final int max) {
		return (int) getDoubleBetween(min, max);
	}

	public double getLongBetween(final long min, final long max) {
		return (long) getDoubleBetween(min, max);
	}
}
