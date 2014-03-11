package com.intellica.evam.report.controller;

/**
 * Author: eeroglu
 * Date: 11 Å�ub 2014 16:33:25
 * Package: com.intellica.evam.report.controller
 *
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intellica.evam.report.data.GraphData;
import com.intellica.evam.report.model.DashboardPortlet;
import com.intellica.evam.report.service.PortletCacheService;
import com.intellica.evam.report.util.ResourceNotFoundException;

@Controller
public class DataController {

	private static final Logger logger = LoggerFactory.getLogger(DataController.class);	
	
	@Autowired
	PortletCacheService portletCacheService;
	
	@RequestMapping(value = "/data/{userId}/{tabKey}/{portletKey}", method = RequestMethod.POST)
	public @ResponseBody GraphData[] data(@PathVariable int userId, 
					 		  		   	  @PathVariable String tabKey, 
					 		  		   	  @PathVariable String portletKey,
					 		  		   	  @RequestBody Map<String, String> dataParameters) {
		
		try {
			// get portlet
			DashboardPortlet portlet = portletCacheService.getPortlet(userId, tabKey, portletKey);
			if(portlet == null) {
				throw new ResourceNotFoundException(String.format("Portlet For User = %d, Tab = %s, Key = %s", userId, tabKey, portletKey));
			}
			// get data
			List<? extends GraphData> responseList = null;
			switch(portlet.getPortletType()) {
			case TEXT_BOX:
				responseList = portlet.getDataSource().getData1D();
				break;
			case AREA_CHART:
			case LINE_CHART:
			case BAR_CHART:
			case PIE_CHART:
			case DONUT_CHART:
			case SELECT_BOX:
				responseList = portlet.getDataSource().getData2D(dataParameters);
				break;
			case MULTISERIES_LINE_CHART:
			case GROUP_BAR_CHART:
				responseList = portlet.getDataSource().getData3D(dataParameters);
				break;
			case DATA_TABLE:
				responseList = portlet.getDataSource().getDataMultipleD(dataParameters);
				break;
			default:
				responseList = new ArrayList<GraphData>();
				break;
			}
			
			GraphData[] responseArray = new GraphData[responseList.size()];
			responseArray = responseList.toArray(responseArray);
			return responseArray;
		}
	    catch(Exception ex) {
	    	logger.error("Exception in data handling", ex);
	    	return null;
	    }		
	}
}
