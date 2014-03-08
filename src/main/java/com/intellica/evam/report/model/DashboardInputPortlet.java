package com.intellica.evam.report.model;

import java.util.ArrayList;
import java.util.List;

import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:58:39
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardInputPortlet extends DashboardPortlet {

	private List<DashboardInteraction> interactions;
	
	public DashboardInputPortlet(PortletType type) {
		super(type);
		this.setInteractions(new ArrayList<DashboardInteraction>());
	}
	
	public DashboardInputPortlet(PortletType type,
								 DataSource dataSource,
								 String portletKey, 
								 String portletTitle,
								 Integer portletWidth,
								 Integer refreshInterval,
								 Boolean autoStart) {
		super(type, dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		this.setInteractions(new ArrayList<DashboardInteraction>());
	}

	/**
	 * @return the interactions
	 */
	public List<DashboardInteraction> getInteractions() {
		return interactions;
	}

	/**
	 * @param interactions the interactions to set
	 */
	public void setInteractions(List<DashboardInteraction> interactions) {
		this.interactions = interactions;
	}

}
