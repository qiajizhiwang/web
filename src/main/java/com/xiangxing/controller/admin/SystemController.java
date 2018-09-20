package com.xiangxing.controller.admin;

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
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.UserMapper;
import com.xiangxing.mapper.UserMenuMapper;
import com.xiangxing.mapper.VersionMapper;
import com.xiangxing.model.Menu;
import com.xiangxing.model.MenuExample;
import com.xiangxing.model.User;
import com.xiangxing.model.UserExample;
import com.xiangxing.model.UserExample.Criteria;
import com.xiangxing.model.UserMenu;
import com.xiangxing.model.UserMenuExample;
import com.xiangxing.model.Version;
import com.xiangxing.model.VersionExample;
import com.xiangxing.vo.MenuVo;
import com.xiangxing.vo.MyMenuVo;
import com.xiangxing.vo.api.ApiResponse;

@Controller
@RequestMapping("/system")
public class SystemController {

	@Autowired
	private SchoolMapper schoolMapper;

	@RequestMapping("/user")
	public String user(Model model) {
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
		User me = (User) SecurityUtils.getSubject().getPrincipal();

		Long schoolId = me.getSchoolId();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		UserExample userExample = new UserExample();
		Criteria criteria = userExample.createCriteria();
		criteria.andTypeEqualTo(1);
		if (me.getType() != 0)
			criteria.andSchoolIdEqualTo(schoolId);
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			criteria.andNameLike(name);

		}

		List<User> rows = userMapper.selectByExample(userExample);
		long total = page.getTotal();
		return new PageResponse<User>(total, rows);
	}

	@RequestMapping("/addUser")
	@ResponseBody
	public ApiResponse addUser(User user, String menus) {
		user.setPassword(
				new SimpleHash("MD5", user.getPassword(), user.getName() + "xiaochendaiwolaiqiaji", 1).toString());
		if(user.getSchoolId()==null){
			return  ApiResponse.getErrorResponse("学校必填");
		}
		userMapper.insert(user);
		for (String menu : menus.split(",")) {
			UserMenu userMenu = new UserMenu();
			userMenu.setUserId(user.getId());
			userMenu.setMenuId(Long.valueOf(menu));
			userMenuMapper.insert(userMenu);
		}

		return new ApiResponse();
	}

	@RequestMapping("/destroyUser")
	@ResponseBody
	public ApiResponse destroyUser(Long id) {
	    userMapper.deleteByPrimaryKey(id);
	    UserMenuExample example = new UserMenuExample();
	    example.createCriteria().andUserIdEqualTo(id);
	    userMenuMapper.deleteByExample(example);
		return new ApiResponse();
	}
	
	@RequestMapping("/editUser")
	@ResponseBody
	public ApiResponse editUser(User user, String menus) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			if (!user.getSchoolId().equals(me.getSchoolId())) {
				return ApiResponse.getDenyErrorResponse();
			}
		}
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(
					new SimpleHash("MD5", user.getPassword(), user.getName() + "xiaochendaiwolaiqiaji", 1).toString());
		}else{
			user.setPassword(null);
		}
		userMapper.updateByPrimaryKeySelective(user);

		if (user.getType() == 1) {
			UserMenuExample example = new UserMenuExample();
			example.createCriteria().andUserIdEqualTo(user.getId());
			userMenuMapper.deleteByExample(example);
			if (StringUtils.isNoneBlank(menus)) {
				for (String menu : menus.split(",")) {
					UserMenu userMenu = new UserMenu();
					userMenu.setUserId(user.getId());
					userMenu.setMenuId(Long.valueOf(menu));
					userMenuMapper.insert(userMenu);
				}
			}
		}

		return new ApiResponse();
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

	@RequestMapping("/defaultMenu")
	@ResponseBody
	public Set<MyMenuVo> defaultMenu() {
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

	@RequestMapping("/myMenu")
	@ResponseBody
	public Set<MyMenuVo> myMenu(long userId) {

		Set<MyMenuVo> allMenus = defaultMenu();

		UserMenuExample example = new UserMenuExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Long> ids = new ArrayList<>();
		List<UserMenu> rows = userMenuMapper.selectByExample(example);
		for (UserMenu m : rows) {
			ids.add(m.getMenuId());
		}
		List<Menu> menus = new ArrayList();
		for (MyMenuVo menuVo : allMenus) {
			for (MyMenuVo menuVo1 : menuVo.getChildren()) {
				if (!ids.contains(menuVo1.getId())) {
					menuVo1.setChecked(false);
				}
			}

		}
		return allMenus;
	}
	
	@Autowired
	VersionMapper versionMapper;

	@RequestMapping("/version")
	public String version(Model model) {
		VersionExample example = new VersionExample();
		example.createCriteria().andVersionIsNotNull();
		List<Version> list = versionMapper.selectByExample(example);
		model.addAttribute("version",list.get(0).getVersion());
		return "version";
	}
	
	@RequestMapping("/saveVersion")
	public String saveVersion(String version,Model model) {
		VersionExample example = new VersionExample();
		example.createCriteria().andVersionIsNotNull();
		List<Version> list = versionMapper.selectByExample(example);
		Version version1 = list.get(0);
		version1.setVersion(version);
		versionMapper.updateByExample(version1, example);
		model.addAttribute("version",version);
		return "version";
	}

}
