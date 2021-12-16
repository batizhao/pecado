package me.batizhao.uaa.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2021/12/14
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户")
public class TokenVO {

    /**
     * access token
     */
    private String token;

    /**
     * time offset
     */
    private long expire = 3600L;

}
