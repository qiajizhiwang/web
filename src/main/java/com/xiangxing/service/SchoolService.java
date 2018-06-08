package com.xiangxing.service;

import java.util.List;

import com.xiangxing.model.School;

public interface SchoolService {
	public void addSchool(School school);

	public List<School> searchSchools(School school);

	public void editSchool(School school, int schoolId);

	public void destroySchool(int schoolId);

}
