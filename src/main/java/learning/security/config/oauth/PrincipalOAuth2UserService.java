package learning.security.config.oauth;

import learning.security.config.auth.PrincipalDetails;
import learning.security.model.User;
import learning.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    //Google 로부터 받은 userRequest 데이터에 대한 후처리 함수
    //함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("Get Attributes : " + oAuth2User.getAttributes());
        String provider = userRequest.getClientRegistration().getClientId();    //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = "GetInThere";
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        Optional<User> findUser = userRepository.findByUsername(username);
        User user = null;

        if(findUser.isEmpty()) {
            System.out.println("최초의 구글 로그인입니다.");
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            System.out.println("이미 구글 로그인을 진행했습니다. 자동 회원가입이 되어 있습니다.");
            user = findUser.get();
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}
