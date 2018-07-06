package com.xiangxing.controller.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.OrderMapper;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.Order;
import com.xiangxing.model.OrderExample;
import com.xiangxing.utils.StringUtil;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.PayResponse;

/**
 * 支付宝
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/alipay")
public class AlipayController extends BaseController {

	private static final Logger logger = LogManager.getLogger(AlipayController.class);

	private String CHARSET = "UTF-8";

	@Value("${Alipay.APP_ID}")
	private String APP_ID;

	@Value("${Alipay.SERVER_URL}")
	private String SERVER_URL;

	@Value("${Alipay.APP_PRIVATE_KEY}")
	private String APP_PRIVATE_KEY;

	@Value("${Alipay.ALIPAY_PUBLIC_KEY}")
	private String ALIPAY_PUBLIC_KEY;

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
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("我是测试数据");
		model.setSubject("App支付测试Java");

		String orderNo = System.currentTimeMillis() + (System.nanoTime() + "").substring(7, 10);
		model.setOutTradeNo(orderNo);
		model.setTimeoutExpress("30m");
		model.setTotalAmount("0.01");
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl("http://120.78.211.181:80/alipay/alipayNotify");
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			System.out.println(response.getBody());// 就是orderString
													// 可以直接给客户端请求，无需再做处理。

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
			payResponse.setOrderInfo(response.getBody());
			return payResponse;
		} catch (AlipayApiException e) {
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

		AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2"); // 获得初始化的AlipayClient
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();// 创建API对应的request类
		// request.setBizContent("{" +
		// " \"out_trade_no\":\"20150320010101001\"," +
		// " \"trade_no\":\"2014112611001004680073956707\"" + " }");// 设置业务参数

		JSONObject bizContent = new JSONObject();
		if (StringUtil.isNotEmpty(orderNo)) {
			bizContent.put("out_trade_no", orderNo);
		}
		if (StringUtil.isNotEmpty(tradeNo)) {
			bizContent.put("trade_no", tradeNo);
		}
		request.setBizContent(bizContent.toJSONString());// 设置业务参数
		AlipayTradeQueryResponse response = alipayClient.execute(request);// 通过alipayClient调用API，获得对应的response类
		logger.info("购买确认结果" + response.getBody());

		JSONObject jsonObject = new JSONObject();

		try {
			JSONObject bodyJsonObject = JSON.parseObject(response.getBody());
			JSONObject tradeJsonObject = JSON.parseObject(bodyJsonObject.getString("alipay_trade_query_response"));
			if ("TRADE_FINISHED".equals(tradeJsonObject.getString("trade_status")) || "TRADE_SUCCESS".equals(tradeJsonObject.getString("trade_status"))) {
				jsonObject.put("info", "订单支付成功！");
				OrderExample example = new OrderExample();
				example.createCriteria().andOrderNoEqualTo(tradeJsonObject.getString("out_trade_no"));
				List<Order> orders = orderMapper.selectByExample(example);
				if (orders.size() > 0 && 1 == orders.get(0).getStatus()) {
					// 更新订单
					Order updateOrder = new Order();
					updateOrder.setId(orders.get(0).getId());
					updateOrder.setTradeNo(tradeJsonObject.getString("trade_no"));
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
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		try {
			boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, "RSA2");
			logger.info("签名验证" + flag);

			if (flag) {
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
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

	}

}
