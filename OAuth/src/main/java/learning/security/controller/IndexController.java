package learning.security.controller;

import learning.security.config.auth.PrincipalDetails;
import learning.security.model.User;
import learning.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication : " + principalDetails.getUser());
        System.out.println("UserDetails : " + userDetails.getUsername());
        return "session 정보 확인";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User user) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("Authentication : " + oAuth2User.getAttributes());
        System.out.println("OAuth 2 User : " + user.getAttributes());
        return "OAuth session 정보 확인";
    }

    //일반 로그인과 OAuth 로그인 둘 다 PrincipalDetails 정보를 받아올 수 있음
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("Principal Details : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @GetMapping("/info")
    @Secured("ROLE_ADMIN")
    public @ResponseBody String info() {
        return "개인 정보";
    }

    @GetMapping("/data")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")   //여러 개의 역할에 적용하고 싶을 때 주로 사용
    public @ResponseBody String data() {
        return "데이터 정보";
    }

}
