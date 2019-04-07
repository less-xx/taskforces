package org.teapotech.taskforce.leader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskforceLeaderMainLoader implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(TaskforceLeaderMainLoader.class);

	public static void main(String[] args) {
		// LOG.info("Hello world");
		System.exit(SpringApplication.exit(SpringApplication.run(TaskforceLeaderMainLoader.class, args)));
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
