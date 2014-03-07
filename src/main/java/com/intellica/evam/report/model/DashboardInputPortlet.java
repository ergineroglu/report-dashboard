package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardInputPortlet extends DashboardPortlet {

	private String input;
	
	public DashboardInputPortlet(PortletType type) {
		super(type);		
	}
	
	public DashboardInputPortlet(PortletType type,
								 DataSource dataSource,
								 String portletKey, 
								 String portletTitle,
								 Integer portletWidth,
								 Integer refreshInterval) {
		super(type, dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
	}

	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(String input) {
		this.input = input;
	}

}
