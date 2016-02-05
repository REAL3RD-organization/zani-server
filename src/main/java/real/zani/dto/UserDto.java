package real.zani.dto;

import lombok.Data;

@Data
public class UserDto {

	private Integer id;
	
	private String loginId;
	
	private String password;

	private String nickname;
	
	private String gcmRegId;
}
