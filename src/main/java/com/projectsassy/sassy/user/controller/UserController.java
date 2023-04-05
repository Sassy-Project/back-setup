package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.user.dto.DuplicateEmailDto;
import com.projectsassy.sassy.user.dto.DuplicateLoginIdDto;
import com.projectsassy.sassy.user.dto.UserJoinDto;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody UserJoinDto joinDto) {
        userService.join(joinDto);
        return ResponseEntity.ok().body("회가입에 성공하였습니다.");
    }

    /**
     * 아이디 중복검사
     */
    @GetMapping("/duplicateLoginId")
    public ResponseEntity duplicateLoginId(@RequestBody DuplicateLoginIdDto duplicateLoginIdDto) {
        userService.duplicateLoginId(duplicateLoginIdDto);
        // false 값이 넘어오기 때문에 api 테스트 필요
        return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
    }

    /**
     * 이메일 중복검사
     */
    @GetMapping("/duplicateEmail")
    public ResponseEntity duplicateEmail(@RequestBody DuplicateEmailDto duplicateEmailDto) {
        userService.duplicateEmail(duplicateEmailDto);

        return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
    }

}
