package me.batizhao.common.security.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.security.handler.MyAccessDeniedHandler;
import me.batizhao.common.security.handler.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.interfaces.RSAPublicKey;

/**
 * @author batizhao
 * @since 2020-03-20
 */
@Slf4j
public class PecadoResourceServerConfigurerAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private MyAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private PermitAllUrlProperties permitAllUrl;

	@Value("${pecado.jwt.public-key}")
	RSAPublicKey key;

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

		registry
			.anyRequest().authenticated()
			.and()
			.csrf().disable()
			.oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exceptions -> exceptions
					.authenticationEntryPoint(authenticationEntryPoint)
					.accessDeniedHandler(accessDeniedHandler)
			);
	}

	/**
	 * 从 JWT 的 scope 中获取的权限 取消 SCOPE_ 的前缀
	 * 设置从 jwt claim 中哪个字段获取权限
	 * 如果需要同多个字段中获取权限或者是通过url请求获取的权限，则需要自己提供jwtAuthenticationConverter()这个方法的实现
	 *
	 * @return JwtAuthenticationConverter
	 */
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		// 去掉 SCOPE_ 的前缀
		authoritiesConverter.setAuthorityPrefix("");
		// 从jwt claim 中那个字段获取权限，模式是从 scope 或 scp 字段中获取
		authoritiesConverter.setAuthoritiesClaimName(SecurityConstants.DETAILS_AUTHORITIES);
		converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
		return converter;
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

}
