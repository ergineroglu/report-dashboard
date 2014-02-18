package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 17 Şub 2014 08:37:21
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardAreaChartBrushPortlet extends DashboardAreaChartPortlet {

	public DashboardAreaChartBrushPortlet() {
		super();
		this.setPortletType(PortletType.AREA_CHART_BRUSH);
	}
	
	public DashboardAreaChartBrushPortlet(String portletKey, 
								   		  String portletTitle,
								   		  Integer portletWidth, 
								   		  Integer refreshInterval) {
		super(portletKey, portletTitle, portletWidth, refreshInterval);		
		this.setPortletType(PortletType.AREA_CHART_BRUSH);
	}

}
