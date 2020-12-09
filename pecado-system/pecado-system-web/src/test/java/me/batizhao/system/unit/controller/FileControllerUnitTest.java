package me.batizhao.system.unit.controller;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.system.domain.File;
import me.batizhao.system.service.FileService;
import me.batizhao.system.controller.FileController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @date 2020/9/28
 */
@WebMvcTest(FileController.class)
public class FileControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;

    @Test
    @WithMockUser
    public void givenFile_whenUpload_thenSuccess() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "test data".getBytes());

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/file/upload").file(mockMultipartFile);

        File file = new File().setFileName("hexFileName").setName("filename")
                .setSize(100L).setUrl("xxx/test2.txt")
                .setCreatedTime(LocalDateTime.now());

        when(fileService.upload(any(MockMultipartFile.class))).thenReturn(file);

        mvc.perform(builder.with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name", equalTo("filename")));

        verify(fileService).upload(any(MockMultipartFile.class));
    }

    @Test
    @WithMockUser
    public void givenImageFileName_whenLoadResource_thenSuccess() throws Exception {
        Resource file = new ClassPathResource("test.jpg");
        when(fileService.loadAsResource(anyString())).thenReturn(file);

        mvc.perform(get("/file/image/test.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG_VALUE));

        verify(fileService).loadAsResource(anyString());
    }

    @Test
    @WithMockUser
    public void givenPngFileName_whenLoadResource_thenFail() throws Exception {
        Resource file = new ClassPathResource("test.png");
        when(fileService.loadAsResource(anyString())).thenReturn(file);

        mvc.perform(get("/file/image/test.png"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("class path resource [test.png] cannot be resolved")));
    }

    @Test
    @WithMockUser
    public void givenTxtFileName_whenLoadResource_thenFail() throws Exception {
        Resource file = new ClassPathResource("test.txt");
        when(fileService.loadAsResource(anyString())).thenReturn(file);

        mvc.perform(get("/file/image/test.txt"))
                .andExpect(status().isNotFound());
    }

}
