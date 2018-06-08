package com.xiangxing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.service.SchoolService;

@Service
public class SchoolServiceImpl implements SchoolService {
	@Autowired
	private SchoolMapper schoolMapper;

	@Override
	public void addSchool(School school) {
		schoolMapper.insertSelective(school);
	}

	@Override
	public List<School> searchSchools(School school) {
		SchoolExample example = new SchoolExample();
		example.createCriteria().andIdIsNotNull();
		return schoolMapper.selectByExample(example);
	}

	@Override
	public void editSchool(School school, int schoolId) {
		school.setId(schoolId);
		schoolMapper.updateByPrimaryKeySelective(school);
	}

	@Override
	public void destroySchool(int schoolId) {
		schoolMapper.deleteByPrimaryKey(schoolId);
	}

}
