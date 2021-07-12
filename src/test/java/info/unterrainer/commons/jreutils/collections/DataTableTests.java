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
}
