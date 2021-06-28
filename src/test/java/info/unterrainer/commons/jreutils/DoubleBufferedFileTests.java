package info.unterrainer.commons.jreutils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DoubleBufferedFileTests {

	@Test
	public void readFromPreparedFileWorks() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("test"), "txt");
		assertThat(dbf.read()).isEqualTo("test");
	}

	@Test
	public void readThrowsExceptionWhenNoFilePresent() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		try {
			dbf.delete();
			assertThatIOException().isThrownBy(() -> dbf.read());
		} finally {
			dbf.delete();
		}
	}

	@Test
	public void readAfterWriteReturnsSameValue() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		try {
			dbf.write(w -> w.write("test"));
			assertThat(dbf.read()).isEqualTo("test");
		} finally {
			dbf.delete();
		}
	}

	@Test
	public void readAfterWriteAfterWriteReturnsNewValue() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		try {
			dbf.write(w -> w.write("test_old"));
			dbf.write(w -> w.write("test_new"));
			assertThat(dbf.read()).isEqualTo("test_new");
		} finally {
			dbf.delete();
		}
	}

	@Test
	public void modifiedNewIsNullWithoutAnyValueWritten() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		LocalDateTime newest = dbf.getNewestModifiedTime();
		assertThat(newest).isNull();
	}

	@Test
	public void modifiedOldIsNullWithoutAnyValueWritten() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		LocalDateTime oldest = dbf.getOldestModifiedTime();
		assertThat(oldest).isNull();
	}

	@Test
	public void modifiedNewAndOldAreEqualWithOneValueWritten() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		try {
			dbf.write(w -> w.write("test_old"));
			LocalDateTime newest = dbf.getNewestModifiedTime();
			LocalDateTime oldest = dbf.getOldestModifiedTime();
			assertThat(newest).isEqualTo(oldest);
		} finally {
			dbf.delete();
		}
	}

	@Test
	public void modifiedNewAndOldDifferWithTwoValuesWritten() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		try {
			dbf.write(w -> w.write("test_old"));
			dbf.write(w -> w.write("test_new"));
			LocalDateTime newest = dbf.getNewestModifiedTime();
			LocalDateTime oldest = dbf.getOldestModifiedTime();
			assertThat(newest).isAfter(oldest);
		} finally {
			dbf.delete();
		}
	}

	@Test
	public void modifiedAfterThreeWritesReturnsNewestValue() throws IOException {
		DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
		try {
			dbf.write(w -> w.write("test_oldest"));
			LocalDateTime oldest = dbf.getNewestModifiedTime();
			dbf.write(w -> w.write("test_older"));
			LocalDateTime older = dbf.getNewestModifiedTime();
			dbf.write(w -> w.write("test_new"));
			assertThat(dbf.getNewestModifiedTime()).isAfter(older);
			assertThat(dbf.getNewestModifiedTime()).isAfter(oldest);
		} finally {
			dbf.delete();
		}
	}
}
