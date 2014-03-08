package com.intellica.evam.report.data;

import java.io.Serializable;

/**
 * Author: eeroglu
 * Date: 4 Mar 2014 11:13:42
 * Package: com.intellica.evam.report.model
 *
 */
public class GraphData1D<X extends Serializable> implements GraphData {

	public X x;
	
	public GraphData1D(X x) {
		this.x = x;
	}

}
