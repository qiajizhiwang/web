package com.xiangxing.config;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiangxing.mapper.SysUserMapper;
import com.xiangxing.model.SysUser;
import com.xiangxing.model.SysUserExample;

@Component
public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	private SysUserMapper sysUserMapper;

	public MyShiroRealm() {
		super();
		super.setCredentialsMatcher(new HashedCredentialsMatcher("MD5"));
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
		// 获取用户的输入的账号.
		String username = (String) token.getPrincipal();
		System.out.println(token.getCredentials());
		// 通过username从数据库中查找 User对象，如果找到，没找到.
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		SysUserExample example = new SysUserExample();
		example.createCriteria().andLoginNameEqualTo(username);

		List<SysUser> userInfos = sysUserMapper.selectByExample(example);

		if (userInfos == null || userInfos.size() != 1) {
			return null;
		}
		SysUser userInfo = userInfos.get(0);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userInfo, // 用户名
				userInfo.getPassword(), // 密码
				ByteSource.Util.bytes(username + "xiaochendaiwolaiqiaji"), // salt=username+salt
				getName() // realm name
		);
		return authenticationInfo;
	}
	
	public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        String credentials = "1";
        int hashIterations = 1;
        Object obj = new SimpleHash(hashAlgorithmName, credentials, "1"+ "xiaochendaiwolaiqiaji", hashIterations);
        System.out.println(obj);
    }
}
