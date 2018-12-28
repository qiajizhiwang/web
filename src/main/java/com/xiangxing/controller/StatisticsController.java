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

import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.ex.FinanceMapper;
import com.xiangxing.model.User;
import com.xiangxing.vo.FinanceVo;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

	private static final Logger logger = LogManager.getLogger(StatisticsController.class);

	@Autowired
	FinanceMapper financeMapper;
	@Autowired
	SchoolMapper schoolMapper;
	
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
		return financeVos;
		
	}

}
