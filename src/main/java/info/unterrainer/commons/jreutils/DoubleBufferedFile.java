package info.unterrainer.commons.jreutils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Allows to access (read/write) a 'doubleBuffered file', meaning that there are
 * two files which get written to alternately.<br>
 * The files are named pathToFile/file1.fileExtension and
 * pathToFile/file2.fileExtension
 * <p>
 * This allows for some fault-tolerance when it comes to corrupted files, since
 * you always have, at least, the other, albeit older file at your disposal.
 * <p>
 * You create this with a path to a virtual file that should not exist, since it
 * misses the numbers.<br>
 * Use the methods to retrieve a write-handle to the correct (older) file and a
 * read-handle as well (newer).
 * <p>
 * Throws an IOException if something happens it cannot handle any longer (both
 * files are locked for write-access and you're requesting write-access for
 * example).
 */
@Accessors(fluent = true)
public class DoubleBufferedFile {

	@FunctionalInterface
	public interface ConsumerWithIoException<T> {
		/**
		 * Performs this operation on the given argument.
		 *
		 * @param t the input argument
		 * @throws IOException if one occurs
		 */
		void accept(T t) throws IOException;

		/**
		 * Returns a composed {@code Consumer} that performs, in sequence, this
		 * operation followed by the {@code after} operation. If performing either
		 * operation throws an exception, it is relayed to the caller of the composed
		 * operation. If performing this operation throws an exception, the
		 * {@code after} operation will not be performed.
		 *
		 * @param after the operation to perform after this operation
		 * @return a composed {@code Consumer} that performs in sequence this operation
		 *         followed by the {@code after} operation
		 * @throws NullPointerException if {@code after} is null
		 * @throws IOException          if one occurs
		 */
		default ConsumerWithIoException<T> andThen(final ConsumerWithIoException<? super T> after) throws IOException {
			Objects.requireNonNull(after);
			return (final T t) -> {
				accept(t);
				after.accept(t);
			};
		}
	}

	@Data
	class DoubleBufferedFileData {
		private final Path path;
		private boolean exists;
		private LocalDateTime modified;
		private boolean readable;
		private boolean writable;

		DoubleBufferedFileData(final Path path) {
			super();
			this.path = path;
			probe();
		}

		void delete() throws IOException {
			if (Files.exists(path, LinkOption.NOFOLLOW_LINKS))
				Files.delete(path);
		}

		void probe() {
			exists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);

			writable = Files.isWritable(path);
			readable = Files.isReadable(path);

			modified = null;
			if (exists)
				try {
					modified = DateUtils.fileTimeToUtcLocalDateTime(
							Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime());
				} catch (IOException e) {
					modified = null;
					readable = false;
					writable = false;
				}
		}

		DoubleBufferedFileData withCheckedWrite() throws IOException {
			if (!writable)
				throw new IOException(String.format("There is no write-access for the given path [%s].", path));
			return this;
		}

		DoubleBufferedFileData withCheckedRead() throws IOException {
			if (!readable)
				throw new IOException(String.format("There is no read-access for the given path [%s].", path));
			return this;
		}

		BufferedWriter getBufferedWriter() throws IOException {
			return Files.newBufferedWriter(path, Charset.forName("UTF-8"));
		}
	}

	protected DoubleBufferedFileData path1;
	protected DoubleBufferedFileData path2;

	public DoubleBufferedFile(final Path pathWithoutNumber, final String fileExtension) {
		path1 = new DoubleBufferedFileData(Path.of(pathWithoutNumber.toString() + "1." + fileExtension));
		path2 = new DoubleBufferedFileData(Path.of(pathWithoutNumber.toString() + "2." + fileExtension));
	}

	public void delete() throws IOException {
		path1.delete();
		path2.delete();
	}

	public LocalDateTime getNewestModifiedTime() {
		if (path1.modified() == null && path2.modified() == null)
			return null;
		if (path1.modified() == null)
			return path2.modified();
		if (path2.modified() == null)
			return path1.modified();

		if (path1.modified().compareTo(path2.modified()) > 0)
			return path1.modified();
		return path2.modified();
	}

	public LocalDateTime getOldestModifiedTime() {
		if (path1.modified() == null && path2.modified() == null)
			return null;
		if (path1.modified() == null)
			return path2.modified();
		if (path2.modified() == null)
			return path1.modified();

		if (path1.modified().compareTo(path2.modified()) <= 0)
			return path1.modified();
		return path2.modified();
	}

	private DoubleBufferedFileData getOldestForWriteAccess() throws IOException {
		path1.probe();
		path2.probe();
		if (!path1.exists() && !path2.exists())
			return path1;
		if (!path1.exists())
			return path1;
		if (!path2.exists())
			return path2;

		if (!path1.writable() && !path2.writable())
			throw new IOException("Both files are locked for write-access.");
		if (!path1.writable())
			throw new IOException("File1 is locked for write-access.");
		if (!path2.writable())
			throw new IOException("File2 is locked for write-access.");

		if (path1.modified() == null || path2.modified() == null)
			throw new IOException("Could not read the modified-date from one of the files.");
		if (path1.modified().compareTo(path2.modified()) <= 0)
			return path1;
		return path2;
	}

	private DoubleBufferedFileData getNewestForReadAccess() throws IOException {
		path1.probe();
		path2.probe();
		if (!path1.exists() && !path2.exists())
			throw new IOException("There is no file to read from, because both files are missing.");
		if (!path1.exists())
			return path2.withCheckedRead();
		if (!path2.exists())
			return path1.withCheckedRead();

		if (!path1.readable() && !path2.readable())
			throw new IOException("Both files are locked for read-access.");
		if (!path1.readable())
			throw new IOException("File1 is locked for read-access.");
		if (!path2.readable())
			throw new IOException("File2 is locked for read-access.");

		if (path1.modified() == null || path2.modified() == null)
			throw new IOException("Could not read the modified-date from one of the files.");
		if (path1.modified().compareTo(path2.modified()) > 0)
			return path1;
		return path2;
	}

	public void write(final ConsumerWithIoException<BufferedWriter> writeContentDelegate) throws IOException {
		DoubleBufferedFileData p = getOldestForWriteAccess();
		if (p.exists())
			Files.delete(p.path());

		try (BufferedWriter writer = p.getBufferedWriter()) {
			writeContentDelegate.accept(writer);
		}
		p.probe();
	}

	public String read() throws IOException {
		DoubleBufferedFileData p = getNewestForReadAccess();
		return Files.readString(p.path());
	}
}
