package com.xiangxing.controller.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.ProductMapper;
import com.xiangxing.mapper.StudentCourseMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.Product;
import com.xiangxing.model.StudentCourseExample;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.ProductVo;

@RequestMapping("/api/teacher")
@RestController
public class ApiTeacherController {

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private StudentCourseMapper studentCourseMapper;

	@RequestMapping("/myStudents")
	public ApiResponse myStudents() {
		LoginInfo info = TokenManager.getNowUser();

		return null;

	}

	@RequestMapping("/uploadProduct")
	public ApiResponse uploadProduct(ProductVo productVo) {
		LoginInfo info = TokenManager.getNowUser();
		StudentCourseExample example = new StudentCourseExample();
		example.createCriteria().andCourseIdEqualTo(productVo.getCourseId())
				.andStudentIdEqualTo(productVo.getStudentId());
		Long studentCourseId = studentCourseMapper.selectByExample(example).get(0).getId();
		Product product = new Product();
		try {
			BeanUtils.copyProperties(product, productVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		product.setStudentCourseId(studentCourseId);
		byte[] imageByte = Base64Utils.decodeFromString(productVo.getBase64Image());
		BufferedImage image;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("/data/xiangxing/app.jpg");
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			int srcWidth = image.getWidth(); // 源图宽度
			int srcHeight = image.getHeight(); // 源图高度
			product.setLength(srcHeight);
			product.setWidth(srcWidth);
			ImageIO.write(image, "jpg", fileOutputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		productMapper.insert(product);
		return new ApiResponse();

	}

}
