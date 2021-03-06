package me.batizhao.ims.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author batizhao
 * @since 2020-05-09
 **/
@Data
@Accessors(chain = true)
@ApiModel(description = "用户信息")
public class UserInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户基本信息")
	private UserVO userVO;

	/**
	 * @mock @pick(["common","admin"])
	 */
	@ApiModelProperty(value = "权限清单")
	private String[] permissions;

	/**
	 * @mock @pick(["ADMIN","MEMBER","GUEST"])
	 */
	@ApiModelProperty(value = "角色清单 ")
	private String[] roles;
}
