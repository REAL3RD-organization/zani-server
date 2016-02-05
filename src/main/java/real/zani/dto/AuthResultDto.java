package real.zani.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResultDto {
	private String accessToken;
	private String ipAddress;
	private Date loginDate;
}
