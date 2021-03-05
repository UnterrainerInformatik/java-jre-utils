package info.unterrainer.commons.jreutils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

	public static DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static LocalDateTime nowUtc() {
		return LocalDateTime.now(ZoneOffset.UTC);
	}

	public static Long utcLocalDateTimeToEpoch(final LocalDateTime utc) {
		return utc.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
	}

	public static LocalDateTime epochToUtcLocalDateTime(final Long epoch) {
		return Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC")).toLocalDateTime();
	}

	public static int getWeekOf(final LocalDateTime dateTime) {
		return dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
	}

	/**
	 * Converts a UTC-issued LocalDateTime to a LocalDateTime issued by the
	 * time-zone specified by the timeZoneIdString.
	 *
	 * @param utc              the input UTC-issued LocalDateTime to convert
	 * @param timeZoneIdString a String describing the target time-zone (for example
	 *                         "Europe/Vienna")
	 * @return the new LocalDateTime
	 */
	public static LocalDateTime utcLocalDateTimeAtZone(final LocalDateTime utc, final String timeZoneIdString) {
		ZonedDateTime utcZoned = ZonedDateTime.of(utc, ZoneId.of("UTC"));
		return utcZoned.withZoneSameInstant(ZoneId.of(timeZoneIdString)).toLocalDateTime();
	}

	public static String utcLocalDateTimeToLocalFormat(final LocalDateTime utc, final String timeZoneIdString) {
		return utcLocalDateTimeToLocalFormat(utc, timeZoneIdString, isoDateTimeFormatter);
	}

	public static String utcLocalDateTimeToLocalFormat(final LocalDateTime utc, final String timeZoneIdString,
			final DateTimeFormatter dateTimeFormatter) {
		LocalDateTime local = utcLocalDateTimeAtZone(utc, timeZoneIdString);
		return local.format(dateTimeFormatter);
	}
}
