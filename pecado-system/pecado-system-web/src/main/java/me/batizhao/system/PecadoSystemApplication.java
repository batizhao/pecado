package me.batizhao.system;

import me.batizhao.common.feign.annotation.EnablePecadoFeignClients;
import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author batizhao
 * @since 2020/3/28
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnablePecadoResourceServer
@EnablePecadoFeignClients
public class PecadoSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoSystemApplication.class, args);
    }

}
