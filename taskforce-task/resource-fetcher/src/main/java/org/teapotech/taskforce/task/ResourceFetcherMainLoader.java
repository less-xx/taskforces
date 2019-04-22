package org.teapotech.taskforce.task;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceFetcherMainLoader implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(ResourceFetcherMainLoader.class);

	@Autowired
	ResourceFetcher resourceFetcher;

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(ResourceFetcherMainLoader.class);
		app.setBannerMode(Banner.Mode.OFF);
		System.exit(SpringApplication.exit(app.run(args)));
	}

	@Override
	public void run(String... args) throws Exception {
		Options options = new Options();

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.getArgs().length == 0) {
				throw new ParseException("Invalid argument");
			}
			String output = resourceFetcher.getResourceAsText(line.getArgs()[0]);
			System.out.println(output);
		} catch (ParseException exp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("fetch-resource <URL>", options);
			System.exit(1);
		}
	}

}
