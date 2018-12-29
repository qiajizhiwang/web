package com.xiangxing.config;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiangxing.model.User;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		WebUtils.getAndClearSavedRequest(request);// 清除之前的地址（跳转到登录页面之前要访问的地址）
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				// 本次用户登陆账号
				String account = this.getUsername(request);

				Subject subject = this.getSubject(request, response);
				// 之前登陆的用户
				User user = (User) subject.getPrincipal();
				// 如果两次登陆的用户不一样，则先退出之前登陆的用户
				if (account != null && user != null && !account.equals(user.getName())) {
					subject.logout();
				}
			}
		}

		return super.isAccessAllowed(request, response, mappedValue);

	}

}