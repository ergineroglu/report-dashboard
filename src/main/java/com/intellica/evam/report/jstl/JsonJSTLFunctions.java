package com.intellica.evam.report.jstl;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Author: eeroglu
 * Date: 07 Mar 2014 15:45:20
 * Package: com.intellica.evam.report.jstl
 *
 */
public class JsonJSTLFunctions {

	// Inner class for json conversion using jackson
	private static class JacksonObjectMapper extends ObjectMapper {	
		public JacksonObjectMapper() {
			configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		}
	}
	
	public static String toJson(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		JacksonObjectMapper mapper = new JacksonObjectMapper();
		return mapper.writeValueAsString(object);
	}	
}
