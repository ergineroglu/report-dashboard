package com.intellica.evam.report.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.intellica.evam.report.model.GraphData2D;
import com.intellica.evam.report.model.GraphData3D;

/**
 * Author: eeroglu
 * Date: 18 Şub 2014 16:13:43
 * Package: com.intellica.evam.report.dao
 *
 */
@Repository("rdbmsDao")
public class RDBMSDao {

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
    			//logger.error("Cannot rollback transaction", rbe);
    			// TODO: log exception
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
	public <K extends Serializable, V extends Serializable> List<GraphData2D<K, V>> executeQuery2D(String queryString) {
		return this.executeQuery2D(queryString, new HashMap<String, String>());
	}
	
	@SuppressWarnings("unchecked")
	public <K extends Serializable, V extends Serializable> List<GraphData2D<K, V>> executeQuery2D(
			String queryString, Map<String, String> queryParameters) {
		// get generic results
		List<Object[]> genericResultList = this.executeQuery(queryString, queryParameters);
		// map results
		List<GraphData2D<K, V>> resultList = new ArrayList<GraphData2D<K, V>>();
		for(Object[] rowIter: genericResultList) {
			if(rowIter.length >= 2) {
				resultList.add(new GraphData2D<K, V>((K) rowIter[0], (V) rowIter[1]));
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
}
