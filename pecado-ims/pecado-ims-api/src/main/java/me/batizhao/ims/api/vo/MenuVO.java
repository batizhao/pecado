package me.batizhao.ims.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.ims.api.dto.TreeNode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 构建菜单和路由数据
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@ApiModel(description = "菜单")
public class MenuVO extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "类型（L左侧 T顶部 B按钮）", example = "0")
    private String type;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

    public MenuVO() {
    }

    public MenuVO(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public MenuVO(MenuVO menuVO) {
        this.id = menuVO.getId();
        this.pid = menuVO.getPid();
        this.icon = menuVO.getIcon();
        this.name = menuVO.getName();
        this.path = menuVO.getPath();
        this.type = menuVO.getType();
        this.permission = menuVO.getPermission();
        this.sort = menuVO.getSort();
    }
}
