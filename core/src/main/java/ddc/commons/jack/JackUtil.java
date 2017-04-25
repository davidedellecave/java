package ddc.commons.jack;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JackUtil {

	public static String prettify(JsonNode node) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (mapper.writerWithDefaultPrettyPrinter()).writeValueAsString(node);
	}
	
	public static String prettify(Object node) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (mapper.writerWithDefaultPrettyPrinter()).writeValueAsString(node);
	}
	
	public static JsonNode parse(String json) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.valueToTree(json);
	}

	public static JsonNode empty() {
		return parse("{}");
	}
}
