package real.zani.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import real.zani.dto.AccessTokenDto;
import real.zani.dto.AuthResultDto;
import real.zani.dto.UserDto;
import real.zani.entity.UserEntity;
import real.zani.exception.BadAuthTrialException;
import real.zani.exception.BadJoinTrialException;
import real.zani.service.AuthService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/login", method= RequestMethod.POST)
	public AuthResultDto login(@RequestBody UserDto userDto, HttpServletRequest request) throws BadAuthTrialException {
		String ipAddress = request.getRemoteAddr();
		String accessToken = authService.login(userDto.getLoginId(), userDto.getPassword(), userDto.getGcmRegId(), ipAddress);
		return new AuthResultDto(accessToken, ipAddress, new Date());
	}
	
	@ResponseStatus(code=HttpStatus.CREATED)
	@RequestMapping(value="/register", method= RequestMethod.POST)
	public AuthResultDto register(@RequestBody UserDto userDto, HttpServletRequest request) throws BadJoinTrialException, BadAuthTrialException {
		UserEntity userEntity = authService.register(userDto);
		String ipAddress = request.getRemoteAddr();
		String accessToken = authService.login(userEntity.getLoginId(), userEntity.getPassword(), userEntity.getGcmRegId(), request.getRemoteAddr());
		return new AuthResultDto(accessToken, ipAddress, new Date());
		
	}
	
	@RequestMapping("/logout")
	public void logout(AccessTokenDto accessTokenDto, @RequestHeader("AccessToken") String accessToken) {
		authService.logout(accessToken);
	}
}
