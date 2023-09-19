package jmcunst.jwt.dto.user;

import jmcunst.jwt.entity.Member;
import jmcunst.jwt.entity.Role;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResDto {
    Long id;
    String num;
    String name;
    String email;
    String department;
    String contact;
    String profileUrl;
    Role role;

    public static UserResDto from(Member user){
        return UserResDto.builder()
                .id(user.getId())
                .num(user.getNum())
                .name(user.getName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .contact(user.getContact())
                .profileUrl(user.getProfileUrl())
                .role(user.getRole())
                .build();
    }
}
