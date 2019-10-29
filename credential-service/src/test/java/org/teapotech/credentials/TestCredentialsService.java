/**
 * 
 */
package org.teapotech.credentials;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.service.impl.CredentialsServiceImpl;

/**
 * @author jiangl
 *
 */
@SpringBootTest
@EnableAutoConfiguration
@EntityScan({ "org.teapotech.credentials.entity" })
@EnableJpaRepositories(basePackages = { "org.teapotech.credentials.repo" })
@ComponentScan("org.teapotech.credentials")
@SpringJUnitConfig(SpringBootContextLoader.class)
public class TestCredentialsService {

	@Autowired
	CredentialsServiceImpl credentialsService;

	@BeforeEach
	public void init() {
		assertNotNull(credentialsService);
	}

	@Test
	public void testCreateCredentials_01() throws Exception {
		UsernamePasswordCredentials c = new UsernamePasswordCredentials();
		c.setUsername("user");
		c.setPassword("pwd");
		Credentials cred = credentialsService.createUsernamePasswordCredentials(c);
		assertNotNull(cred.getCredentials());
	}
}
