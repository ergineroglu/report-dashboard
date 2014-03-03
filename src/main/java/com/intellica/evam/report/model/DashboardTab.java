package com.intellica.evam.report.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:43:48
 * Package: com.intellica.evam.model
 *
 */
public class DashboardTab {
	private String tabKey;
	private String tabTitle;
	private Map<String, DashboardPortlet> tabPortlets;
	
	public DashboardTab() {
		this.tabPortlets = new LinkedHashMap<String, DashboardPortlet>();
	}
	
	public DashboardTab(String tabKey, String tabTitle) {
		this.tabPortlets = new LinkedHashMap<String, DashboardPortlet>();
		this.tabKey = tabKey;
		this.tabTitle = tabTitle;
	}
	
	public void addPortlet(String portletKey, DashboardPortlet portlet) {
		this.tabPortlets.put(portletKey, portlet);
	}
	
	/**
	 * @return the tabKey
	 */
	public String getTabKey() {
		return tabKey;
	}
	/**
	 * @param tabKey the tabKey to set
	 */
	public void setTabKey(String tabKey) {
		this.tabKey = tabKey;
	}
	/**
	 * @return the tabTitle
	 */
	public String getTabTitle() {
		return tabTitle;
	}
	/**
	 * @param tabTitle the tabTitle to set
	 */
	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}
	/**
	 * @return the tabPortlets
	 */
	public Map<String, DashboardPortlet> getTabPortlets() {
		return tabPortlets;
	}
	/**
	 * @param tabPortlets the tabPortlets to set
	 */
	public void setTabPortlets(Map<String, DashboardPortlet> tabPortlets) {
		this.tabPortlets = tabPortlets;
	}
}
