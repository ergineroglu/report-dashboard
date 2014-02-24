package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 20 Şub 2014 10:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardLineChartPortlet extends DashboardPortlet {

	private String axisXName;
	private String axisYName;
	private String axisXType;
	private String axisYType;
	private String axisXFormat;
	private String axisYFormat;
	private String interpolationMethod;
	private Boolean brush;
	
	public DashboardLineChartPortlet() {
		super(PortletType.LINE_CHART);		
	}
	
	public DashboardLineChartPortlet(DataSource dataSource,
									 String portletKey,
								   	 String portletTitle,
								   	 Integer portletWidth,
								   	 Integer refreshInterval) {
		super(PortletType.LINE_CHART, dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
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
	
	/**
	 * @return the axisXType
	 */
	public String getAxisXType() {
		return axisXType;
	}

	/**
	 * @param axisXType the axisXType to set
	 */
	public void setAxisXType(String axisXType) {
		this.axisXType = axisXType;
	}

	/**
	 * @return the axisYType
	 */
	public String getAxisYType() {
		return axisYType;
	}

	/**
	 * @param axisYType the axisYType to set
	 */
	public void setAxisYType(String axisYType) {
		this.axisYType = axisYType;
	}

	/**
	 * @return the axisXFormat
	 */
	public String getAxisXFormat() {
		return axisXFormat;
	}

	/**
	 * @param axisXFormat the axisXFormat to set
	 */
	public void setAxisXFormat(String axisXFormat) {
		this.axisXFormat = axisXFormat;
	}

	/**
	 * @return the axisYFormat
	 */
	public String getAxisYFormat() {
		return axisYFormat;
	}

	/**
	 * @param axisYFormat the axisYFormat to set
	 */
	public void setAxisYFormat(String axisYFormat) {
		this.axisYFormat = axisYFormat;
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

	/**
	 * @return the brush
	 */
	public Boolean getBrush() {
		return brush;
	}

	/**
	 * @param brush the brush to set
	 */
	public void setBrush(Boolean brush) {
		this.brush = brush;
	}
}
