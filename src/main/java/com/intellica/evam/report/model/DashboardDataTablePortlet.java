package com.intellica.evam.report.model;

import com.intellica.evam.report.util.PortletType;

/**
 * Author: eeroglu
 * Date: 5 Mar 2014 15:48:19
 * Package: com.intellica.evam.report.model
 *
 */
public class DashboardDataTablePortlet extends DashboardPortlet {

	private String[] columnNames;
	
	public DashboardDataTablePortlet() {
		super(PortletType.DATA_TABLE);
		this.columnNames = new String[0];
	}

	/**
	 * @param dataSource
	 * @param portletKey
	 * @param portletTitle
	 * @param portletWidth
	 * @param refreshInterval
	 */
	public DashboardDataTablePortlet(DataSource dataSource, 
									 String portletKey, 
									 String portletTitle,
									 Integer portletWidth, 
									 Integer refreshInterval) {
		super(PortletType.DATA_TABLE, dataSource, portletKey, portletTitle, portletWidth, refreshInterval);
		this.columnNames = new String[0];
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

}
