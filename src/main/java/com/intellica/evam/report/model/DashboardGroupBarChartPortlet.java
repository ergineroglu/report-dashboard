package com.intellica.evam.report.model;

import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Mar 2014 09:49:23
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardGroupBarChartPortlet extends DashboardBarChartPortlet {

	public DashboardGroupBarChartPortlet() {
		super();
		this.setPortletType(PortletType.GROUP_BAR_CHART);
	}

	/**
	 * @param dataSource
	 * @param portletKey
	 * @param portletTitle
	 * @param portletWidth
	 * @param refreshInterval
	 * @param autoStart
	 */
	public DashboardGroupBarChartPortlet(DataSource dataSource,
										 String portletKey, 
										 String portletTitle, 
										 Integer portletWidth,
										 Integer refreshInterval, 
										 Boolean autoStart) {
		super(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		this.setPortletType(PortletType.GROUP_BAR_CHART);
	}

}
