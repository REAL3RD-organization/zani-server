package real.zani.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import real.zani.dto.AccessTokenDto;
import real.zani.dto.UserDto;
import real.zani.entity.UserEntity;
import real.zani.exception.UserNotFoundException;
import real.zani.service.UserService;

@RestController
public class FriendController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DozerBeanMapper dozer;
	
	@RequestMapping(value="/friends", method=RequestMethod.GET)
	public List<UserDto> getFriends(AccessTokenDto accessTokenDto) throws UserNotFoundException {
		UserEntity userEntity = userService.findUserByLoginId(accessTokenDto.getLoginId());
		List<UserDto> friends = new ArrayList<UserDto>();
		for(UserEntity friendEntity : userEntity.getFriends()) {
			UserDto friendDto = new UserDto();
			friendDto.setLoginId(friendEntity.getLoginId());
			friendDto.setNickname(friendEntity.getNickname());
			friends.add(friendDto);
		}
		
		return friends;
	}
	
	@RequestMapping(value="/friend/{friendId}", method=RequestMethod.GET)
	public UserDto findFriend(AccessTokenDto accessTokenDto, @PathVariable String friendId) throws UserNotFoundException {
		UserEntity userEntity = userService.findUserByLoginId(friendId);
		
		if(userEntity == null) {
			throw new UserNotFoundException();
		}
		
		return dozer.map(userEntity, UserDto.class);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value="/friend/{friendId}", method=RequestMethod.POST)
	public void addFriend(AccessTokenDto accessTokenDto, @PathVariable String friendId) {
		userService.addFriend(accessTokenDto.getId(), friendId);
	}
	
}
