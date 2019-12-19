/**
 * 
 */
package org.teapotech.taskforce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teapotech.base.service.user.User;
import org.teapotech.base.service.user.UserService;
import org.teapotech.taskforce.repo.UserRepo;

/**
 * @author jiangl
 *
 */
@Service
public class DefaultUserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public User findById(String userId) {
		return userRepo.findById(userId).orElseGet(null);
	}

	@Override
	public User findByName(String userName) {
		return userRepo.findByName(userName);
	}

	@Override
	public void userDidLogon(User user) {

	}

}
