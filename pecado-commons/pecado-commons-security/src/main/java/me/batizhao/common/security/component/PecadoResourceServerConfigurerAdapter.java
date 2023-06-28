package me.batizhao.common.security.component;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.handler.MyAccessDeniedHandler;
import me.batizhao.common.security.handler.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPublicKey;

/**
 * @author batizhao
 * @since 2020-03-20
 */
@Slf4j
@EnableWebSecurity
public class PecadoResourceServerConfigurerAdapter {

	@Autowired
	private MyAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private MyAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private PermitAllUrlProperties permitAllUrl;
	@Autowired
	private JwtAuthenticationTokenFilter authenticationTokenFilter;

	@Value("${pecado.jwt.public-key}")
	RSAPublicKey key;

	/**
	 * 配置不需要鉴权的接口
	 *
	 * @param http
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>
				.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

		permitAllUrl.getAnt().getUrls()
				.forEach(url -> registry.antMatchers(url).permitAll());

		permitAllUrl.getRegex().getUrls()
			.forEach(url -> registry.regexMatchers(url).permitAll());

		return registry
			.anyRequest().authenticated()
			.and()
			.csrf().disable()
//			.oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exceptions -> exceptions
					.authenticationEntryPoint(authenticationEntryPoint)
					.accessDeniedHandler(accessDeniedHandler)
			).addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	/**
	 * 从 JWT 的 scope 中获取的权限 取消 SCOPE_ 的前缀
	 * 设置从 jwt claim 中哪个字段获取权限
	 * 如果需要同多个字段中获取权限或者是通过url请求获取的权限，则需要自己提供jwtAuthenticationConverter()这个方法的实现
	 *
	 * @return JwtAuthenticationConverter
	 */
//	private JwtAuthenticationConverter jwtAuthenticationConverter() {
//		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
//		// 去掉 SCOPE_ 的前缀
//		authoritiesConverter.setAuthorityPrefix("");
//		// 从jwt claim 中那个字段获取权限，模式是从 scope 或 scp 字段中获取
//		authoritiesConverter.setAuthoritiesClaimName(SecurityConstants.DETAILS_AUTHORITIES);
//		converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
//		return converter;
//	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

}
