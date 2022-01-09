package learning.security.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import learning.security.config.auth.PrincipalDetails;
import learning.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//Spring Security 의 UsernamePasswordAuthenticationFilter 는 /login 요청에서 username, password 를 전송하면 작동
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    //login 요청 시 로그인 시도를 위해 실행되는 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도 중");

        //1. username, password 받기
        try {
            /*
            BufferedReader br = request.getReader();
            String input = null;
            while((input = br.readLine()) != null) System.out.println(input);
             */

            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService 의 loadUserByUsername() 실행 -> 정상 처리되면 authentication 리턴
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //authentication 객체가 session 영역에 저장됨 -> 로그인이 되었다는 뜻
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료 : " +  principalDetails.getUser().getUsername());

            /*
            authentication 객체를 session 영역에 저장하기 위해 리턴
            리턴해야 하는 이유는 권한 관리를 security 가 편하게 할 수 있도록
             */
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2. authenticationManager 로 로그인 시도 -> PrincipalDetailsService 호출 -> loadUserByUsername() 자동 실행

        //3. PrincipalDetails 를 session 에 담기

        //4. JWT 토큰을 만들어서 응답

        return null;
    }

    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행
    //JWT 토큰 생성 및 response
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(JwtProperties.SECRET)
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }

}
