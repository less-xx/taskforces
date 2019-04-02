package org.teapotech.taskforce.expert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceFetcherMainLoader {

	private static Logger LOG = LoggerFactory.getLogger(ResourceFetcherMainLoader.class);

	public static void main(String[] args) {
		LOG.info("Hello world");
		System.exit(SpringApplication.exit(SpringApplication.run(ResourceFetcherMainLoader.class, args)));

	}

}
