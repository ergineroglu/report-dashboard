package com.intellica.evam.report.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.intellica.evam.report.model.GraphData1D;
import com.intellica.evam.report.model.GraphData2D;
import com.intellica.evam.report.model.GraphData3D;
import com.intellica.evam.report.model.GraphDataMultipleD;

/**
 * Author: eeroglu
 * Date: 18 Şub 2014 16:13:43
 * Package: com.intellica.evam.report.dao
 *
 */
@Repository("rdbmsDao")
public class RDBMSDao {

	private static final Logger logger = LoggerFactory.getLogger(RDBMSDao.class);
	
	// execute query
	public List<Object[]> executeQuery(String queryString) {
		return this.executeQuery(queryString, new HashMap<String, String>());
	}
	
	public List<Object[]> executeQuery(String queryString, Map<String, String> queryParameters) {
		Session session = null;
    	Transaction tx = null;
 
    	try {
    		// get session and start transaction
    		session = HibernateConfiguration.getSessionFactory().openSession();
    		tx = session.beginTransaction();    		
 
    		// create hibernate query and set parameters
    		final org.hibernate.Query query = session.createSQLQuery(queryString);
    		for(Map.Entry<String, String> entry : queryParameters.entrySet()) {
    			query.setParameter(entry.getKey(), entry.getValue());
    		}
    		
    		// execute query
    		@SuppressWarnings("unchecked")
			List<Object[]> resultList = query.list(); 
    		
    		// commit transaction
    		tx.commit();
    		
    		// return saved query
    		return resultList;
    	}
    	catch(RuntimeException e){
    		// try to rollback
    		try{
    			tx.rollback();
    		}
    		catch(RuntimeException rbe){
    			logger.error("Cannot rollback transaction", rbe);
    		}
    		
    		// rethrow exception
    		throw e;
    	}
    	finally{
    		// close session
    		if(session!=null){
    			session.close();
    		}
    	}
	}
	
	// Graph2D getters
	public <X extends Serializable> List<GraphData1D<X>> executeQuery1D(String queryString) {
		return this.executeQuery1D(queryString, new HashMap<String, String>());
	}
	
	@SuppressWarnings("unchecked")
	public <X extends Serializable> List<GraphData1D<X>> executeQuery1D(
			String queryString, Map<String, String> queryParameters) {
		// get generic results
		List<Object[]> genericResultList = this.executeQuery(queryString, queryParameters);
		// map results
		List<GraphData1D<X>> resultList = new ArrayList<GraphData1D<X>>();
		for(Object[] rowIter: genericResultList) {
			if(rowIter.length >= 1) {
				resultList.add(new GraphData1D<X>((X) rowIter[0]));
			}
			else {
				// TODO: handle row with errors
			}
		}
		
		return resultList;
	}
		
	
	// Graph2D getters
	public <X extends Serializable, Y extends Serializable> List<GraphData2D<X, Y>> executeQuery2D(String queryString) {
		return this.executeQuery2D(queryString, new HashMap<String, String>());
	}
	
	@SuppressWarnings("unchecked")
	public <X extends Serializable, Y extends Serializable> List<GraphData2D<X, Y>> executeQuery2D(
			String queryString, Map<String, String> queryParameters) {
		// get generic results
		List<Object[]> genericResultList = this.executeQuery(queryString, queryParameters);
		// map results
		List<GraphData2D<X, Y>> resultList = new ArrayList<GraphData2D<X, Y>>();
		for(Object[] rowIter: genericResultList) {
			if(rowIter.length >= 2) {
				resultList.add(new GraphData2D<X, Y>((X) rowIter[0], (Y) rowIter[1]));
			}
			else {
				// TODO: handle row with errors
			}
		}
		
		return resultList;
	}
	
	// Graph3D getters
	public <X extends Serializable, Y extends Serializable, Z extends Serializable> List<GraphData3D<X, Y, Z>> executeQuery3D(String queryString) {
		return this.executeQuery3D(queryString, new HashMap<String, String>());
	}
	
	@SuppressWarnings("unchecked")
	public <X extends Serializable, Y extends Serializable, Z extends Serializable> List<GraphData3D<X, Y, Z>> executeQuery3D(
			String queryString, Map<String, String> queryParameters) {
		// get generic results
		List<Object[]> genericResultList = this.executeQuery(queryString, queryParameters);
		// map results
		List<GraphData3D<X, Y, Z>> resultList = new ArrayList<GraphData3D<X, Y, Z>>();
		for(Object[] rowIter: genericResultList) {
			if(rowIter.length >= 2) {
				resultList.add(new GraphData3D<X, Y, Z>((X) rowIter[0], (Y) rowIter[1], (Z) rowIter[2]));
			}
			else {
				// TODO: handle row with errors
			}
		}
		
		return resultList;
	}
	
	// GraphMultipleD getters
	public List<GraphDataMultipleD> executeQueryMultipleD(String queryString) {
		return this.executeQueryMultipleD(queryString, new HashMap<String, String>());
	}
		
	public List<GraphDataMultipleD> executeQueryMultipleD(String queryString, Map<String, String> queryParameters) {
		// get generic results
		List<Object[]> genericResultList = this.executeQuery(queryString, queryParameters);
		// map results
		List<GraphDataMultipleD> resultList = new ArrayList<GraphDataMultipleD>();
		for(Object[] rowIter: genericResultList) {
			resultList.add(new GraphDataMultipleD(rowIter));			
		}
		
		return resultList;
	}
}
