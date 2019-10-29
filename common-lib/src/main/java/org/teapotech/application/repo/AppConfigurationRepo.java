/**
 * 
 */
package org.teapotech.application.repo;

import org.springframework.data.repository.CrudRepository;
import org.teapotech.application.entity.AppConfiguration;

/**
 * @author jiangl
 *
 */
public interface AppConfigurationRepo extends CrudRepository<AppConfiguration, String> {

}
