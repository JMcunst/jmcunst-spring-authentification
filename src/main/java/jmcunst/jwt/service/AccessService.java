package jmcunst.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessService {

    public String accessTest() {
        return "Access Test SUCCESS";
    }
}
