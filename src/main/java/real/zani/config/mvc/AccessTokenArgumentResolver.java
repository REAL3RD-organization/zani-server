package real.zani.config.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import real.zani.dto.AccessTokenDto;
import real.zani.exception.BadAccessTokenException;
import real.zani.service.AuthService;

@Configuration
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private AuthService authService;

	public AccessTokenArgumentResolver() {
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(AccessTokenDto.class);
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory
	) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) webRequest
				.getNativeRequest();
		String accessToken = httpRequest.getHeader("AccessToken");

		try {
			AccessTokenDto accessTokenDto = authService.toAccessTokenDto(accessToken);
			return accessTokenDto;
		} catch (Exception e) {
			throw new BadAccessTokenException(e.getMessage());
		}
	}
}