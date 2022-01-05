package learning.security.config.oauth.provider;

public interface OAuth2UserInfo {
    String getName();
    String getEmail();
    String getProvider();
    String getProviderId();
}
