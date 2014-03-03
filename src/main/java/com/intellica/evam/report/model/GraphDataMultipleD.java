package com.intellica.evam.report.model;

import java.io.Serializable;
import java.util.List;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 15:52:16
 * Package: com.intellica.evam.report.model
 *
 */
public class GraphDataMultipleD<K extends Serializable, V extends Serializable> implements GraphData {
	public K x;
	public List<V> listY;
	
	public GraphDataMultipleD(K x, List<V> listY) {
		this.x = x;
		this.listY = listY;
	}
}
