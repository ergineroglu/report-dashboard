package com.intellica.evam.report.model;

/**
 * Author: eeroglu
 * Date: 7 Mar 2014 09:19:56
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardInteraction {

	private String tabKey;
	private String portletKey;
	private String variable;
	private Boolean trigger;
	
	/**
	 * @param tabKey
	 * @param portletKey
	 * @param variable
	 * @param trigger
	 */
	public DashboardInteraction(String tabKey,
								String portletKey,
								String variable,
								Boolean trigger) {
		this.tabKey = tabKey;
		this.portletKey = portletKey;
		this.variable = variable;
		this.trigger = trigger;
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
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	/**
	 * @return the trigger
	 */
	public Boolean getTrigger() {
		return trigger;
	}

	/**
	 * @param trigger the trigger to set
	 */
	public void setTrigger(Boolean trigger) {
		this.trigger = trigger;
	}
	
}
