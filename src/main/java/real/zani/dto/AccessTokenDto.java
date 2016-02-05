package real.zani.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Baek on 2016-01-28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenDto {
    private int id;
    private String loginId;
    private String ipAddress;
    private Date loginDate;
}
