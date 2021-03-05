package me.batizhao.dp.initializr;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;
import me.batizhao.dp.initializr.project.ProjectDescriptionCustomizerConfiguration;
import me.batizhao.dp.initializr.support.CacheableDependencyManagementVersionResolver;
import me.batizhao.dp.initializr.support.StartInitializrMetadataUpdateStrategy;
import me.batizhao.dp.initializr.web.HomeController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringBootApplication
@Import(ProjectDescriptionCustomizerConfiguration.class)
@EnableCaching
@EnableAsync
public class PecadoDevInitializrApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoDevInitializrApplication.class, args);
    }

    @Bean
    public HomeController homeController() {
        return new HomeController();
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return (registry) -> {
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
            registry.addErrorPages(new ErrorPage("/error/index.html"));
        };
    }

    @Bean
    public StartInitializrMetadataUpdateStrategy initializrMetadataUpdateStrategy(
            RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        return new StartInitializrMetadataUpdateStrategy(restTemplateBuilder.build(), objectMapper);
    }

    @Bean
    public DependencyManagementVersionResolver dependencyManagementVersionResolver() throws IOException {
        return new CacheableDependencyManagementVersionResolver(DependencyManagementVersionResolver
                .withCacheLocation(Files.createTempDirectory("version-resolver-cache-")));
    }
}
