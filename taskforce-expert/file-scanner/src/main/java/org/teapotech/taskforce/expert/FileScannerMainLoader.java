package org.teapotech.taskforce.expert;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileScannerMainLoader implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(FileScannerMainLoader.class);

	@Autowired
	FileScanner fileScanner;

	public static void main(String[] args) {
		// LOG.info("Hello world");
		System.exit(SpringApplication.exit(SpringApplication.run(FileScannerMainLoader.class,
				args)));
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
			File[] files = fileScanner.getFiles(args[0]);
			printFiles(files);
		} catch (ParseException exp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("file-scanner <directory>", options);
		} catch (FileNotFoundException e) {
			System.out.println("");
		}
	}

	private void printFiles(File[] files) {
		System.out.println("total " + files.length);
		for (File f : files) {
			String name = f.getName();
			if (f.isDirectory()) {
				name = "/" + name;
			}
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(f.lastModified()));
			String line = String.format("%-20s\t%12d\t%s", name, f.length(), timestamp);

			System.out.println(line);
		}
	}

}
