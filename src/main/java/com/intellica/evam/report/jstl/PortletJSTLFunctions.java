package com.intellica.evam.report.jstl;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 15:45:20
 * Package: com.intellica.evam.report.jstl
 *
 */
public class PortletJSTLFunctions {

	public static Integer widthClass(Integer widthByPercent) {
		return new Double(Math.min(12, Math.max(1, Math.ceil(widthByPercent / (100.0 / 12))))).intValue();
	}
}
