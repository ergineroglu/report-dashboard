package com.intellica.evam.report.data.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellica.evam.report.dao.RDBMSDao;
import com.intellica.evam.report.data.GraphData;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 09:19:04
 * Package: com.intellica.evam.report.model
 *
 */
public class DatabaseDataSource implements DataSource {

	private String queryTemplate;
	private RDBMSDao rdbmsDAO;
	
	/**
	 * 
	 */
	public DatabaseDataSource(String queryTemplate) {
		this.queryTemplate = queryTemplate;
		this.rdbmsDAO = new RDBMSDao();
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
		return this.rdbmsDAO.<String>executeQuery1D(this.queryTemplate, parameters);
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
		return this.rdbmsDAO.<String, String>executeQuery2D(this.queryTemplate, parameters);
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData3D()
	 */
	@Override
	public List<? extends GraphData> getData3D() {
		return this.getData3D(new HashMap<String, String>());
	}

	/* (non-Javadoc)
	 * @see com.intellica.evam.report.model.DataSource#getData2D(java.util.Map)
	 */
	@Override
	public List<? extends GraphData> getData3D(Map<String, String> parameters) {
		return this.rdbmsDAO.<String, String, String>executeQuery3D(this.queryTemplate, parameters);
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
		return this.rdbmsDAO.executeQueryMultipleD(this.queryTemplate, parameters);
	}
}
