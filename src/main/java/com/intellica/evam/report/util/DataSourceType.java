package com.intellica.evam.report.util;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 09:12:35
 * Package: com.intellica.evam.report.util
 *
 */
public enum DataSourceType {
	DATABASE("database"),
	CONSTANT("constant");
	
	private String name;
	
	DataSourceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	
	
	public static DataSourceType fromName(String name) {
		if (name != null) {
			for (DataSourceType type : DataSourceType.values()) {
				if (name.equalsIgnoreCase(type.name)) return type;
			}
	    }
	    return null;
	}
}
