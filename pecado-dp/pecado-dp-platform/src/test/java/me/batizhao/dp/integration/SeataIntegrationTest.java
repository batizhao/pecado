package me.batizhao.dp.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author batizhao
 * @since 2020-10-26
 */
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = PecadoDevPlatformApplication.class)
//@ImportAutoConfiguration(RocketMQAutoConfiguration.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("integration")
//@Tag("integration")
//@Slf4j
//@DirtiesContext
public class SeataIntegrationTest {

//    @Value("${pecado.token.admin}")
//    String adminAccessToken;
//
//    @Autowired
//    MockMvc mvc;
//
//    /**
//     * 无分布式事务框架，远程事务全部成功
//     * @throws Exception
//     */
//    @Test
//    public void givenLog_whenNoTransaction_thenSuccess() throws Exception {
//        mvc.perform(get("/no/success")
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data").value(true));
//    }
//
//    /**
//     * 无分布式事务框架，出现异常，远程事务部分成功
//     * @throws Exception
//     */
//    @Test
//    public void givenLog_whenNoTransaction_ThenFail() throws Exception {
//        mvc.perform(get("/no/fail")
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().is5xxServerError())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
//                .andExpect(jsonPath("$.data").value("handleNoTransactionThenFail"));
//    }
//
//    /**
//     * 有分布式事务框架，远程事务全部成功
//     * @throws Exception
//     */
//    @Test
//    public void givenLog_whenTransaction_ThenSuccess() throws Exception {
//        mvc.perform(get("/seata/commit")
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data").value(true));
//    }
//
//    /**
//     * 有分布式事务框架，出现异常，远程事务全部回滚
//     * @throws Exception
//     */
//    @Test
//    public void givenLog_whenSeataTransaction_ThenFail() throws Exception {
//        mvc.perform(get("/seata/rollback")
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().is5xxServerError())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
//                .andExpect(jsonPath("$.data").value("handleSeataTransactionThenFail"));
//    }
}
