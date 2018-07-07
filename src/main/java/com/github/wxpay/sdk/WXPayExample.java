package com.github.wxpay.sdk;

import java.util.HashMap;
import java.util.Map;

public class WXPayExample {
	public static void main(String[] args) throws Exception {

//		MyConfig config = new MyConfig("wx7cf0ac001b331ca2", "1503959681", "61a99775b3ac44209679e85183fd366c", "api.mch.weixin.qq.com");
//		WXPay wxpay = new WXPay(config, true);
//
//		Map<String, String> data = new HashMap<String, String>();
//		data.put("body", "腾讯充值中心-QQ会员充值");
//		data.put("out_trade_no", "2016090910595900000012");
//		data.put("device_info", "");
//		data.put("fee_type", "CNY");
//		data.put("total_fee", "1");
//		data.put("spbill_create_ip", "123.12.12.123");
//		data.put("notify_url", "http://www.example.com/wxpay/notify");
//		data.put("trade_type", "NATIVE"); // 此处指定为扫码支付
//		data.put("product_id", "12");
//
//		try {
//			Map<String, String> resp = wxpay.unifiedOrder(data);
//			System.out.println(resp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		MyConfig config = new MyConfig("wx7cf0ac001b331ca2", "1503959681", "61a99775b3ac44209679e85183fd366c", "api.mch.weixin.qq.com");
		 WXPay wxpay = new WXPay(config,true);
		
		 Map<String, String> data = new HashMap<String, String>();
		 data.put("out_trade_no", "2016090910595900000012");
		
		 try {
		 Map<String, String> resp = wxpay.orderQuery(data);
		 System.out.println(resp);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
	}
}
