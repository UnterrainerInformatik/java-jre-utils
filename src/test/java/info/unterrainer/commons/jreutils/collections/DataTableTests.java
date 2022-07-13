package info.unterrainer.commons.jreutils.collections;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DataTableTests {

	@Test
	public void peekingWorks() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.add("test");
		assertThat(dt.peek()).isEqualTo("test");
	}

	@Test
	public void pollingWorks() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.add("test");
		assertThat(dt.poll()).isEqualTo("test");
	}

	@Test
	public void clearingWorks() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.add("test");
		dt.clear();
		assertThat(dt.peek()).isNull();
		assertThat(dt.poll()).isNull();
	}

	@Test
	public void addingIndexWorks() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.addIndex("index1", e -> e);
	}

	@Test
	public void addingWorksWithIndex() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.addIndex("index1", e -> e);
		dt.add("test");
		assertThat(dt.get("index1", "test")).isEqualTo("test");
	}

	@Test
	public void addingWorksWithTwoIndexes() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.addIndex("index1", e -> e);
		dt.addIndex("index2", e -> e);
		dt.add("test");
		assertThat(dt.get("index1", "test")).isEqualTo("test");
		assertThat(dt.get("index2", "test")).isEqualTo("test");
	}

	@Test
	public void deletingFromQueueAlsoDeletesFromAllIndexes() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.addIndex("index1", e -> e);
		dt.addIndex("index2", e -> e);
		dt.add("test");
		dt.poll();
		assertThat(dt.get("index1", "test")).isNull();
		assertThat(dt.get("index2", "test")).isNull();
	}

	@Test
	public void clearingWorksWithTwoIndexes() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.addIndex("index1", e -> e);
		dt.addIndex("index2", e -> e);
		dt.add("test");
		dt.clear();
		assertThat(dt.peek()).isNull();
		assertThat(dt.poll()).isNull();
		assertThat(dt.get("index1", "test")).isNull();
		assertThat(dt.get("index2", "test")).isNull();
	}

	@Test
	public void addingTwoFragmentationsWorks() {
		DataTable<String> dt = new DataTable<>(String.class, 10);
		dt.addIndex("index1", e -> e, e -> e.startsWith("item"));
		dt.addIndex("index2", e -> e, e -> e.startsWith("other"));

		dt.add("item1");
		dt.add("item2");

		dt.add("other1");
		dt.add("other2");

		assertThat(dt.get("index1", "item1")).isEqualTo("item1");
		assertThat(dt.get("index1", "item2")).isEqualTo("item2");
		assertThat(dt.get("index1", "other1")).isNull();
		assertThat(dt.get("index1", "other2")).isNull();

		assertThat(dt.get("index2", "other1")).isEqualTo("other1");
		assertThat(dt.get("index2", "other2")).isEqualTo("other2");
		assertThat(dt.get("index2", "item1")).isNull();
		assertThat(dt.get("index2", "item2")).isNull();

		assertThat(dt.keySet("index1")).containsExactly("item1", "item2");
		assertThat(dt.keySet("index2")).containsExactly("other1", "other2");
	}
}
