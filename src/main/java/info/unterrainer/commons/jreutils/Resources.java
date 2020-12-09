package info.unterrainer.commons.jreutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class Resources {

	/**
	 * Read a file from resources as a String. Works within JAR files as well, since
	 * it's using the stream accessor.
	 *
	 * @param path the path of the file you want to read
	 * @return the contents of the file as a String
	 * @throws IOException if there is a problem opening or reading the file given
	 */
	public static String readResource(final Class<?> classLoaderSource, final String path) throws IOException {
		InputStream inputStream = classLoaderSource.getResourceAsStream(path);
		StringBuilder textBuilder = new StringBuilder();
		try (Reader reader = new BufferedReader(
				new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c = 0;
			while ((c = reader.read()) != -1)
				textBuilder.append((char) c);
		}
		return textBuilder.toString();
	}

	/**
	 * Walks the directory tree and returns a List of {@link Path} with all the
	 * paths.
	 * <p>
	 * This method starts walking the tree in the base-directory (so it walks all
	 * the files within your distribution).
	 *
	 * @return the List of Paths
	 * @throws IOException if something went wrong opening a directory
	 */
	public static List<Path> walk(final Class<?> classLoaderSource) throws IOException {
		return walk(classLoaderSource, e -> true);
	}

	/**
	 * Walks the directory tree and returns a List of {@link Path} with all the
	 * paths.
	 * <p>
	 * path: "", "/" or null... the execution folder of the runtime (this is the
	 * default for this method. If you send an empty string, the slash is appended
	 * automatically).
	 * <p>
	 * This method strips the base-path you have given from the result file-names so
	 * you can easily get those with class.getResource().
	 *
	 * @param path the path to start searching in
	 * @return the List of Paths
	 * @throws IOException if something went wrong opening a directory
	 */
	public static List<Path> walk(final Class<?> classLoaderSource, final String path) throws IOException {
		return walk(classLoaderSource, path, e -> true);
	}

	/**
	 * Walks the directory tree and returns a List of {@link Path} with all the
	 * paths.
	 * <p>
	 * This method starts walking the tree in the base-directory (so it walks all
	 * the files within your distribution).
	 *
	 * @param fileNameFilters a single or multiple filters to match the file-names
	 *                        against (and is implied)
	 * @return the List of Paths
	 * @throws IOException if something went wrong opening a directory
	 */
	@SafeVarargs
	public static List<Path> walk(final Class<?> classLoaderSource, final Predicate<Path>... fileNameFilters)
			throws IOException {
		return walk(classLoaderSource, null, fileNameFilters);
	}

	/**
	 * Walks the directory tree and returns a List of relative {@link Path} objects
	 * with all the paths matching your 'fileNameFilter' {@link Predicate}.
	 * <p>
	 * path: "", "/" or null... the execution folder of the runtime (this is the
	 * default for this method. If you send an empty string, the slash is appended
	 * automatically).
	 * <p>
	 * This method strips the base-path you have given from the result file-names so
	 * you can easily get those with class.getResource().
	 *
	 * @param relativePath    the path to start searching in
	 * @param fileNameFilters a single or multiple filters to match the file-names
	 *                        against (and is implied)
	 * @return the List of Paths
	 * @throws IOException if something went wrong opening a directory
	 */
	@SafeVarargs
	public static List<Path> walk(final Class<?> classLoaderSource, final String relativePath,
			final Predicate<Path>... fileNameFilters) throws IOException {
		Stream<Path> stream = null;
		List<Predicate<Path>> allPredicates = Arrays.asList(fileNameFilters);
		String path = relativePath;
		if (path == null || path.isEmpty())
			path = "/";

		try {
			URI uri = classLoaderSource.getResource(path).toURI();
			if ("jar".equals(uri.getScheme())) {
				log.debug("Jar found. Running zip-walker.");
				return walkJar(classLoaderSource, allPredicates);
			} else {
				log.debug("Running file-walker.");
				Path basePath = Paths.get(uri);
				log.debug("Walking from base-directory: [{}]", basePath);
				stream = Files.walk(basePath, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS);
				List<Path> list = stream.collect(Collectors.toList());
				List<Path> results = new ArrayList<>();
				for (Path p : list)
					filterFor(allPredicates, results, basePath.relativize(p));
				return results;
			}
		} catch (URISyntaxException e) {
			log.error("This should never happen!", e);
			// NOOP (never happens)
		} finally {
			if (stream != null)
				stream.close();
		}
		return null;
	}

	private static List<Path> walkJar(final Class<?> classLoaderSource, final List<Predicate<Path>> allPredicates)
			throws URISyntaxException, IOException, FileNotFoundException {
		File jarFile = new File(classLoaderSource.getProtectionDomain().getCodeSource().getLocation().toURI());
		try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile))) {
			List<Path> results = new ArrayList<>();
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				String name = entry.getName();
				filterFor(allPredicates, results, Path.of(name));
			}
			return results;
		}
	}

	private static void filterFor(final List<Predicate<Path>> allPredicates, final List<Path> results, final Path p) {
		boolean failed = false;
		for (Predicate<Path> predicate : allPredicates)
			if (!predicate.test(p)) {
				failed = true;
				break;
			}
		if (!p.toString().isEmpty() && !failed) {
			log.debug("pathscan accepted [{}]", p.toString());
			results.add(p);
		}
	}
}
