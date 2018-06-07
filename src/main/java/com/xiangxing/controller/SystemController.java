package com.xiangxing.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.mapper.SysUserMapper;
import com.xiangxing.model.SysUser;
import com.xiangxing.model.SysUserExample;

@Controller
@RequestMapping("/system")
public class SystemController {

	@RequestMapping("/user")
	public String user() {
		return "user";
	}

	@Autowired
	SysUserMapper sysUserMapper;

	@RequestMapping("/userList")
	@ResponseBody
	public PageResponse<SysUser> userList(PageRequest pageRequest, String name) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		SysUserExample sysUserExample = new SysUserExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			sysUserExample.createCriteria().andLoginNameLike(name);

		}

		List<SysUser> rows = sysUserMapper.selectByExample(sysUserExample);
		long total = page.getTotal();
		return new PageResponse<SysUser>(total, rows);
	}

}
