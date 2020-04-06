package me.batizhao.common.security.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-20
 *
 * <p>
 * 不鉴权的接口配置
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "pecado.ignore")
public class PermitAllUrlProperties {

	private Ant ant = new Ant();
	private Regex regex = new Regex();

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
