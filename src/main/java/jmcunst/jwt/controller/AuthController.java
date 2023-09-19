package jmcunst.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jmcunst.jwt.common.BaseResponse;
import jmcunst.jwt.dto.TokenDto;
import jmcunst.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "토큰 재발행")
    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(String refreshToken){
        return new BaseResponse<>(authService.reissueToken(refreshToken));
    }
}
