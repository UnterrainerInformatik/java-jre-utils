package info.unterrainer.commons.jreutils;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@SuperBuilder()
public class SetIntersection {

	private final Set<String> before;
	private final Set<String> after;

	private final Set<String> create;
	private final Set<String> delete;
	private final Set<String> leave;

	public static SetIntersection of(final Set<String> before, final Set<String> after) {
		Set<String> create = new HashSet<>(before);
		create.removeAll(after);

		Set<String> delete = new HashSet<>(after);
		delete.removeAll(before);

		Set<String> leave = new HashSet<>(before);
		leave.removeAll(create);

		return new SetIntersection(before, after, create, delete, leave);
	}
}
