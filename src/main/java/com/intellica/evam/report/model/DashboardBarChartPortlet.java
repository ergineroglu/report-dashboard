package com.intellica.evam.report.model;

import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 10 Mar 2014 11:23:49
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardBarChartPortlet extends DashboardXYChartPortlet {

	public DashboardBarChartPortlet() {
		super(PortletType.BAR_CHART);
	}

	/**
	 * @param dataSource
	 * @param portletKey
	 * @param portletTitle
	 * @param portletWidth
	 * @param refreshInterval
	 * @param autoStart
	 */
	public DashboardBarChartPortlet(DataSource dataSource,
								    String portletKey, 
								    String portletTitle, 
								    Integer portletWidth,
							    	Integer refreshInterval, 
							    	Boolean autoStart) {
		super(PortletType.BAR_CHART, dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
	}

}
