package me.batizhao.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.system.domain.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author batizhao
 * @date 2020/9/23
 */
public interface FileService extends IService<File> {

//    Boolean save(File file);
    Resource loadAsResource(String filename);

    File uploadAndSave(MultipartFile file);
}
