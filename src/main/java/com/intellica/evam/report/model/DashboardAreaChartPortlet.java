package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 15:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardAreaChartPortlet extends DashboardPortlet {

	private String axisXName;
	private String axisYName;
	
	public DashboardAreaChartPortlet() {
		super(PortletType.AREA_CHART);		
	}
	
	public DashboardAreaChartPortlet(String portletKey, 
								   	 String portletTitle,
								   	 Integer portletWidth,
								   	 Integer refreshInterval) {
		super(PortletType.AREA_CHART, portletKey, portletTitle, portletWidth, refreshInterval);
	}

	/**
	 * @return the axisXName
	 */
	public String getAxisXName() {
		return axisXName;
	}

	/**
	 * @param axisXName the axisXName to set
	 */
	public void setAxisXName(String axisXName) {
		this.axisXName = axisXName;
	}

	/**
	 * @return the axisYName
	 */
	public String getAxisYName() {
		return axisYName;
	}

	/**
	 * @param axisYName the axisYName to set
	 */
	public void setAxisYName(String axisYName) {
		this.axisYName = axisYName;
	}
}
