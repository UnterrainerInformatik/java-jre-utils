package info.unterrainer.commons.jreutils;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@SuperBuilder()
public class SetIntersection<T> {

	private final Set<T> before;
	private final Set<T> after;

	private final Set<T> create;
	private final Set<T> delete;
	private final Set<T> leave;

	public static <T> SetIntersection<T> of(final Set<T> before, final Set<T> after) {
		Set<T> create = new HashSet<>(after);
		create.removeAll(before);

		Set<T> delete = new HashSet<>(before);
		delete.removeAll(after);

		Set<T> leave = new HashSet<>(after);
		leave.removeAll(create);

		return new SetIntersection<>(before, after, create, delete, leave);
	}
}
