package me.batizhao.ims.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.ims.api.dto.TreeNode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "菜单类")
public class Menu extends TreeNode implements Serializable {

    @ApiModelProperty(value = "路径", example = "/user/common")
    @NotBlank(message = "path is not blank")
    private String path;

    @ApiModelProperty(value = "菜单名", example = "权限管理")
    @NotBlank(message = "name is not blank")
    private String name;

    @ApiModelProperty(value = "权限名", example = "ims_root")
    @NotBlank(message = "permission is not blank")
    private String permission;

    @ApiModelProperty(value = "权限说明", example = "This is admin permission")
    private String description;

    @ApiModelProperty(value = "图标", example = "icon-web")
    private String icon;

    @ApiModelProperty(value = "类型（0菜单 1按钮）", example = "0")
    private String type;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;
}
