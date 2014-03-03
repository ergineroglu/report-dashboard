package com.intellica.evam.report.model;

import java.io.Serializable;

/**
 * Author: eeroglu
 * Date: 14 Şub 2014 13:45:20
 * Package: com.intellica.evam.report.model
 *
 */
public class GraphData2D<K extends Serializable, V extends Serializable> implements GraphData {
	public K x;
	public V y;
	
	public GraphData2D(K x, V y) {
		this.x = x;
		this.y = y;
	}
}
