package com.intellica.evam.report.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.intellica.evam.report.model.DashboardPortlet;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 09:42:16
 * Package: com.intellica.evam.report.service
 *
 */
@Service("portletCacheService")
public class PortletCacheService {

	private final String KEY_DELIMITER = "___"; 
	private Map<String, DashboardPortlet> portletCache = new ConcurrentHashMap<String, DashboardPortlet>();
	
	public DashboardPortlet getPortlet(int userId, String tabKey, String portletKey) {
		// create key and search in cache
		return portletCache.get(this.createKey(userId, tabKey, portletKey));
	}
	
	public DashboardPortlet setPortlet(int userId, String tabKey, String portletKey, DashboardPortlet portlet) {
		// create key and add to cache
		return portletCache.put(this.createKey(userId, tabKey, portletKey), portlet);
	}
	
	private String createKey(int userId, String tabKey, String portletKey) {
		return new StringBuilder().append(userId).append(KEY_DELIMITER)
							      .append(tabKey).append(KEY_DELIMITER)
							      .append(portletKey).toString();
	}
}
