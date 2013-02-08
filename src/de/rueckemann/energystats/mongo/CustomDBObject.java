package de.rueckemann.energystats.mongo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.mongodb.BasicDBObject;

public class CustomDBObject extends BasicDBObject {

	private static final long serialVersionUID = 5360742505470649245L;
	
	public CustomDBObject(Object pojo) {
		super();
		for(Method method : pojo.getClass().getMethods()) {
			if(method.getName().startsWith("get")) {
				String property = method.getName().substring(3);
				try {
					Object value = new PropertyDescriptor(property, pojo.getClass()).getReadMethod().invoke(pojo);
					System.out.println("adding: { " + property + " : " + value +" }");
					append(property, value);
				} catch (IntrospectionException ex) {
					System.out.println(ex.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
