package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 15:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardDonutChartPortlet extends DashboardPortlet {
	
	public DashboardDonutChartPortlet() {
		super(PortletType.DONUT_CHART);
	}
	
	public DashboardDonutChartPortlet(DataSource dataSource,
									  String portletKey, 								   	  
									  String portletTitle,
								   	  Integer portletWidth,
								   	  Integer refreshInterval) {
		super(PortletType.DONUT_CHART, dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
	}
}
