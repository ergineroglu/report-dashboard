package com.intellica.evam.report.model;

import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:46:42
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardPortlet {
	private DataSource dataSource;
	private String portletKey;
	private String portletTitle;
	private PortletType portletType;
	private Integer portletWidth; // width by percent of a row - min 1 - max 100 - default 50
	private Integer refreshInterval; // in seconds 	
	private Boolean autoStart;
	
	public DashboardPortlet(PortletType portletType) {
		this.portletType = portletType; 
	}
	
	public DashboardPortlet(PortletType portletType, 
							DataSource dataSource,
							String portletKey, 
							String portletTitle,
							Integer portletWidth,
							Integer refreshInterval,
							Boolean autoStart) {
		this.portletType = portletType; 
		this.dataSource = dataSource;
		this.portletKey = portletKey;
		this.portletTitle = portletTitle;
		this.portletWidth = portletWidth;
		this.refreshInterval = refreshInterval;
		this.autoStart = autoStart;
	}
	
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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

	/**
	 * @return the autoStart
	 */
	public Boolean getAutoStart() {
		return autoStart;
	}

	/**
	 * @param autoStart the autoStart to set
	 */
	public void setAutoStart(Boolean autoStart) {
		this.autoStart = autoStart;
	}
	
}
