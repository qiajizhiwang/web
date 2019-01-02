package com.xiangxing.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangxing.mapper.HeadquartersMapper;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.ex.FinanceMapper;
import com.xiangxing.mapper.ex.TeacherPeriodPoMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.TeacherPeriodPo;
import com.xiangxing.vo.FinanceVo;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

	private static final Logger logger = LogManager.getLogger(StatisticsController.class);

	@Autowired
	FinanceMapper financeMapper;
	@Autowired
	TeacherPeriodPoMapper teacherPeriodPoMapper;
	@Autowired
	SchoolMapper schoolMapper;
	
	@Autowired
	HeadquartersMapper headquartersMapper;
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	dateFormat.setLenient(false);  
	binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	@RequestMapping("/financeReport")
	public String financeReport(Model model) {
		return "financeReport";
	}

	@RequestMapping("/periodData")
	public String periodData(Model model) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType()==1) {
//			教师课时
			return "teacherPeriod";
		}else if (me.getType()==2) {
//			校区课时
			return "schoolPeriod";
		}
		return null;
	}
	@RequestMapping("/financeList")
	@ResponseBody
	public List<FinanceVo> financeList(Date startDate, Date endDate) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<FinanceVo> financeVos = new ArrayList<FinanceVo>();
		if(null!=endDate)
			endDate = DateUtils.addDays(endDate, 1);
		if (me.getType() == 1) {
			FinanceVo financeVo =new FinanceVo();
			Long schoolId = me.getSchoolId();
			long newF= financeMapper.sumAllPeriodBySchool(startDate,endDate , schoolId, 1);
			long reF= financeMapper.sumAllPeriodBySchool(startDate,endDate , schoolId, 2);
			long exdF= financeMapper.sumAllPeriodBySchool(startDate,endDate , schoolId, 3);
			long usedF= financeMapper.sumUsedPeriodBySchool(startDate,endDate , schoolId, null);
			financeVo.setAllFinance(newF+reF+exdF);
			financeVo.setNewFinance(newF+exdF);
			financeVo.setReFinance(reF);
			financeVo.setUsedFinance(usedF);
			financeVo.setRemainFinance(newF+reF+exdF-usedF);
			financeVo.setSchoolName(schoolMapper.selectByPrimaryKey(schoolId).getName());
			financeVos.add(financeVo);
		}
		if (me.getType() == 2) {
			SchoolExample schoolExample = new SchoolExample();
			schoolExample.createCriteria().andHeadquartersIdEqualTo(me.getHeadquartersId());
			List<School> schools = schoolMapper.selectByExample(schoolExample); 
			for(School school :schools){
				FinanceVo financeVo =new FinanceVo();
			Long schoolId = school.getId();
			long newF= financeMapper.sumAllPeriodBySchool(startDate,endDate , schoolId, 1);
			long reF= financeMapper.sumAllPeriodBySchool(startDate,endDate , schoolId, 2);
			long exdF= financeMapper.sumAllPeriodBySchool(startDate,endDate , schoolId, 3);
			long usedF= financeMapper.sumUsedPeriodBySchool(startDate,endDate , schoolId, null);
			financeVo.setAllFinance(newF+reF+exdF);
			financeVo.setNewFinance(newF+exdF);
			financeVo.setReFinance(reF);
			financeVo.setUsedFinance(usedF);
			financeVo.setRemainFinance(newF+reF+exdF-usedF);
			financeVo.setSchoolName(schoolMapper.selectByPrimaryKey(schoolId).getName());
			financeVos.add(financeVo);
			}
		}
		return financeVos;
		
	}
	
	@RequestMapping("/financeAllList")
	@ResponseBody
	public List<FinanceVo> financeAllList(Date startDate, Date endDate) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<FinanceVo> financeVos = new ArrayList<FinanceVo>();
		if(null!=endDate)
			endDate = DateUtils.addDays(endDate, 1);
		if (me.getType() == 2) {
			FinanceVo financeVo =new FinanceVo();
			Long headquartersId = me.getHeadquartersId();
			long newF= financeMapper.sumAllPeriodByHeadquarters(startDate,endDate , headquartersId, 1);
			long reF= financeMapper.sumAllPeriodByHeadquarters(startDate,endDate , headquartersId, 2);
			long exdF= financeMapper.sumAllPeriodByHeadquarters(startDate,endDate , headquartersId, 3);
			long usedF= financeMapper.sumUsedPeriodByHeadquarters(startDate,endDate , headquartersId, null);
			financeVo.setAllFinance(newF+reF+exdF);
			financeVo.setNewFinance(newF+exdF);
			financeVo.setReFinance(reF);
			financeVo.setUsedFinance(usedF);
			financeVo.setRemainFinance(newF+reF+exdF-usedF);
			financeVo.setSchoolName(headquartersMapper.selectByPrimaryKey(headquartersId).getName());
			financeVos.add(financeVo);
		}
		return financeVos;
		
	}

	@RequestMapping("/teacherPeriodData")
	@ResponseBody
	public List<TeacherPeriodPo> teacherPeriodData(Date startDate, Date endDate) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<TeacherPeriodPo> teacherPeriodPos =new ArrayList<>();
		if(null!=startDate&&null!=endDate)
			teacherPeriodPos = teacherPeriodPoMapper.teacherPeriodDataByDate(me.getSchoolId(),startDate,endDate);
		else{
			teacherPeriodPos = teacherPeriodPoMapper.teacherPeriodData(me.getSchoolId());
		}
		return teacherPeriodPos;
		
	}

	@RequestMapping("/schoolPeriodData")
	@ResponseBody
	public List<TeacherPeriodPo> schoolPeriodData(Date startDate, Date endDate) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<TeacherPeriodPo> teacherPeriodPos =new ArrayList<>();
		if(null!=startDate&&null!=endDate)
			teacherPeriodPos = teacherPeriodPoMapper.schoolPeriodDataByDate(me.getHeadquartersId(),startDate,endDate);
		else{
			teacherPeriodPos = teacherPeriodPoMapper.schoolPeriodData(me.getHeadquartersId());
		}
		return teacherPeriodPos;
		
	}

	@RequestMapping("/schoolTotalPeriodData")
	@ResponseBody
	public List<TeacherPeriodPo> schoolTotalPeriodData() {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<TeacherPeriodPo> teacherPeriodPos = teacherPeriodPoMapper.schoolTotalPeriodData(me.getHeadquartersId());
		return teacherPeriodPos;
		
	}

}
