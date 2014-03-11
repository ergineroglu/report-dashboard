package com.intellica.evam.report.model;

import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 20 Şub 2014 10:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardLineChartPortlet extends DashboardXYChartPortlet {

	private String interpolationMethod;
	
	public DashboardLineChartPortlet() {
		super(PortletType.LINE_CHART);		
	}
	
	public DashboardLineChartPortlet(DataSource dataSource,
									 String portletKey,
								   	 String portletTitle,
								   	 Integer portletWidth,
								   	 Integer refreshInterval,
								   	 Boolean autoStart) {
		super(PortletType.LINE_CHART, dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
	}

	/**
	 * @return the interpolationMethod
	 */
	public String getInterpolationMethod() {
		return interpolationMethod;
	}

	/**
	 * @param interpolationMethod the interpolationMethod to set
	 */
	public void setInterpolationMethod(String interpolationMethod) {
		this.interpolationMethod = interpolationMethod;
	}
}
