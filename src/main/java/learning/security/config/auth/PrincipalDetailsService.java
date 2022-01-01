package learning.security.config.auth;

import learning.security.model.User;
import learning.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
Security 설정 - loginProcessUrl("/login")
/login 요청이 들어오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수 실행
 */

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //Security session(Authentication(UserDetails))
    //함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()) {
            return new PrincipalDetails(user.get());
        }
        return null;
    }

}
