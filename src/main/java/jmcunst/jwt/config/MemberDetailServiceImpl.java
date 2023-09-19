package jmcunst.jwt.config;

import jmcunst.jwt.common.BaseException;
import jmcunst.jwt.entity.Member;
import jmcunst.jwt.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static jmcunst.jwt.common.BaseResponseStatus.INVALID_USER_NUM;

@Service
@Log4j2
public class MemberDetailServiceImpl implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String num) throws BaseException {
        Member user = memberRepository.findByNum(num)
                .orElseThrow(() -> {
                    log.error(INVALID_USER_NUM.getMessage());
                    return new BaseException(INVALID_USER_NUM);
                });

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        return new org
                .springframework
                .security
                .core
                .userdetails
                .User(user.getNum(), user.getPassword(), grantedAuthorities);
    }
}
