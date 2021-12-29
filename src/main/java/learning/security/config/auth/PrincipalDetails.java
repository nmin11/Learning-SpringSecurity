package learning.security.config.auth;

import learning.security.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
시큐리티가 /login 요청을 대신 처리
로그인 진행이 완료되면 session 을 만들어줌 (Security ContextHolder)
Object => Authentication 타입 객체
Authentication 안에 User 정보가 있어야 함
User Object 타입 => UserDetails 타입 객체

Security Session => Authentication => UserDetails
 */

@AllArgsConstructor
public class PrincipalDetails implements UserDetails {

    private User user;

    //해당 유저의 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
