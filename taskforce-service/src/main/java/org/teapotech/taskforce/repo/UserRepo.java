/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teapotech.taskforce.entity.DefaultUserImpl;

/**
 * @author jiangl
 *
 */
public interface UserRepo
		extends JpaRepository<DefaultUserImpl, String> {

	DefaultUserImpl findByName(String name);
}
