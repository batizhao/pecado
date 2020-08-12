package me.batizhao.ims.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "角色")
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID", example = "100")
    private Long id;

    @ApiModelProperty(value = "名称", example = "管理员")
    @NotBlank(message = "name is not blank")
    @Size(min = 3, max = 30)
    private String name;

    /**
     * @mock ROLE_@string("upper", 3, 20)
     */
    @ApiModelProperty(value = "代码", example = "ROLE_USER")
    @NotBlank(message = "code is not blank")
    @Size(min = 3, max = 30)
    private String code;

    @ApiModelProperty(value = "说明", example = "This is admin")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

}
