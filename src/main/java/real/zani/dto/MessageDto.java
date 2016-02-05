package real.zani.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class MessageDto {
	private String friendLoginId;
	
	@Length(min=2, max=20)
	private String sendName;
	
	@Length(min=2, max=40)
	private String message;
}
