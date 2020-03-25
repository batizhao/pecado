package me.batizhao.log;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnablePecadoResourceServer
@EnableFeignClients(basePackages = "me.batizhao.ims.api.feign")
public class PecadoLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoLogApplication.class, args);
    }

}
