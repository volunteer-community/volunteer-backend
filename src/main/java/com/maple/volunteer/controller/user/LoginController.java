package com.maple.volunteer.controller.user;

import com.maple.volunteer.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login/oauth2",produces = "application/json")
@RequiredArgsConstructor
public class LoginController {
    private final CustomOAuth2UserService loginService;
//    @GetMapping("/code/{registrationId}")
//    public void googleLogin(@RequestParam String code , @PathVariable String registrationId){
//        loginService.socialLogin(code, registrationId);
//    }


}