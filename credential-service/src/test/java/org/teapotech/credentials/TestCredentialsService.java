/**
 * 
 */
package org.teapotech.credentials;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.teapotech.credentials.DBConnectionCredentials.AccessType;
import org.teapotech.credentials.Oauth2Credentials.AuthorizationType;
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
	public void testCreateUsernamePasswordCredentials_01() throws Exception {
		UsernamePasswordCredentials c = new UsernamePasswordCredentials();
		c.setUsername("user");
		c.setPassword("pwd");
		Credentials cred = credentialsService.createUsernamePasswordCredentials(c);
		assertNotNull(cred.getCredentials());
		System.out.println(cred.getCredentials());
		cred.setName("Test UsernamePassword Credentials");
		cred = credentialsService.saveCredentials(cred);
		assertNotNull(cred.getId());
		System.out.println(cred.getCredentials());

		Credentials c1 = credentialsService.getCredentialsById(cred.getId());
		assertNotNull(c1);
		Credentials c2 = credentialsService.getDecryptedCredentialsById(cred.getId());
		assertNotNull(c2);
		System.out.println(c2.getCredentials());
		assertTrue(c2.getCredentials().indexOf("{") == 0);

	}

	@Test
	public void testCreateOAuthCredentials_01() throws Exception {
		Oauth2Credentials c = new Oauth2Credentials();
		c.setAuthorizationType(AuthorizationType.CLIENT_CREDENTIALS);
		ClientCredentialsResourceDetails crd = new ClientCredentialsResourceDetails();
		crd.setAccessTokenUri("https://hostname/security-service/oauth/token");
		crd.setAuthenticationScheme(AuthenticationScheme.header);
		crd.setClientAuthenticationScheme(AuthenticationScheme.header);
		crd.setClientId("test_client_id");
		crd.setClientSecret("test_client_secret");
		crd.setId("test_client_secret_detail_id");
		crd.setTokenName("access_token");
		c.setOauth2Resource(crd);
		Credentials cred = credentialsService.createOauth2Credentials(c);
		assertNotNull(cred.getCredentials());
		System.out.println(cred.getCredentials());
		cred.setName("Test OAuth2 Credentials");
		cred = credentialsService.saveCredentials(cred);
		assertNotNull(cred.getId());
		System.out.println(cred.getCredentials());

		Credentials c1 = credentialsService.getCredentialsById(cred.getId());
		assertNotNull(c1);
		Credentials c2 = credentialsService.getDecryptedCredentialsById(cred.getId());
		assertNotNull(c2);
		System.out.println(c2.getCredentials());
		assertTrue(c2.getCredentials().indexOf("{") == 0);

	}

	@Test
	public void testCreateDBConnCredentials_01() throws Exception {
		DBConnectionCredentials c = new DBConnectionCredentials();
		c.setAccessType(AccessType.JDBC);
		c.setConnectionType("PostgreSQL");
		c.setDbname("testDB");
		c.setHostname("localhost");
		c.setPort(5432);
		c.setUsername("db_user");
		c.setPassword("db_pass");
		Credentials cred = credentialsService.createDBConnectionCredentials(c);
		assertNotNull(cred.getCredentials());
		System.out.println(cred.getCredentials());
		cred.setName("Test DB Connection Credentials");
		cred = credentialsService.saveCredentials(cred);
		assertNotNull(cred.getId());
		System.out.println(cred.getCredentials());

		Credentials c1 = credentialsService.getCredentialsById(cred.getId());
		assertNotNull(c1);
		Credentials c2 = credentialsService.getDecryptedCredentialsById(cred.getId());
		assertNotNull(c2);
		System.out.println(c2.getCredentials());
		assertTrue(c2.getCredentials().indexOf("{") == 0);

	}
}
