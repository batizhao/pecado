package me.batizhao.common.security.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.exception.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author batizhao
 * @since 2020-03-20
 */
@Slf4j
public class PecadoResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
	@Autowired
	protected MyAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	@Autowired
	private PermitAllUrlProperties permitAllUrl;

	/**
	 * 配置不需要鉴权的接口
	 *
	 * @param httpSecurity
	 */
	@Override
	@SneakyThrows
	public void configure(HttpSecurity httpSecurity) {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>
			.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

		permitAllUrl.getAnt().getUrls()
				.forEach(url -> registry.antMatchers(url).permitAll());

		permitAllUrl.getRegex().getUrls()
			.forEach(url -> registry.regexMatchers(url).permitAll());

		registry.anyRequest().authenticated().and().csrf().disable();

	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		UserAuthenticationConverter userTokenConverter = new PecadoUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);

		PecadoTokenServices tokenServices = new PecadoTokenServices();

		// 这里的签名key 保持和认证中心一致
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("123");
		converter.setVerifier(new MacSigner("123"));
		JwtTokenStore jwtTokenStore = new JwtTokenStore(converter);
		tokenServices.setTokenStore(jwtTokenStore);
		tokenServices.setJwtAccessTokenConverter(converter);
		tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);

		resources.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler).tokenServices( tokenServices );
	}

}
