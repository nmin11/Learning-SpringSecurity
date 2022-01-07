package learning.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);    //JSON 을 JavaScript 에서 처리할 수 있도록
        configuration.addAllowedOrigin("*");        //모든 IP 응답 허용
        configuration.addAllowedHeader("*");        //모든 header 응답 허용
        configuration.addAllowedMethod("*");        //모든 HTTP Method 요청 허용
        source.registerCorsConfiguration("/api/**", configuration);

        return new CorsFilter(source);
    }

}
