package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 15:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardAreaChartPortlet extends DashboardLineChartPortlet {

	public DashboardAreaChartPortlet() {
		super();
		this.setPortletType(PortletType.AREA_CHART);
	}
	
	public DashboardAreaChartPortlet(String portletKey, 
								   	 String portletTitle,
								   	 Integer portletWidth,
								   	 Integer refreshInterval) {
		super(portletKey, portletTitle, portletWidth, refreshInterval);
		this.setPortletType(PortletType.AREA_CHART);
	}
}
