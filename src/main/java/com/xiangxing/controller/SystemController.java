package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.mapper.MenuMapper;
import com.xiangxing.mapper.UserMapper;
import com.xiangxing.mapper.UserMenuMapper;
import com.xiangxing.model.Menu;
import com.xiangxing.model.MenuExample;
import com.xiangxing.model.School;
import com.xiangxing.model.User;
import com.xiangxing.model.UserExample;
import com.xiangxing.model.UserMenu;
import com.xiangxing.model.UserMenuExample;
import com.xiangxing.vo.MenuVo;
import com.xiangxing.vo.MyMenuVo;

@Controller
@RequestMapping("/system")
public class SystemController {

	@RequestMapping("/user")
	public String user(Model model) {
		List<School> schools = new ArrayList();
		School school = new School();
		school.setSchoolId("1");
		school.setName("超级大学");
		schools.add(school);
		model.addAttribute("schools", schools);
		model.addAttribute("defaultValue", school.getSchoolId());
		return "user";
	}

	@Autowired
	UserMapper userMapper;
	@Autowired
	MenuMapper menuMapper;
	@Autowired
	UserMenuMapper userMenuMapper;

	@RequestMapping("/userList")
	@ResponseBody
	public PageResponse<User> userList(PageRequest pageRequest, String name, Model model) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		UserExample UserExample = new UserExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			UserExample.createCriteria().andNameLike(name);

		}

		List<User> rows = userMapper.selectByExample(UserExample);
		long total = page.getTotal();
		return new PageResponse<User>(total, rows);
	}

	@RequestMapping("/addUser")
	@ResponseBody
	public String addUser(User user, String menus) {
		user.setPassword(
				new SimpleHash( "MD5",user.getPassword(), user.getName() + "xiaochendaiwolaiqiaji", 1).toString());
		userMapper.insert(user);
		for (String menu : menus.split(",")) {
			UserMenu userMenu = new UserMenu();
			userMenu.setUserId(user.getId());
			userMenu.setMenuId(Long.valueOf(menu));
			userMenuMapper.insert(userMenu);
		}

		return "";
	}

	@RequestMapping("/menu")
	public String menu() {

		return "menu";
	}

	@RequestMapping("/menuList")
	@ResponseBody
	public Set<MenuVo> menuList() {
		MenuExample example = new MenuExample();
		example.createCriteria().andIdGreaterThan(0L);
		// example.setOrderByClause("id ASC");
		List<Menu> rows = menuMapper.selectByExample(example);
		Map<Long, MenuVo> menuMap = new HashMap();
		for (Menu menu : rows) {
			if (menu.getParentId() == null) {
				MenuVo menuVo = new MenuVo();
				menuVo.setId(menu.getId());
				menuVo.setName(menu.getName());
				menuVo.setNo(menu.getNo());
				menuMap.put(menu.getId(), menuVo);
			} else {
				MenuVo parent = menuMap.get(menu.getParentId());
				MenuVo menuVo = new MenuVo();
				menuVo.setId(menu.getId());
				menuVo.setName(menu.getName());
				menuVo.setNo(menu.getNo());
				menuVo.setUrl(menu.getUrl());
				parent.getChildren().add(menuVo);
			}
		}
		Set menus = new TreeSet(menuMap.values());
		return menus;
	}

	@RequestMapping("/showMenu")
	@ResponseBody
	public Set<MenuVo> showMenu() {
		User me = (User) SecurityUtils.getSubject().getPrincipal();

		UserMenuExample example = new UserMenuExample();
		example.createCriteria().andUserIdEqualTo(me.getId());
		List<UserMenu> rows = userMenuMapper.selectByExample(example);
		List<Menu> menus = new ArrayList();
		Map<Long, MyMenuVo> menuMap = new HashMap();
		for (UserMenu userMenu : rows) {
			Menu menu = menuMapper.selectByPrimaryKey(userMenu.getMenuId());
			MyMenuVo menuVo = new MyMenuVo();
			menuVo.setId(menu.getId());
			menuVo.setText(menu.getName());
			menuVo.setNo(menu.getNo());
			menuVo.getAttributes().put("url", menu.getUrl());
			MyMenuVo parent;
			if (menuMap.containsKey(menu.getParentId())) {
				parent = menuMap.get(menu.getParentId());
			} else {
				parent = new MyMenuVo();
				Menu parentMenu = menuMapper.selectByPrimaryKey(menu.getParentId());
				parent.setId(parentMenu.getId());
				parent.setText(parentMenu.getName());
				parent.setNo(parentMenu.getNo());
				menuMap.put(menu.getParentId(), parent);
			}
			parent.getChildren().add(menuVo);
		}
		return new TreeSet(menuMap.values());
	}

	@RequestMapping("/myMenu")
	@ResponseBody
	public Set<MenuVo> myMenu() {
		MenuExample example = new MenuExample();
		// 为超级还是普通 if

		example.createCriteria().andTypeEqualTo(1);
		example.setOrderByClause("id ASC");
		List<Menu> rows = menuMapper.selectByExample(example);
		Map<Long, MyMenuVo> menuMap = new HashMap();
		for (Menu menu : rows) {
			if (menu.getParentId() == null) {
				MyMenuVo menuVo = new MyMenuVo();
				menuVo.setId(menu.getId());
				menuVo.setText(menu.getName());
				menuVo.setNo(menu.getNo());
				menuMap.put(menu.getId(), menuVo);
			} else {
				MyMenuVo parent = menuMap.get(menu.getParentId());
				MyMenuVo menuVo = new MyMenuVo();
				menuVo.setId(menu.getId());
				menuVo.setText(menu.getName());
				menuVo.setNo(menu.getNo());
				parent.getChildren().add(menuVo);
			}
		}
		Set menus = new TreeSet(menuMap.values());

		return menus;
	}

}
