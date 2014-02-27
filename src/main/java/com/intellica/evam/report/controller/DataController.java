package com.intellica.evam.report.controller;

/**
 * Author: eeroglu
 * Date: 11 Å�ub 2014 16:33:25
 * Package: com.intellica.evam.report.controller
 *
 */


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intellica.evam.report.model.GraphData;
import com.intellica.evam.report.model.GraphData2D;

@Controller
public class DataController {

	private static final Logger logger = LoggerFactory.getLogger(DataController.class);	
	
	@RequestMapping(value = "/data/{userId}/{tabKey}/{portletKey}", method = RequestMethod.POST)
	public @ResponseBody GraphData[] data(@PathVariable int userId, 
					 		  		   	  @PathVariable String tabKey, 
					 		  		   	  @PathVariable String portletKey) {
		
		try {
			ArrayList<GraphData> responseList = new ArrayList<GraphData>();
			responseList.add(new GraphData2D<String, Double>("01.03.2012", 348.13));
			responseList.add(new GraphData2D<String, Double>("10.03.2012", 621.13));
			responseList.add(new GraphData2D<String, Double>("18.03.2012", 500.13));
			responseList.add(new GraphData2D<String, Double>("20.03.2012", 482.13));
			responseList.add(new GraphData2D<String, Double>("21.03.2012", 382.13));
			responseList.add(new GraphData2D<String, Double>("30.04.2012", 590.13));
			responseList.add(new GraphData2D<String, Double>("30.07.2012", 82.13));
			responseList.add(new GraphData2D<String, Double>("01.05.2012", 582.13));
			responseList.add(new GraphData2D<String, Double>("30.02.2013", 182.13));
			responseList.add(new GraphData2D<String, Double>("12.03.2013", 782.13));
			responseList.add(new GraphData2D<String, Double>("21.08.2013", 282.13));
			responseList.add(new GraphData2D<String, Double>("30.11.2013", 690.13));
			responseList.add(new GraphData2D<String, Double>("15.12.2012", 184.13));
			
			GraphData[] responseArray = new GraphData[responseList.size()];
			responseArray = responseList.toArray(responseArray);
			return responseArray;
		}
	    catch(Exception ex) {
	    	logger.error("Exception in data handling: %s", ex.toString());
	    	return null;
	    }		
	}

}
