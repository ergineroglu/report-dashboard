package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 15:08:29
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardMultiseriesLineChartPortlet extends DashboardLineChartPortlet {

	public DashboardMultiseriesLineChartPortlet() {
		super();
		this.setPortletType(PortletType.MULTISERIES_LINE_CHART);
	}

	/**
	 * @param dataSource
	 * @param portletKey
	 * @param portletTitle
	 * @param portletWidth
	 * @param refreshInterval
	 */
	public DashboardMultiseriesLineChartPortlet(DataSource dataSource,
										  	    String portletKey, 
										  	    String portletTitle, 
										  	    Integer portletWidth,
										  	    Integer refreshInterval) {
		super(dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
		this.setPortletType(PortletType.MULTISERIES_LINE_CHART); 
	}
}
