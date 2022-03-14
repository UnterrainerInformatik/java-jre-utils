package info.unterrainer.commons.jreutils.lerp;

public class Lerp {

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a linear
	 * progression.
	 *
	 * <pre>
	 * ^
	 * |    /
	 * |   /
	 * |  /
	 * | /
	 * ------------->
	 * </pre>
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double linear(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.LINEAR);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a
	 * quadratic progression.
	 *
	 * <pre>
	 * ^
	 * |      /
	 * |    /
	 * |_--
	 * ------------->
	 * </pre>
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double quadratic(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.QUADRATIC);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a cubic
	 * progression.
	 *
	 * <pre>
	 * ^
	 * |    /
	 * |   /
	 * |_--
	 * ------------->
	 * </pre>
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double cubic(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.CUBIC);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using an
	 * exponential progression.
	 *
	 * <pre>
	 * ^
	 * |    |
	 * |    |
	 * |   /
	 * |__-
	 * ------------->
	 * </pre>
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double exponential(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.EXPONENTIAL);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a
	 * bidirectional-linear progression.
	 *
	 * <pre>
	 * ^
	 * |    /\
	 * |   /  \
	 * |  /    \
	 * | /      \
	 * ------------->
	 * </pre>
	 *
	 * It's an arrowhead basically y=x for values [0, 0.5] and y=1-x for values
	 * ]0.5, 1]
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double bidirectionalLinear(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_LINEAR);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a
	 * bidirectional-quadratic-slow progression.
	 *
	 * <pre>
	 * ^
	 * |       /\
	 * |      /  \
	 * |     /    \
	 * |__--        --__
	 * ------------->
	 * </pre>
	 *
	 * The flanks are longer and it doesn't remain long on the plateau.
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double bidirectionalQuadraticSlow(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_QUADRATIC_SLOW);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a
	 * bidirectional-quadratic progression.
	 *
	 * <pre>
	 * ^
	 * |     __
	 * |    -  -
	 * |   /    \
	 * |_-        -_
	 * ------------->
	 * </pre>
	 *
	 * A normal quadratic curve.
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double bidirectionalQuadratic(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_QUADRATIC);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b) using a
	 * bidirectional-quadratic-quick progression.
	 *
	 * <pre>
	 * ^
	 * |    ___
	 * |   /   \
	 * |  |     |
	 * |_/       \_
	 * ------------->
	 * </pre>
	 *
	 * The flanks are steeper and it remains longer on the plateau.
	 *
	 * @param a          the lower bound of the interval
	 * @param b          the upper bound of the interval
	 * @param percentage the percentage of the way the value is between a and b (on
	 *                   the path of the given progression)
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double bidirectionalQuadraticQuick(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_QUADRATIC_QUICK);
	}

	/**
	 * Returns a number within an interval based on the percentage of the path
	 * between those two values [a, b] (p=0 returns a, p=1 returns b). You may
	 * specify different {@link LerpProgression}s which will alter the output
	 * significantly. The default is linear, so p=.5 will return (b-a)/2.
	 *
	 * @param a           the lower bound of the interval
	 * @param b           the upper bound of the interval
	 * @param percentage  the percentage of the way the value is between a and b (on
	 *                    the path of the given progression)
	 * @param progression the given progression to follow
	 * @return the value within [a, b] defined by p and the type of progression
	 */
	public static double withProgression(final double a, final double b, final double percentage,
			final LerpProgression progression) {
		if (percentage <= 0.0D)
			return a;
		if (percentage >= 1.0D)
			return progression.isBidirectional() ? a : b;

		double p;
		switch (progression) {
		case QUADRATIC:
			p = percentage * percentage;
			break;
		case CUBIC:
			p = percentage * percentage * percentage;
		case EXPONENTIAL:
			p = (Math.pow(20d, percentage) - 1d) / 19d;
		case BIDIRECTIONAL_LINEAR:
			if (percentage <= .5d)
				p = percentage;
			else
				p = 1d - percentage;
			p *= 2d;
			break;
		case BIDIRECTIONAL_QUADRATIC_QUICK:
			p = (1d - Math.cos(2d * percentage * Math.PI)) / 2d;
			p = Math.pow(p, .3d);
			break;
		case BIDIRECTIONAL_QUADRATIC:
			p = (1d - Math.cos(2d * percentage * Math.PI)) / 2d;
			break;
		case BIDIRECTIONAL_QUADRATIC_SLOW:
			p = (1d - Math.cos(2d * percentage * Math.PI)) / 2d;
			p = Math.pow(p, 3d);
			break;
		case LINEAR:
		default:
			p = percentage;
			break;
		}

		return a + (b - a) * p;
	}
}
