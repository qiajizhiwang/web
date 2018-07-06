package com.github.wxpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {

	private String appId;
	private String mchID;
	private String key;
	private String domain;

	private byte[] certData;

	public MyConfig(String appId, String mchID, String key, String domain) {
		super();
		this.appId = appId;
		this.mchID = mchID;
		this.key = key;
		this.domain = domain;
	}

	// TODO 暂时不需要证书
	// public MyConfig() throws Exception {
	// String certPath = "/path/to/apiclient_cert.p12";
	// File file = new File(certPath);
	// InputStream certStream = new FileInputStream(file);
	// this.certData = new byte[(int) file.length()];
	// certStream.read(this.certData);
	// certStream.close();
	// }

	@Override
	String getAppID() {
		return this.appId;
	}

	@Override
	String getMchID() {
		return this.mchID;
	}

	@Override
	String getKey() {
		return this.key;
	}

	@Override
	String getDomain() {
		return this.domain; // 域名
	}

	@Override
	boolean getPrimaryDomain() {
		return true; // 该域名是否为主域名。例如:api.mch.weixin.qq.com为主域名
	}

	@Override
	InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	@Override
	IWXPayDomain getWXPayDomain() {
		// TODO Auto-generated method stub
		return new MyWXPayDomain();
	}

}
