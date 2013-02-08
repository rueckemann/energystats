package de.rueckemann.energystats.mongo;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

import de.rueckemann.energystats.domain.EnergyMeter;

public class MongoDB {

	private static final String COLLECTION_ENERGY_METER = "EnergyMeter";
	private static MongoClient mongoClient;
	
	private static synchronized MongoClient getMongoClient() throws UnknownHostException {
		if(mongoClient == null) {
			mongoClient = new MongoClient( "localhost" , 27017 );
		}
		return mongoClient;
	}
	
	private static DB getDbConnection() {
		try {
			return getMongoClient().getDB("energystats");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
//	public static WriteResult insertEnergyMeter(EnergyMeter energyMeter) {
//		return insertObject(COLLECTION_ENERGY_METER, energyMeter);
//	}
//	
//	public static WriteResult insertObject(String collection, Object value) {
//		DBCollection coll = getDbConnection().getCollection(collection);
//		DBObject doc = new CustomDBObject(value);
//		return coll.insert(doc);
//	}
	
	public static Collection<EnergyMeter> getEnergyMeter() {
		DBCollection coll = getDbConnection().getCollection(COLLECTION_ENERGY_METER);
		DBCursor cursor = coll.find();
		Collection<EnergyMeter>result = new ArrayList<EnergyMeter>();
		try {
			   while(cursor.hasNext()) {
				   DBObject next = cursor.next();				   
				   result.add((EnergyMeter)convertDBObject(EnergyMeter.class, next));
			   }
			} finally {
			   cursor.close();
			}
		return result;
	}
	
	private static Object convertDBObject(Class<?>clazz, DBObject obj) {
		Object result = null;	
		try {
			result = clazz.newInstance();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if(method.getName().startsWith("set")) {
					final String property = method.getName().substring(3);
					if(obj.containsField(property)) {
						Object value = obj.get(property);
						System.out.println("calling method " + method.getName() + " with value: " + value);
						method.invoke(result, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int saveObject(Object bean) throws DBException {
		int result = 0;
		DBCollection coll = getDbConnection().getCollection(COLLECTION_ENERGY_METER);
		DBObject doc = new CustomDBObject(bean);
		WriteResult writeResult = coll.save(doc);
		if(writeResult != null) {
			if(writeResult.getError() != null) {
				throw new DBException(writeResult.getError());				
			} 
			result = writeResult.getN();
		}
		return result;
	}
	
	public static int removeObject(Object bean) throws DBException {
		int result = 0;
		DBCollection coll = getDbConnection().getCollection(COLLECTION_ENERGY_METER);
		DBObject doc = new CustomDBObject(bean);
		WriteResult writeResult = coll.remove(doc);
		if(writeResult != null) {
			if(writeResult.getError() != null) {
				throw new DBException(writeResult.getError());				
			} 
			result = writeResult.getN();
		}
		return result;
	}
}
