package me.batizhao.system;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnablePecadoResourceServer
@EnableFeignClients(basePackages = "me.batizhao.ims.api.feign")
public class PecadoSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoSystemApplication.class, args);
    }

}
