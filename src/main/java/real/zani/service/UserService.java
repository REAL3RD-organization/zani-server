package real.zani.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import real.zani.entity.UserEntity;
import real.zani.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity findUser(Integer userId) {
		return userRepository.findOne(userId);
	}
	
	public UserEntity findUserByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	public void addFriend(Integer userId, String friendLoginId) {
		UserEntity userEntity = userRepository.findOne(userId);
		UserEntity friendEntity = userRepository.findByLoginId(friendLoginId);
		userEntity.getFriends().add(friendEntity);
		userRepository.save(userEntity);
	}
}
