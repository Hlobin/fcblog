package com.feicheng.blog.filter;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * @author Lenovo
 */
public class URLPathMatchingFilter extends PathMatchingFilter {


	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {

		
		String requestURI = getPathWithinApplication(request);

		Subject subject = SecurityUtils.getSubject();


		// 如果没有登录，就跳转到登录页面

		if (!subject.isAuthenticated()) {

			WebUtils.issueRedirect(request, response, "/admin/login.html");

			return false;
		}

		return true;
	}
}