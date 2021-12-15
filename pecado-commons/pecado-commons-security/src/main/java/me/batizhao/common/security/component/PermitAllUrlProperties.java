package me.batizhao.common.security.component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.annotation.Inner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-03-20
 *
 * <p>
 * 不鉴权的接口配置
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlProperties implements InitializingBean, ApplicationContextAware {

	private Ant ant = new Ant();
	private Regex regex = new Regex();
	private ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() {
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
			if (method != null) {
				assert info.getPathPatternsCondition() != null;
				Set<String> inner = info.getPathPatternsCondition().getPatternValues();
				for (String url : inner) {
					Set<NameValueExpression<String>> params = info.getParamsCondition().getExpressions();
					if (!params.isEmpty()) {
						StringBuilder sb = new StringBuilder();
						for (NameValueExpression<String> param : params) {
							url = sb.append("\\")
									.append(url)
									.append("\\?")
									.append(param.getName())
									.append("\\=\\w+").toString();
							log.info(url);
							regex.urls.add(url);
						}
					} else {
						ant.urls.add(url);
					}
				}
			}
		});

		log.info("ant urls path: {}", ant.urls);
		log.info("regex urls path: {}", regex.urls);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 支持 ant 表达式
	 */
	@Data
	public static class Ant {

		private List<String> urls = new ArrayList<>();

	}

	/**
	 * 支持 regex 表达式
	 */
	@Data
	public static class Regex {

		private List<String> urls = new ArrayList<>();

	}
}
