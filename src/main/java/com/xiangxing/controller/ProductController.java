package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.mapper.ProductMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.mapper.ex.ProductPoMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.Product;
import com.xiangxing.model.ProductExample;
import com.xiangxing.model.ProductExample.Criteria;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.ProductPo;
import com.xiangxing.utils.MD5Util;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ProductPoMapper productPoMapper;

	@Autowired
	private CourseMapperEx courseMapperEx;

	@RequestMapping
	public String product(Model model) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<CourseEx> courses = new ArrayList<>();
		if (me.getType() == 1) {
			courses = courseMapperEx.courseList(null, me.getSchoolId(), null);

		}
		model.addAttribute("courses", courses);
		return "product";
	}

	@RequestMapping("/saveProduct")
	public void saveProduct(Product product) {
		productMapper.insertSelective(product);
		writeToOkResponse();
	}

	@RequestMapping("/productList")
	@ResponseBody
	public PageResponse<ProductPo> productList(PageRequest pageRequest, String name, String courseId,Long studentId,HttpServletRequest httpServletRequest) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		Long schoolId = null;
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		if (me.getType() == 1) {
			schoolId = me.getSchoolId();
		}
		List<ProductPo> products;
		if(null!=studentId && schoolId!=0){
			 products = productPoMapper.getListByStudentId(studentId);
		}
		else
		products = productPoMapper.list(name, courseId, schoolId);
		for (ProductPo product : products) {
			product.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + product.getPath());
		}
		long total = page.getTotal();
		return new PageResponse<ProductPo>(total, products);

	}

	@RequestMapping("/editProduct")
	public void editproduct(Product product, Long productId) {
		product.setId(productId);
		productMapper.updateByPrimaryKeySelective(product);
		writeToOkResponse();
	}

	@RequestMapping("/destroyProduct")
	public void destroyproduct(Long productId) {
		productMapper.deleteByPrimaryKey(productId);
		writeToOkResponse();
	}

}
