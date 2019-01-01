package com.xiangxing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.ex.MessagePoMapper;
import com.xiangxing.model.ex.MessagePo;
import com.xiangxing.utils.DateUtil;

@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {
	@Autowired
	private MessagePoMapper messagePoMapper;

	@RequestMapping("/getStudentMessageInfo")
	@ResponseBody
	public PageResponse<MessagePo> getStudentMessageInfo(PageRequest pageRequest, Long studentId) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		List<MessagePo> messagePos = messagePoMapper.getStudentMessageInfo(studentId);
		for (MessagePo messagePo : messagePos) {
			messagePo.setShowCreateTime(DateUtil.dateToString(messagePo.getCreateTime(), DateUtil.patternD));
		}
		long total = page.getTotal();
		return new PageResponse<MessagePo>(total, messagePos);

	}

}
