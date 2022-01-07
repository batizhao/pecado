package me.baitzhao.gateway;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020/3/20
 */
@SpringBootApplication
public class PecadoGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoGatewayApplication.class, args);
    }

    @Autowired
    RouteDefinitionLocator locator;

    /**
     * Swagger Api
     * @return
     */
    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*m")).forEach(routeDefinition -> {
            String name = "api";
            GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
        });
        return groups;
    }

}
