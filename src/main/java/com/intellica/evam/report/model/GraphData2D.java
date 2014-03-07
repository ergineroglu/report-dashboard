package com.intellica.evam.report.model;

import java.io.Serializable;

/**
 * Author: eeroglu
 * Date: 14 Şub 2014 13:45:20
 * Package: com.intellica.evam.report.model
 *
 */
public class GraphData2D<X extends Serializable, Y extends Serializable> extends GraphData1D<X> {
	public Y y;
	
	public GraphData2D(X x, Y y) {
		super(x);
		this.y = y;
	}
}
