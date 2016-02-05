package real.zani.service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import real.zani.dto.AccessTokenDto;
import real.zani.dto.UserDto;
import real.zani.entity.UserEntity;
import real.zani.exception.BadAuthTrialException;
import real.zani.exception.BadJoinTrialException;
import real.zani.exception.CryptoException;
import real.zani.repository.UserRepository;
import real.zani.util.AES256;

/**
 * Created by Baek on 2016. 2. 3. 
 */
@Service
public class AuthService {

	@Value("${token.secret}")
	private String secretKey;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DozerBeanMapper dozer;

	public static final ConcurrentHashMap<String, AccessTokenDto> tokenCache = new ConcurrentHashMap<String, AccessTokenDto>();

	/**
	 * 회원가입 수행
	 * @param userDto
	 * @return 
	 */
	public UserEntity register(UserDto userDto) throws BadJoinTrialException {
		UserEntity userEntity = userRepository.findByLoginId(userDto.getLoginId());
		if(userEntity != null) {
			throw new BadJoinTrialException("LoginId duplicated"); 
		}
		
		userEntity = dozer.map(userDto, UserEntity.class);
		userEntity.setCreatedAt(new Date());
		userRepository.save(userEntity);
		
		return userEntity;
	}

	/**
	 * 로그인 로직 수행
	 * @param kakaoToken
	 * @param ipAddress
	 * @param ipAddress 
	 * @return
	 * @throws BadAuthTrialException
     */
	public String login(String loginId, String password, String gcmRegId, String ipAddress) throws BadAuthTrialException {
		UserEntity userEntity = userRepository.findByLoginIdAndPassword(loginId, password);

		try {
			if(userEntity == null) {
				throw new BadAuthTrialException("Invalid User Condition");
			}
			
			userEntity.setGcmRegId(gcmRegId);
			userRepository.save(userEntity);
			return makeAccessToken(userEntity, ipAddress);
		} catch (Exception e) {
			throw new BadAuthTrialException(e.getMessage());
		}
	}

	/**
	 * 로그아웃
	 * @param accessToken
     */
	public void logout(String accessToken) {
		tokenCache.remove(accessToken);
	}

	/**
	 * AccessToken 생성
	 * @param userEntity
	 * @param ipAddress
	 * @return
	 * @throws JsonProcessingException
	 * @throws CryptoException
     */
	private String makeAccessToken(UserEntity userEntity, String ipAddress) throws JsonProcessingException, CryptoException {
		AccessTokenDto accessTokenDto = new AccessTokenDto(userEntity.getId(), userEntity.getLoginId(), ipAddress, new Date());
		String accessTokenJson = objectMapper.writeValueAsString(accessTokenDto);
		String accessToken = AES256.encode(secretKey, accessTokenJson);
		tokenCache.put(accessToken, accessTokenDto);
		return accessToken;
	}

	/**
	 * AccessToken 해석
	 * @param accessToken
	 * @return
	 * @throws CryptoException
	 * @throws IOException
     */
	public AccessTokenDto toAccessTokenDto(String accessToken) throws CryptoException, IOException {
		if(tokenCache.containsKey(accessToken)) {
			return tokenCache.get(accessToken);
		}

		String accessTokenJson = AES256.decode(secretKey, accessToken);
		AccessTokenDto accessTokenDto = objectMapper.readValue(accessTokenJson, AccessTokenDto.class);
		return accessTokenDto;
	}

}
