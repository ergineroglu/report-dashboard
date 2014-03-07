package com.intellica.evam.report.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 10:45:19
 * Package: com.intellica.evam.report.model
 *
 */
public class ConstantDataSource implements DataSource {

	private List<String[]> dataRows;
	
	/**
	 * 
	 */
	public ConstantDataSource(String delimiter, List<String> rows) {
		this.dataRows = new ArrayList<String[]>();
		for(String row : rows) {
			dataRows.add(StringUtils.trimArrayElements(row.split(delimiter)));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData1D()
	 */
	@Override
	public List<? extends GraphData> getData1D() {
		return this.getData1D(new HashMap<String, String>());
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData1D(java.util.Map)
	 */
	@Override
	public List<? extends GraphData> getData1D(Map<String, String> parameters) {
		List<GraphData1D<String>> resultList = new ArrayList<GraphData1D<String>>();
		for(String[] row: dataRows) {
			resultList.add(new GraphData1D<String>(row[0]));
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData2D()
	 */
	@Override
	public List<? extends GraphData> getData2D() {
		return this.getData2D(new HashMap<String, String>());
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData2D(java.util.Map)
	 */
	@Override
	public List<? extends GraphData> getData2D(Map<String, String> parameters) {
		List<GraphData2D<String, String>> resultList = new ArrayList<GraphData2D<String, String>>();
		for(String[] row: dataRows) {
			resultList.add(new GraphData2D<String, String>(row[0], row[1]));
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData3D()
	 */
	@Override
	public List<? extends GraphData> getData3D() {
		return this.getData3D(new HashMap<String, String>());
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData3D(java.util.Map)
	 */
	@Override
	public List<? extends GraphData> getData3D(Map<String, String> parameters) {
		List<GraphData3D<String, String, String>> resultList = new ArrayList<GraphData3D<String, String, String>>();
		for(String[] row: dataRows) {
			resultList.add(new GraphData3D<String, String, String>(row[0], row[1], row[2]));
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getDataMultipleD()
	 */
	@Override
	public List<? extends GraphData> getDataMultipleD() {
		return this.getDataMultipleD(new HashMap<String, String>());
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getDataMultipleD(java.util.Map)
	 */
	@Override
	public List<? extends GraphData> getDataMultipleD(Map<String, String> parameters) {
		List<GraphDataMultipleD> resultList = new ArrayList<GraphDataMultipleD>();
		for(String[] row: dataRows) {
			resultList.add(new GraphDataMultipleD(row));
		}
		return resultList;
	}
}
