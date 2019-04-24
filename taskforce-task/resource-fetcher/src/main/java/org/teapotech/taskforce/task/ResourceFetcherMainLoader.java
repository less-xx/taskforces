package org.teapotech.taskforce.task;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.teapotech.taskforce", "org.teapotech.taskforce.task" })
public class ResourceFetcherMainLoader extends AbstractTaskLoader {

	private static Logger LOG = LoggerFactory.getLogger(ResourceFetcherMainLoader.class);

	@Value("${logging.file}")
	String loggingFilePath;

	@Autowired
	ResourceFetcher resourceFetcher;

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(ResourceFetcherMainLoader.class);

		app.setBannerMode(Banner.Mode.OFF);
		System.exit(SpringApplication.exit(app.run(args)));
	}

	@Override
	void execute(String... args) {
		Options options = new Options();

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.getArgs().length == 0) {
				throw new ParseException("Invalid argument");
			}
			String output = resourceFetcher.getResourceAsText(line.getArgs()[0]);
			System.out.println(output);
		} catch (Exception exp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("fetch-resource <URL>", options);
			System.exit(1);
		}
	}

	@Override
	DockerBlockCallable getDockerBlockRunnable() {
		return this.resourceFetcher;
	}

	@Override
	BlockExecutionContext getBlockExecutionContext() {
		return resourceFetcher.getContext();
	}

	@Override
	Object execute(Block block, DockerBlockCallable callable)
			throws BlockExecutionException, BlockExecutorNotFoundException, InvalidBlockExecutorException,
			InvalidBlockException {
		return callable.call(block);
	}

	File getLogFile() {
		return new File(loggingFilePath);
	}
}
