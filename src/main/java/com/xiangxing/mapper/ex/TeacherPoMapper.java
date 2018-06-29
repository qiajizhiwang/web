package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.Student;
import com.xiangxing.model.ex.ProductPo;

public interface TeacherPoMapper {

	List<Student> myStudents(@Param("teacherId") long teacherId, @Param("courseId") Long courseId);

	List<ProductPo> studentProducts(@Param("teacherId") long teacherId, @Param("courseId") Long courseId, @Param("studentId") Long studentId);

}
