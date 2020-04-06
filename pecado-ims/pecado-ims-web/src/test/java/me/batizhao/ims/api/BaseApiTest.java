package me.batizhao.ims.api;

import lombok.SneakyThrows;
import me.batizhao.common.core.exception.WebExceptionHandler;
import me.batizhao.ims.PecadoImsApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 在集成测试中，不再需要 Mock Bean 和 Stub，
 * 但是需要实例化整个上下文，再对返回数据进行判断
 * 参数校验相关的测试可以放在集成测试中，因为不需要 Stub Data
 * Import WebExceptionHandler 是因为 mvn test 不能加载应用外的 resource
 *
 * @author batizhao
 * @since 2020-02-07
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoImsApplication.class)
@AutoConfigureMockMvc
@Import(WebExceptionHandler.class)
@ActiveProfiles("test")
@Tag("api")
public abstract class BaseApiTest {

    /**
     * 使用一个超长时间的 token，隔离获取 token 的操作。避免测试 token 过期！
     * curl -X POST --user 'test:passw0rd' -d 'grant_type=password&username=admin&password=123456' http://localhost:4000/oauth/token
     * curl -X POST --user 'test:passw0rd' -d 'grant_type=password&username=tom&password=123456' http://localhost:4000/oauth/token
     */
    @Value("${pecado.token.admin}")
    String adminAccessToken;
    @Value("${pecado.token.user}")
    String userAccessToken;

    @Autowired
    MockMvc mvc;

    /**
     * 阻止测试主线程提前退出，导致 @SystemLog 异步线程失败
     * 这个可以放在集成测试中
     */
    @SneakyThrows
    @AfterEach
    public void sleep() {
        Thread.sleep(10000L);
    }
}
