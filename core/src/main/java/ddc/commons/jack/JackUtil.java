package ddc.commons.jack;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JackUtil<T> {

	public static String prettify(JsonNode node) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (mapper.writerWithDefaultPrettyPrinter()).writeValueAsString(node);
	}
	
	public static String convAsPrettifiedString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (mapper.writerWithDefaultPrettyPrinter()).writeValueAsString(obj);
	}
	
	public static String convAsString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
	
	public static JsonNode parse(String json) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.valueToTree(json);
	}

	public static JsonNode empty() {
		return parse("{}");
	}
	
	public List<T> parseList(String json, Class<T> elementClass) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<T> list = mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass));
		return list;
	}
}
