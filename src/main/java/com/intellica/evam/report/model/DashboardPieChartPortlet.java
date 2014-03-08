package com.intellica.evam.report.model;

import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 15:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardPieChartPortlet extends DashboardPortlet {
	
	public DashboardPieChartPortlet() {
		super(PortletType.PIE_CHART);
	}
	
	public DashboardPieChartPortlet(DataSource dataSource,
									String portletKey, 									
								   	String portletTitle,
								   	Integer portletWidth,
								   	Integer refreshInterval,
								   	Boolean autoStart) {
		super(PortletType.PIE_CHART, dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
	}
}
