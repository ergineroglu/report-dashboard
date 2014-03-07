package com.intellica.evam.report.model;

import java.util.List;
import java.util.Map;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 09:14:28
 * Package: com.intellica.evam.report.model
 *
 */
public interface DataSource {
	
	// Data accessor methods
	public List<? extends GraphData> getData1D();
	public List<? extends GraphData> getData1D(Map<String, String> parameters);
	
	public List<? extends GraphData> getData2D();
	public List<? extends GraphData> getData2D(Map<String, String> parameters);
	
	public List<? extends GraphData> getData3D();
	public List<? extends GraphData> getData3D(Map<String, String> parameters);
	
	public List<? extends GraphData> getDataMultipleD();
	public List<? extends GraphData> getDataMultipleD(Map<String, String> parameters);
}
