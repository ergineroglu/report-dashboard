package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:46:42
 * Package: com.intellica.evam.model
 *
 */
public class DashboardPortlet {
	private String portletKey;
	private String portletTitle;
	private PortletType portletType;
	private Integer portletWidth; // width by percent of a row - min 1 - max 100 - default 50
	private Integer refreshInterval; // in seconds 
	
	public DashboardPortlet(PortletType portletType) {
		this.portletType = portletType; 
	}
	
	public DashboardPortlet(PortletType portletType, 
							String portletKey, 
							String portletTitle,
							Integer portletWidth,
							Integer refreshInterval) {
		this.portletType = portletType; 
		this.portletKey = portletKey;
		this.portletTitle = portletTitle;
		this.portletWidth = portletWidth;
		this.refreshInterval = refreshInterval;
	}
	
	/**
	 * @return the portletKey
	 */
	public String getPortletKey() {
		return portletKey;
	}
	/**
	 * @param portletKey the portletKey to set
	 */
	public void setPortletKey(String portletKey) {
		this.portletKey = portletKey;
	}
	/**
	 * @return the portletTitle
	 */
	public String getPortletTitle() {
		return portletTitle;
	}
	/**
	 * @param portletTitle the portletTitle to set
	 */
	public void setPortletTitle(String portletTitle) {
		this.portletTitle = portletTitle;
	}
	/**
	 * @return the portletType
	 */
	public PortletType getPortletType() {
		return portletType;
	}
	/**
	 * @param portletType the portletType to set
	 */
	public void setPortletType(PortletType portletType) {
		this.portletType = portletType;
	}
	/**
	 * @return the portletWidth
	 */
	public Integer getPortletWidth() {
		return portletWidth;
	}
	/**
	 * @param portletWidth the portletWidth to set
	 */
	public void setPortletWidth(Integer portletWidth) {
		this.portletWidth = portletWidth;
	}
	/**
	 * @return the refreshInterval
	 */
	public Integer getRefreshInterval() {
		return refreshInterval;
	}
	/**
	 * @param refreshInterval the refreshInterval to set
	 */
	public void setRefreshInterval(Integer refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	
}
