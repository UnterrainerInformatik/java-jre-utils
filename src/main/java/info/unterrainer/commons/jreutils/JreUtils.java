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
		Cli cli = CliParser.cliFor(args, "JreUtils", "A library of JRE helper tools.").addArg(Arg.String("path")
				.shortName("p").description("the relative path to get the resources-list from").optional()).create();

		if (cli.isHelpSet())
			System.exit(0);

		String path = "";
		if (cli.isArgSet("path"))
			path = cli.getArgValue("path");
		List<Path> r = Resources.walk(path);
		for (Path p : r)
			log.info("[{}]", p.toString());
	}
}
