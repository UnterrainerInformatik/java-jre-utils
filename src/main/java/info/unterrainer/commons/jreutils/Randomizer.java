package info.unterrainer.commons.jreutils;

import java.security.SecureRandom;

/**
 * Uses {@link SecureRandom} to overcome the limitations of Java's 48 bit random
 * seed therefore providing a nextLong() implementation that does indeed
 * potentially hit all of the available (64bit) Long values.
 */
public class Randomizer {

	private final SecureRandom random = new SecureRandom();

	/**
	 * Gets the next pseudorandom, uniformly distributed double within the interval
	 * [0, 1[.
	 *
	 * @return the next double within the given interval
	 */
	public double nextDouble() {
		return random.nextDouble();
	}

	/**
	 * Gets the next pseudorandom, uniformly distributed float within the interval
	 * [0, 1[.
	 *
	 * @return the next float within the given interval
	 */
	public float nextFloat() {
		return random.nextFloat();
	}

	/**
	 * Gets the next pseudorandom, uniformly distributed integer within the interval
	 * [Integer.MIN_VALUE, Integer.MAX_VALUE].
	 *
	 * @return the next integer within the given interval
	 */
	public int nextInt() {
		return random.nextInt();
	}

	/**
	 * Gets the next pseudorandom, uniformly distributed boolean.
	 *
	 * @return the next boolean
	 */
	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Gets the next pseudorandom, uniformly distributed long within
	 * [Long.MIN_VALUE, Long.MAX_VALUE].
	 *
	 * @return the next double within the given interval
	 */
	public long nextLong() {
		return random.nextLong();
	}

	/**
	 * Gets the next pseudorandom, uniformly distributed double within the interval
	 * [0, 1[.
	 *
	 * @return the next double within the given interval
	 */
	public double nextGaussian() {
		return random.nextGaussian();
	}

	/**
	 * Gets the next double within the interval [minimum, maximum]. Swaps minimum
	 * and maximum if minimum &gt; maximum.
	 *
	 * @param minimum the lower bound
	 * @param maximum the upper bound
	 * @return a random double within the given interval
	 */
	public double nextDouble(final double minimum, final double maximum) {
		double min = minimum;
		double max = maximum;
		if (min > max) {
			min = maximum;
			max = minimum;
		}
		return min + random.nextDouble() * (max - min);
	}

	/**
	 * Gets the next float within the interval [minimum, maximum]. Swaps minimum and
	 * maximum if minimum &gt; maximum.
	 *
	 * @param minimum the lower bound
	 * @param maximum the upper bound
	 * @return a random float within the given interval
	 */
	public double nextFloat(final float minimum, final float maximum) {
		return (float) nextDouble(minimum, maximum);
	}

	/**
	 * Gets the next integer within the interval [minimum, maximum]. Swaps minimum
	 * and maximum if minimum &gt; maximum.
	 *
	 * @param minimum the lower bound
	 * @param maximum the upper bound
	 * @return a random integer within the given interval
	 */
	public double nextInt(final int minimum, final int maximum) {
		int max = maximum;
		if (maximum == Integer.MAX_VALUE)
			max = maximum - 1;
		return (int) nextInt(minimum, max + 1);
	}

	/**
	 * Gets the next long within the interval [minimum, maximum]. Swaps minimum and
	 * maximum if minimum &gt; maximum.
	 *
	 * @param minimum the lower bound
	 * @param maximum the upper bound
	 * @return a random long within the given interval
	 */
	public double nextLong(final long minimum, final long maximum) {
		return (long) nextDouble(minimum, maximum);
	}
}
