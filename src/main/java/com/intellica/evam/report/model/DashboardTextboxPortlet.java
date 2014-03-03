package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardTextboxPortlet extends DashboardPortlet {

	private String input;
	
	public DashboardTextboxPortlet() {
		super(PortletType.TEXT_BOX);		
	}
	
	public DashboardTextboxPortlet(DataSource dataSource,
								   String portletKey, 
								   String portletTitle,
								   Integer portletWidth,
								   Integer refreshInterval) {
		super(PortletType.TEXT_BOX, dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
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
