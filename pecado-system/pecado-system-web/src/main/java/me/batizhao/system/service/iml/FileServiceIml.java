package me.batizhao.system.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.StorageException;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.system.config.FileUploadProperties;
import me.batizhao.system.domain.File;
import me.batizhao.system.mapper.FileMapper;
import me.batizhao.system.service.FileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Service
public class FileServiceIml extends ServiceImpl<FileMapper, File> implements FileService {

    private final Path rootLocation;

    @Autowired
    public FileServiceIml(FileUploadProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public File upload(MultipartFile file) {
        if (file == null) {
            throw new StorageException("Failed to store empty file.");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String hexFileName = fileNameEncode(filename);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException("Cannot store file with relative path outside current directory " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path target = this.rootLocation.resolve(pathEncode(hexFileName));
                Files.createDirectories(target.getParent());
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        //只返回文件名给前端，不包括路径
        return new File().setFileName(hexFileName).setName(filename)
                .setSize(file.getSize()).setUrl(this.rootLocation.toString())
                .setCreatedTime(LocalDateTime.now());
    }

    @Override
    public File uploadAndSave(MultipartFile file) {
        File f = upload(file);
        save(f);
        return f;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(pathEncode(filename));
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    /**
     * 对文件名进行 md5
     * @param fileName 文件名
     * @return
     */
    private String fileNameEncode(String fileName) {
        String username = SecurityUtils.getUser().getUsername();
        return DigestUtils.md5Hex(username + fileName) + fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 根据 fileNameEncode 生成的文件名，生成 hash 目录结构
     * @param hexFileName hash 以后的文件名
     * @return
     */
    private String pathEncode(String hexFileName) {
        String secondMD5 = DigestUtils.md5Hex(hexFileName);
        String p1 = secondMD5.substring(0, 2);
        String p2 = secondMD5.substring(2, 4);

        StringBuilder sb = new StringBuilder();
        sb.append(p1)
            .append("/").append(p2)
            .append("/").append(hexFileName);
        return sb.toString();
    }
}
