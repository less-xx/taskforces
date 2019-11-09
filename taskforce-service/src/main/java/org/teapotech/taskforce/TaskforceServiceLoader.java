/**
 * 
 */
package org.teapotech.taskforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author jiangl
 *
 */
@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
@EntityScan(basePackages = { "org.teapotech.taskforce.entity", "org.teapotech.credentials.entity",
		"org.teapotech.resource.entity" })
@EnableJpaRepositories(basePackages = { "org.teapotech.taskforce.repo", "org.teapotech.credentials.repo",
		"org.teapotech.resource.repo" })
@ComponentScan(basePackages = { "org.teapotech.taskforce", "org.teapotech.user", "org.teapotech.credentials",
		"org.teapotech.resource" })
public class TaskforceServiceLoader {

	public static void main(String[] args) {
		SpringApplication.run(TaskforceServiceLoader.class, args);
	}
}
