package com.xiangxing.controller.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.OrderMapper;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.Order;
import com.xiangxing.model.OrderExample;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.PayResponse;

@Controller
@RequestMapping("/weChatPay")
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
	private OrderMapper orderMapper;

	/**
	 * 购买接口
	 * 
	 * @author sh
	 * @version V1.0
	 * @throws Exception
	 */
	@RequestMapping("/buy")
	public ApiResponse buy() throws Exception {
		String entryFormId = request.getParameter("entryFormId");
		LoginInfo info = TokenManager.getNowUser();
		Long studentId = info.getId();

		// 确认报名信息
		EntryForm entryForm = entryFormMapper.selectByPrimaryKey(Long.valueOf(entryFormId));
		if (null == entryForm || entryForm.getStudentId() != studentId) {
			// "不存在！");
			return ApiResponse.getErrorResponse("生成支付订单失败，报名信息有误！");
		}

		String orderNo = System.currentTimeMillis() + (System.nanoTime() + "").substring(7, 10);

		MyConfig config = new MyConfig(appId, mchID, key, domain);
		WXPay wxpay = new WXPay(config, true);

		Map<String, String> data = new HashMap<String, String>();
		data.put("body", "腾讯充值中心-QQ会员充值");
		data.put("out_trade_no", "2016090910595900000012");
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		data.put("total_fee", "1");
		data.put("spbill_create_ip", "123.12.12.123");
		data.put("notify_url", "http://120.78.211.181:80/alipay/alipayNotify");
		data.put("trade_type", "NATIVE"); // 此处指定为扫码支付
		data.put("product_id", "12");

		try {
			Map<String, String> resp = wxpay.unifiedOrder(data);
			System.out.println(resp);

			// 生成订单信息
			Order order = new Order();
			order.setOrderNo(orderNo);
			order.setStudentId(Long.valueOf(studentId));
			// TODO
			order.setMoney(null);
			Date date = new Date();
			order.setCreateTime(date);
			order.setUpdateTime(date);
			orderMapper.insertSelective(order);

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
	@RequestMapping("/buysucceed")
	public ApiResponse buysucceed() throws Exception {
		String orderNo = request.getParameter("orderNo");
		String tradeNo = request.getParameter("tradeNo");

		MyConfig config = new MyConfig(appId, mchID, key, domain);
		WXPay wxpay = new WXPay(config, true);

		Map<String, String> data = new HashMap<String, String>();
		data.put("out_trade_no", "2016090910595900000012");

		try {
			Map<String, String> resp = wxpay.orderQuery(data);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (true) {
				OrderExample example = new OrderExample();
				example.createCriteria().andOrderNoEqualTo("");
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
				return ApiResponse.getErrorResponse("订单支付异常，稍后查看订单是否成功购买或联系客服！");
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
	@RequestMapping("/alipayNotify")
	public void alipayNotify() {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();

		if (true) {
			if ("TRADE_FINISHED".equals(params.get("trade_status")) || "TRADE_SUCCESS".equals(params.get("trade_status"))) {
				logger.info("购买成功更新订单！");
				OrderExample example = new OrderExample();
				example.createCriteria().andOrderNoEqualTo(params.get("out_trade_no"));
				List<Order> orders = orderMapper.selectByExample(example);
				if (orders.size() > 0 && 1 == orders.get(0).getStatus()) {
					// 更新订单
					Order updateOrder = new Order();
					updateOrder.setId(orders.get(0).getId());
					updateOrder.setTradeNo(params.get("trade_no"));
					updateOrder.setStatus(0);
					updateOrder.setUpdateTime(new Date());
					orderMapper.updateByPrimaryKeySelective(updateOrder);
				} else if (orders.size() == 0) {
					logger.info("订单不存在！");
				}
			}
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

}
