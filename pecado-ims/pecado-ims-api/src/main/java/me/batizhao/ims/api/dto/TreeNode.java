/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/vo/TreeNode.java
 * @ Apache-2.0
 */

package me.batizhao.ims.api.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ace
 * @author lengleng
 * @date 2017年11月9日23:33:45
 */
public class TreeNode {

	@ApiModelProperty(value = "ID", example = "100")
	protected int id;

	@ApiModelProperty(value = "父ID", example = "100")
	@NotBlank(message = "pid is not blank")
	protected int pid;

	protected List<TreeNode> children = new ArrayList<>();;

	protected boolean isLeaf = true;

	public void add(TreeNode node) {
		children.add(node);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean leaf) {
		isLeaf = leaf;
	}
}
