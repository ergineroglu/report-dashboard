package com.intellica.evam.report.util;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:51:03
 * Package: com.intellica.evam.report.util
 *
 */
public enum PortletType {
	TEXT_BOX("textbox"),
	SELECT_BOX("selectbox"),
	RADIO_BUTTON("radiobutton"),
	LINE_CHART("linechart"),
	MULTISERIES_LINE_CHART("multiserieslinechart"),
	AREA_CHART("areachart"),
	DONUT_CHART("donutchart"),
	PIE_CHART("piechart");
	
	private String name;
	
	PortletType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	
	
	public static PortletType fromName(String name) {
		if (name != null) {
			for (PortletType type : PortletType.values()) {
				if (name.equalsIgnoreCase(type.name)) return type;
			}
	    }
	    return null;
	}
}
