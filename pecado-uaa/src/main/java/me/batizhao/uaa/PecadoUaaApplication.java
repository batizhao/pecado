package me.batizhao.uaa;

import me.batizhao.common.feign.annotation.EnablePecadoFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnablePecadoFeignClients
public class PecadoUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoUaaApplication.class, args);
    }

}
