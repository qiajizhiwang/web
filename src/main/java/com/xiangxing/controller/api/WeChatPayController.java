package com.xiangxing.controller.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.ExamMapper;
import com.xiangxing.mapper.OrderMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.Exam;
import com.xiangxing.model.Order;
import com.xiangxing.model.OrderExample;
import com.xiangxing.model.Student;
import com.xiangxing.model.ex.EntryFormPo;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.PayResponse;

@RestController
@RequestMapping("/api/weChatPay")
public class WeChatPayController extends BaseController {

	private static final Logger logger = LogManager.getLogger(AlipayController.class);

	@Value("${wechat_appId}")
	private String appId;

	@Value("${wechat_mchID}")
	private String mchID;

	@Value("${wechat_key}")
	private String key;

	@Value("${wechat_domain}")
	private String domain;

	@Autowired
	private EntryFormMapper entryFormMapper;
	@Autowired
	private ExamMapper examMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 创建订单
	 * 
	 * @author sh
	 * @version V1.0
	 * @throws Exception
	 */
	@RequestMapping("/tradeCreate")
	public ApiResponse tradeCreate(EntryFormPo entryFormPo) throws Exception {
		Long entryFormId = entryFormPo.getEntryFormId();
		LoginInfo info = TokenManager.getNowUser();
		Long studentId = info.getId();

		// 确认报名信息
		EntryForm entryForm = entryFormMapper.selectByPrimaryKey(Long.valueOf(entryFormId));
		if (null == entryForm || entryForm.getStudentId() != studentId) {
			// "不存在！");
			return ApiResponse.getErrorResponse("生成支付订单失败，报名信息有误！");
		}

		Exam exam = examMapper.selectByPrimaryKey(entryForm.getExamId());

		// 订单总金额
		Long total_fee = exam.getMoney();

		String orderNo = System.currentTimeMillis() + (System.nanoTime() + "").substring(7, 10);

		MyConfig config = new MyConfig(appId, mchID, key, domain);
		WXPay wxpay = new WXPay(config, true);

		Map<String, String> data = new HashMap<String, String>();
		data.put("body", "报名缴费");
		data.put("out_trade_no", orderNo);
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		data.put("total_fee", String.valueOf(total_fee * 100));
		data.put("spbill_create_ip", "123.12.12.123");
		data.put("notify_url", "http://120.78.211.181:80/api/weChatPay/weChatNotify");
		data.put("trade_type", "APP"); // 支付类型
		// data.put("product_id", "12");

		try {
			Map<String, String> resp = wxpay.unifiedOrder(data);
			System.out.println(resp);

			// 生成订单信息
			Order order = new Order();
			order.setOrderNo(orderNo);
			order.setStudentId(Long.valueOf(studentId));
			order.setEntryFormId(Long.valueOf(entryFormId));
			order.setMoney(total_fee);
			order.setType(2);
			Date date = new Date();
			order.setCreateTime(date);
			order.setUpdateTime(date);
			orderMapper.insertSelective(order);

			// 修改学生信息
			Student updateStudent = new Student();
			updateStudent.setId(studentId);
			updateStudent.setName(entryFormPo.getStudentName());
			updateStudent.setPinyin(entryFormPo.getPinyin());
			updateStudent.setGender(entryFormPo.getGender());
			updateStudent.setBirthday(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse(entryFormPo.getBirthday()));
			updateStudent.setState(entryFormPo.getState());
			updateStudent.setMajor(entryFormPo.getMajor());
			studentMapper.updateByPrimaryKeySelective(updateStudent);

			PayResponse payResponse = new PayResponse();
			payResponse.setOrderInfo(resp.toString());
			return payResponse;
		} catch (Exception e) {
			return ApiResponse.getErrorResponse("生成支付订单失败，系统异常！");
		}

	}

	/**
	 * 支付成功确定接口
	 * 
	 * @author sh
	 * @version V1.0
	 * @throws Exception
	 */
	@RequestMapping("/tradeQuery")
	@ResponseBody
	public ApiResponse tradeQuery() throws Exception {
		String orderNo = request.getParameter("orderNo");

		MyConfig config = new MyConfig(appId, mchID, key, domain);
		WXPay wxpay = new WXPay(config, true);

		Map<String, String> data = new HashMap<String, String>();
		data.put("out_trade_no", orderNo);

		try {
			Map<String, String> resp = wxpay.orderQuery(data);
			logger.info("订单查询：" + resp);
			String trade_state = resp.get("trade_state");
			if ("SUCCESS".equals(trade_state)) {
				logger.info("支付成功：" + orderNo);
				OrderExample example = new OrderExample();
				example.createCriteria().andOrderNoEqualTo(orderNo);
				List<Order> orders = orderMapper.selectByExample(example);
				if (orders.size() > 0 && 1 == orders.get(0).getStatus()) {
					// 更新订单
					Order updateOrder = new Order();
					updateOrder.setId(orders.get(0).getId());
					updateOrder.setTradeNo("");
					updateOrder.setStatus(0);
					updateOrder.setUpdateTime(new Date());
					orderMapper.updateByPrimaryKeySelective(updateOrder);
				}
			} else {
				return ApiResponse.getErrorResponse("该订单未支付，如果已支付请稍后查看订单是否成功购买或联系客服！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ApiResponse.getErrorResponse("订单支付异常，稍后查看订单是否成功购买或联系客服！");
		}
		return new ApiResponse();
	}

	/**
	 * 支付通知接口
	 * 
	 * @author sh
	 * @version V1.0
	 * @throws Exception
	 */
	@RequestMapping("/weChatNotify")
	public void weChatNotify() throws Exception {
		// PrintWriter writer = response.getWriter();
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String notifyData = new String(outSteam.toByteArray(), "utf-8");
		System.out.println("微信支付通知结果：" + notifyData);// 支付结果通知的xml格式数据

		MyConfig config = new MyConfig(appId, mchID, key, domain);
		WXPay wxpay = new WXPay(config);

		Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData); // 转换成map

		if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
			// 签名正确
			// 进行处理。
			// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
			if ("SUCCESS".equals(notifyMap.get("result_code"))) {
				logger.info("购买成功更新订单！");
				OrderExample example = new OrderExample();
				example.createCriteria().andOrderNoEqualTo(notifyMap.get("out_trade_no"));
				List<Order> orders = orderMapper.selectByExample(example);
				if (orders.size() > 0 && 1 == orders.get(0).getStatus()) {
					// 更新订单
					Order updateOrder = new Order();
					updateOrder.setId(orders.get(0).getId());
					updateOrder.setTradeNo(notifyMap.get("trade_no"));
					updateOrder.setStatus(0);
					updateOrder.setUpdateTime(new Date());
					orderMapper.updateByPrimaryKeySelective(updateOrder);
				} else if (orders.size() == 0) {
					logger.info("订单不存在！");
				}

				// 返回数据
				PrintWriter pw;
				try {
					pw = response.getWriter();
					pw.write("success");
					pw.flush();
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			// 签名错误，如果数据里没有sign字段，也认为是签名错误
		}

	}

}
