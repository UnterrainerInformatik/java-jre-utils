package info.unterrainer.commons.jreutils.lerp;

public class Lerp {

	public static double quadratic(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.QUADRATIC);
	}

	public static double cubic(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.CUBIC);
	}

	public static double exponential(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.EXPONENTIAL);
	}

	public static double bidirectionalLinear(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_LINEAR);
	}

	public static double bidirectionalQuadraticQuick(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_QUADRATIC_QUICK);
	}

	public static double bidirectionalQuadratic(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_QUADRATIC);
	}

	public static double bidirectionalQuadraticSlow(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.BIDIRECTIONAL_QUADRATIC_SLOW);
	}

	public static double linear(final double a, final double b, final double percentage) {
		return withProgression(a, b, percentage, LerpProgression.LINEAR);
	}

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
