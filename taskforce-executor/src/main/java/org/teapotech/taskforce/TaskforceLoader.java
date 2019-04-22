/**
 * 
 */
package org.teapotech.taskforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.teapotech.taskforce.context.TaskforceContext;

/**
 * @author jiangl
 *
 */
@SpringBootApplication
public class TaskforceLoader implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(TaskforceLoader.class);

	@Autowired
	TaskforceContext taskforceContext;

	public static void main(String[] args) {
		// LOG.info("Hello world");
		System.exit(SpringApplication.exit(SpringApplication.run(TaskforceLoader.class, args)));
	}

	@Override
	public void run(String... args) throws Exception {

		LOG.info("Taskforce context initialized. {}", taskforceContext);

	}

}
