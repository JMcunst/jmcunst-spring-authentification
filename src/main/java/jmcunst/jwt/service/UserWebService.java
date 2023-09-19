package jmcunst.jwt.service;

import jmcunst.jwt.common.BaseException;
import jmcunst.jwt.dto.TokenDto;
import jmcunst.jwt.dto.user.UserCreateReqDto;
import jmcunst.jwt.dto.user.UserCreateResDto;
import jmcunst.jwt.dto.user.UserLoginReqDto;
import jmcunst.jwt.dto.user.UserResDto;
import jmcunst.jwt.entity.Member;
import jmcunst.jwt.entity.Role;
import jmcunst.jwt.repository.MemberRepository;
import jmcunst.jwt.utils.JwtTokenProvider;
import jmcunst.jwt.utils.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static jmcunst.jwt.common.BaseResponseStatus.*;
import static jmcunst.jwt.common.BaseResponseStatus.INVALID_USER_UID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Log4j2
public class UserWebService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final WebDriverUtil webDriverUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateResDto signUpUser(UserCreateReqDto userCreateReqDto) throws BaseException {
        validateDuplicateUser(userCreateReqDto.getUid());

        webDriverUtil.chrome();
        JSONObject userInfo = webDriverUtil.getUserInfoObj(userCreateReqDto.getUid(), userCreateReqDto.getPassword());
        Member member = Member.builder()
                .uid(userCreateReqDto.getUid())
                .password(passwordEncoder.encode(userCreateReqDto.getPassword()))
                .role(userCreateReqDto.getRole() == 0 ? Role.STUDENT : Role.PROFESSOR)
                .name(userInfo.get("givenName").toString())
                .email(userInfo.get("emailAddress").toString())
                .profileUrl(null)
                .department(userInfo.get("department").toString())
                .contact(userInfo.get("mobilePhone").toString())
                .build();

        memberRepository.save(member);

        return UserCreateResDto.from(member);
    }


    // 유저 중복 확인
    private void validateDuplicateUser(String uid) throws BaseException {
        Optional<Member> findUsers = memberRepository.findByUid(uid);
        if (!findUsers.isEmpty()){
            throw new BaseException(USERS_DUPLICATED_NUM);
        }
    }

    @Transactional
    public TokenDto login(UserLoginReqDto userLoginReqDto) throws BaseException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReqDto.getUid(),
                            userLoginReqDto.getPassword()
                    )
            );

            TokenDto tokenDto = new TokenDto(
                    jwtTokenProvider.createAccessToken(authentication),
                    jwtTokenProvider.createRefreshToken(authentication)
            );

            return tokenDto;

        }catch(BadCredentialsException e){
            log.error(INVALID_USER_PW.getMessage());
            throw new BaseException(INVALID_USER_PW);
        }
    }

    public UserResDto getUser(String uid) {
        Optional<Member> members = memberRepository.findByUid(uid);
        Member member = members.orElseThrow(() -> {
            log.error(INVALID_USER_UID.getMessage());
            return  new BaseException(INVALID_USER_UID);
        });

        return UserResDto.from(member);
    }
}
