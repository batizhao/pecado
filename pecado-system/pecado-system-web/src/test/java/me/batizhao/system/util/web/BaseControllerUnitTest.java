package me.batizhao.system.util.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author batizhao
 * @since 2020-04-02
 **/
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Tag("unit")
public abstract class BaseControllerUnitTest {

    /**
     * 控制扫描范围，否则会加载 Security Config，导致 UserDetailsService 实例化
     */
    @SpringBootApplication(scanBasePackages = {"me.batizhao.system.web"})
    static class InnerConfig {}

}