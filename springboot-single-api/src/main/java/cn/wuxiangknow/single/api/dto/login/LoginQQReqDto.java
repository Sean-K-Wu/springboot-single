package cn.wuxiangknow.single.api.dto.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class LoginQQReqDto {
    private String openid;
    private String unionid;
    private String nickname;
    private String headImgUrl;
}
