package me.batizhao.ims.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "菜单类")
public class Menu implements Serializable {

    @ApiModelProperty(value = "ID", example = "100")
    private Integer id;

    @ApiModelProperty(value = "父ID", example = "100")
    @Min(0)
    private Integer pid;

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

    @ApiModelProperty(value = "类型（L左侧 T顶部 B按钮）", example = "L")
    private String type;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    private String status;

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
}
