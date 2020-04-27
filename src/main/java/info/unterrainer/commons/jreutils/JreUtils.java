package info.unterrainer.commons.jreutils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import info.unterrainer.commons.cliutils.Arg;
import info.unterrainer.commons.cliutils.Cli;
import info.unterrainer.commons.cliutils.CliParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JreUtils {

	public static void main(final String[] args) throws IOException {
		Cli cli = CliParser.cliFor(args, "JreUtils", "A library of JRE helper tools.")
				.addArg(Arg.String("path").shortName("p")
						.description("the relative path to get the resources-list from").optional())
				.addArg(Arg.String("endswith").shortName("e")
						.description("adds a filter to the list of scanned files: endswith(ARG)"))
				.create();

		if (cli.isHelpSet())
			System.exit(0);

		String path = "";
		if (cli.isArgSet("path"))
			path = cli.getArgValue("path");

		List<Path> r;
		if (cli.isArgSet("endswith")) {
			String ew = cli.getArgValue("endswith");
			log.info("adding endswith [{}]", ew);
			r = Resources.walk(path, p -> p.toString().endsWith(ew));
		} else
			r = Resources.walk(path);
	}
}
