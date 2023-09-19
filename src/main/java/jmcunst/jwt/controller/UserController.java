package jmcunst.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jmcunst.jwt.common.BaseException;
import jmcunst.jwt.common.BaseResponse;
import jmcunst.jwt.dto.TokenDto;
import jmcunst.jwt.dto.user.UserCreateReqDto;
import jmcunst.jwt.dto.user.UserCreateResDto;
import jmcunst.jwt.dto.user.UserLoginReqDto;
import jmcunst.jwt.dto.user.UserResDto;
import jmcunst.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    public BaseResponse<UserCreateResDto> signUp(@Valid @RequestBody UserCreateReqDto userCreateReqDto, BindingResult br) {

        // 형식적 validation
        if (br.hasErrors()){
            String errorName = br.getAllErrors().get(0).getDefaultMessage(); //dto에 선언한 에러
            log.error(errorName);
            return new BaseResponse<>(errorName);
        }

        try {
            return new BaseResponse<>(userService.signUpUser(userCreateReqDto));
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto, BindingResult br) throws BaseException {

        if (br.hasErrors()){
            String errorName = br.getAllErrors().get(0).getDefaultMessage();
            log.error(errorName);
            return new BaseResponse<>(errorName);
        }

        try {
            return new BaseResponse<>(userService.login(userLoginReqDto));
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @Operation(summary = "사용자 조회")
    @GetMapping("/{num}")
    public BaseResponse<UserResDto> getUser(@PathVariable("num") String num) {
        try {
            return new BaseResponse<>(userService.getUser(num));
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
