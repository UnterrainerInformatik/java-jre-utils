package info.unterrainer.commons.jreutils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

public class JreUtilsTests {

	@Test
	public void walkResourcesTest() throws IOException, URISyntaxException {
		List<Path> l = Resources.walk();
		for (Path s : l)
			System.out.println("- " + s);
	}

	@Test
	public void walkResourcesWithFilterTest() throws IOException, URISyntaxException {
		List<Path> l = Resources.walk(path -> path.toString().endsWith("j.properties"));
		for (Path s : l)
			System.out.println("- " + s);
	}
}
