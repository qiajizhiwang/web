package com.github.wxpay.sdk;

public class MyWXPayDomain implements IWXPayDomain {

	@Override
	public void report(String domain, long elapsedTimeMillis, Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public DomainInfo getDomain(WXPayConfig config) {
		// TODO Auto-generated method stub
		return new DomainInfo(config.getDomain(), config.getPrimaryDomain());
	}

}
