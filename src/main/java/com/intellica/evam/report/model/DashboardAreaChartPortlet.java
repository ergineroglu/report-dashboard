package com.intellica.evam.report.model;

import com.intellica.evam.report.data.source.DataSource;
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
	
	public DashboardAreaChartPortlet(DataSource dataSource,
									 String portletKey, 
								   	 String portletTitle,
								   	 Integer portletWidth,
								   	 Integer refreshInterval,
								   	 Boolean autoStart) {
		super(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		this.setPortletType(PortletType.AREA_CHART);
	}
}
