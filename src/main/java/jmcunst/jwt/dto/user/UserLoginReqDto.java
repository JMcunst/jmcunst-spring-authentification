package jmcunst.jwt.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginReqDto {
    @NotNull(message = "아이디를 입력하세요.")
    private String uid;

    @NotNull(message = "비밀번호를 입력하세요.")
    private String password;

    @Builder
    public UserLoginReqDto(String uid, String password){
        this.uid = uid;
        this.password = password;
    }
}
