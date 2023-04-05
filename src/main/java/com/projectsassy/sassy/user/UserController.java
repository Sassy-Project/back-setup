package com.projectsassy.sassy.user;

import com.projectsassy.sassy.user.dto.UserJoinDto;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
