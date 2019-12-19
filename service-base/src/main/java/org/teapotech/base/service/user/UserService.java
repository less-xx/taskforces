/**
 * 
 */
package org.teapotech.base.service.user;

/**
 * @author jiangl
 *
 */
public interface UserService {

	User findById(String userId);

	User findByName(String userName);

	void userDidLogon(User user);
}
