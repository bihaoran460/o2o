package com.bihaoran.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断验证码是否正确
 * @author bihaoran
 *
 */
public class CodeUtil {
	public static boolean cheakVerifyCode(HttpServletRequest request)
	{
		String verifyCodeExpect=(String)request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String verifyCodeActual=HttpServletRequestUtil.getString(request,
				"verifyCodeActual");
		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpect))
		{
			return false;
		}
		return true;
		
	}

}
