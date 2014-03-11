package com.intellica.evam.report.jstl;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 15:45:20
 * Package: com.intellica.evam.report.jstl
 *
 */
public class PortletJSTLFunctions {

	public static Integer widthClass(Integer widthByPercent) {
		return new Double(Math.min(12, Math.max(1, Math.round(widthByPercent / (100.0 / 12))))).intValue();
	}
	
	public static String portletIcon(PortletType type) {
		switch(type) {
		case TEXT_BOX:
		case SELECT_BOX:
			return "glyphicon-edit";
		case DATA_TABLE:
			return "glyphicon-th";
		case LINE_CHART:
		case AREA_CHART:
		case MULTISERIES_LINE_CHART:
		case BAR_CHART:
		case GROUP_BAR_CHART:
		default:
			return "glyphicon-signal";
		case PIE_CHART:
		case DONUT_CHART:
			return "glyphicon-adjust";
		}
	}	
}
