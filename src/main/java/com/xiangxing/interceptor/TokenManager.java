package com.xiangxing.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import com.xiangxing.controller.api.LoginInfo;

public class TokenManager {

	private static ConcurrentHashMap<String, LoginInfo> users = new ConcurrentHashMap<>();

	private static ThreadLocal<String> tokenCache = new ThreadLocal<>();

	public static LoginInfo getUser(String token) {
		return users.get(token);

	}

	public static LoginInfo getNowUser() {
		return users.get(getToken());

	}

	public static void setUser(String token, LoginInfo user) {
		users.putIfAbsent(token, user);
	}

	public static String getToken() {
		return tokenCache.get();
	}

	public static void setToken(String token) {
		tokenCache.set(token);
	}

	public static void clearToken() {
		tokenCache.remove();
	}

}
