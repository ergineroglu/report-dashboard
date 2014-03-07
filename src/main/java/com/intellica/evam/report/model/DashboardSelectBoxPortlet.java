package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 3 Mar 2014 16:49:14
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardSelectBoxPortlet extends DashboardInputPortlet {

	public DashboardSelectBoxPortlet() {
		super(PortletType.SELECT_BOX);
	}

	/**
	 * @param dataSource
	 * @param portletKey
	 * @param portletTitle
	 * @param portletWidth
	 * @param refreshInterval
	 */
	public DashboardSelectBoxPortlet(DataSource dataSource, 
									 String portletKey, 
									 String portletTitle,
								 	 Integer portletWidth, 
								 	 Integer refreshInterval) {
		super(PortletType.SELECT_BOX, dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
	}

}
