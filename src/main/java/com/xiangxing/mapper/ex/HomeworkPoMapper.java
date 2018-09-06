package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.HomeworkPo;
import com.xiangxing.model.ex.StudentHomeworkPo;

public interface HomeworkPoMapper {

	List<HomeworkPo> list(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("status") Integer status);

	List<HomeworkPo> getTeacherHomeWork(@Param("teacherId") Long teacherId, @Param("courseId") Long courseId);

	List<StudentHomeworkPo> getStudentHomework(@Param("homeworkId") Long homeworkId);

}
