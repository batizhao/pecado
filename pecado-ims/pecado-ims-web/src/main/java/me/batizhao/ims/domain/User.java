package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", example = "100")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名", example = "zhangsan")
    @NotBlank(message = "username is not blank")
    @Size(min = 3, max = 30)
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "password is not blank")
    @JsonIgnore
    private String password = "123456";

    @ApiModelProperty(value = "邮箱", example = "zhangsan@qq.com")
    @NotBlank(message = "email is not blank")
    @Email
    private String email;

    /**
     * @mock @cname
     */
    @ApiModelProperty(value = "姓名", example = "张三")
    @NotBlank(message = "name is not blank")
    private String name;

    /**
     * @mock @url
     */
    @ApiModelProperty(value = "用户头像", example = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png")
    private String avatar;

    @ApiModelProperty(value = "未读消息数量", example = "99")
    private Integer unreadCount;

    @ApiModelProperty(value = "是否锁定", example = "0 or 1")
    private Integer locked;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}
