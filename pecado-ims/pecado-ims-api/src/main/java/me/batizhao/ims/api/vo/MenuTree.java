package me.batizhao.ims.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import me.batizhao.ims.api.dto.TreeNode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 构建 Antd 菜单树形结构
 * @author batizhao
 * @date 2020/8/11
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "菜单树")
public class MenuTree extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单名", example = "权限管理")
    @NotBlank(message = "title is not blank")
    private String title;

    @ApiModelProperty(value = "权限名", example = "ims_root")
    @NotBlank(message = "key is not blank")
    private String key;
}
