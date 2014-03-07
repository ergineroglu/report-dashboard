package com.intellica.evam.report.model;

import java.io.Serializable;

/**
 * Author: eeroglu
 * Date: 14 Şub 2014 13:45:20
 * Package: com.intellica.evam.report.model
 *
 */
public class GraphData3D<X extends Serializable, Y extends Serializable, Z extends Serializable> extends GraphData2D<X, Y> {
	public Z z;
	
	public GraphData3D(X x, Y y, Z z) {		
		super(x, y);		
		this.z = z;
	}
}
