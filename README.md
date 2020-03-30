# Pecado  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这个项目从 [paper](https://github.com/batizhao/paper) 演进而来。

## 微服务自动化测试

###单元测试

目标：每个类中的方法要保证自己的部分是符合预期行为的（正常的、异常的）

面向：Java 类中的方法（DAO、Service、Controller）

特点：方法自hi，每个测试都可以单独运行

方法：Mock 相关依赖方法，构造预期行为，调用比对。

工具：[Hamcrest](http://hamcrest.org/JavaHamcrest/tutorial)、Mockito

启动：`mvn clean test`

```java
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public abstract class BaseServiceUnitTest {
}

@Slf4j
public class UserServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceIml();
        }
    }

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private List<User> userList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        userList = new ArrayList<>();
        userList.add(new User().setId(1L).setEmail("zhangsan@gmail.com").setUsername("zhangsan").setName("张三").setPassword("123456"));
        userList.add(new User().setId(2L).setEmail("lisi@gmail.com").setUsername("lisi").setName("李四").setPassword("123456"));
        userList.add(new User().setId(3L).setEmail("wangwu@gmail.com").setUsername("wangwu").setName("王五").setPassword("123456"));
    }

    @Test
    public void givenUserName_whenFindUser_thenUser() {
        String username = "zhangsan";

        when(userMapper.selectOne(any()))
                .thenReturn(userList.get(0));

        UserVO user = userService.findByUsername(username);

        assertThat(user.getUsername(), equalTo(username));
        assertThat(user.getEmail(), equalTo("zhangsan@gmail.com"));
    }
}
```

### API 测试

目标：微服务内的集成测试（正常的、异常的）

面向：@RestController 中的 API

特点：服务内部自hi，覆盖每个接口。类似之前单例应用中的集成测试，不涉及外部系统。

方法：启动当前微服务 Mock 实例 SpringBootTest.WebEnvironment.MOCK

工具：[Hamcrest](http://hamcrest.org/JavaHamcrest/tutorial)、MockMvc

启动：`mvn clean test -Ptest` 或者 `mvn clean test -Ptest -Dtest-group=api`

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoImsApplication.class)
@AutoConfigureMockMvc
@Import(WebExceptionHandler.class)
@ActiveProfiles("test")
@RefreshScope
public abstract class BaseApiTest {
    /**
     * 使用一个超长时间的 token，隔离获取 token 的操作。避免测试 token 过期！
     */
    @Value("${pecado.token.admin}")
    String adminAccessToken;

    @Autowired
    MockMvc mvc;
}

public class UserApiTest extends BaseApiTest {

    @Test
    public void givenUserName_whenFindUser_thenSuccess() throws Exception {
        mvc.perform(get("/user").param("userId", "1")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("admin@qq.com"))
                .andExpect(jsonPath("$.data.roleList", hasSize(2)));
    }
}
```

### 契约测试

目标：微服务之间接口调用测试，但不需要启动被调用的服务。

面向：@FeignClient @RestTemplate

特点：

* 微服务之间交互的部分。一般服务内部尽量用 Feign 调用，不要直接用 @RestTemplate
* 这里也不需要 Provider Online，而是通过 Stub 启动
* 快速反馈。Provider 对契约的修改会即时通知到 Consumer

方法：

* Provider 编写契约（Groovy\YAML\Java）
* 部署契约到 Git 或者 Maven 仓库
* Provider、Consumer 引用契约进行测试

工具：[Spring Cloud Contract](https://cloud.spring.io/spring-cloud-contract/reference/html/project-features.html#contract-dsl)、[Hamcrest](http://hamcrest.org/JavaHamcrest/tutorial)、[REST Assured](http://rest-assured.io/)

启动：`mvn clean test -Ptest` 或者 `mvn clean test -Ptest -Dtest-group=contract`

声明契约测试基类

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@RefreshScope
public abstract class BaseContractTest {

    @Value("${pecado.token.admin}")
    String adminAccessToken;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp(){
        RestAssuredMockMvc.webAppContextSetup(context);
    }

}
```

声明契约（首选 Groovy 脚本）
```groovy
package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'givenUserId_whenFindRoles_thenSuccess'
    request {
        method GET()
        url('/role') {
            queryParameters {
                parameter('userId', $(consumer(number()), producer(1L)))
            }
            headers {
                header('Authorization', $(consumer(nonBlank()), producer(execute('adminAccessToken'))))
            }
        }
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(file("roleResponse.json"))
    }
}
```

使用 Maven 插件生成 Provider 测试类，然后 install 到仓库

```java
public class ContractVerifierTest extends BaseContractTest {

	@Test
	public void validate_givenUserId_whenFindRoles_thenSuccess() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Authorization", adminAccessToken);

		// when:
			ResponseOptions response = given().spec(request)
					.queryParam("userId","1")
					.get("/role");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['code']").isEqualTo(0);
			assertThatJson(parsedJson).field("['message']").isEqualTo("ok");
			assertThatJson(parsedJson).array("['data']").contains("['id']").isEqualTo(1);
			assertThatJson(parsedJson).array("['data']").contains("['name']").isEqualTo("ROLE_USER");
			assertThatJson(parsedJson).array("['data']").contains("['id']").isEqualTo(2);
			assertThatJson(parsedJson).array("['data']").contains("['name']").isEqualTo("ROLE_ADMIN");
	}
}
```

在 Consumer 编写测试用例（这里通过 @AutoConfigureStubRunner，不需要 Provider Online）

```java
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "me.batizhao:pecado-ims-web:+:stubs:10000")
public class LogControllerContractTest {

    @Autowired
    MockMvc mvc;

    @Value("${pecado.token.admin}")
    String adminAccessToken;

    @Test
    public void givenUserId_whenFindRoles_thenSuccess() throws Exception {
        mvc.perform(get("/log")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }
}
```

### E2E 集成测试

目标：微服务之间接口调用测试。

面向：@FeignClient @RestTemplate，有些场景之前的方法覆盖不到。

特点：代价大，所有微服务、数据库都要 Online

方法：启动所有的微服务。

工具：[Hamcrest](http://hamcrest.org/JavaHamcrest/tutorial)、MockMvc

启动：`mvn clean test -Pintegration` 或者 `mvn clean test -Dtest-group=integration`

### Mock Server 测试（可选）

目标：前端接口调用测试。

面向：前端通过 Gateway 调用后端微服务的场景。

特点：

* 前端实时感知后端接口变化；
* Mock 和实际的接口是一致的；
* 对应 API 测试。

方法：

* 微服务可以启动，也可以不启动（Mock）。

* 使用 IDEA EasyYapi 插件，兼容 Swagger，这样如果没有 YApi 或者 EasyYapi 也可以使用 Swagger UI。

  同时，可以从代码单向同步接口配置到 YApi，好处是：

  - 不至于每次在两边修改后，不好同步。
  - 可以删除 YApi 的接口，只要从代码重新同步。
  - YApi 的配置算是加入了版本控制，因为所有的配置都在代码里。
  - Mock 接口和真实接口的返回数据是一致的。

工具：[YApi](https://github.com/YMFE/yapi)、[EasyYapi](https://easyyapi.com)

### UI 测试

...

