package info.unterrainer.commons.jreutils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JreUtilsTests {

	@Test
	public void walkResourcesTest() throws IOException, URISyntaxException {
		List<Path> l = Resources.walk(JreUtils.class);
		for (Path s : l)
			log.info("- " + s);
	}

	@Test
	public void walkResourcesWithFilterTest() throws IOException, URISyntaxException {
		List<Path> l = Resources.walk(JreUtils.class, path -> path.toString().endsWith("j.properties"));
		for (Path s : l)
			log.info("- " + s);
	}
}
