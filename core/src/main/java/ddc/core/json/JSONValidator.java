package ddc.core.json;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

public class JSONValidator {
	private String jsonSchema=null;
	private Schema schema = null;
	
	public JSONValidator(String jsonSchema) {
		this.jsonSchema = jsonSchema;
	}
		
	public JSONObject validate(String jsonData, boolean schemaValidation) throws JSONValidationException {
		try {
			JSONObject json = new JSONObject(jsonData);
			if (schemaValidation) validate(json);
			return json;
		} catch (RuntimeException e) {
			throw new JSONValidationException(e);
		}
	}
	
	public void validate(JSONObject json) throws JSONValidationException {
		try {
			if (schema==null) {
				JSONObject rawSchema = new JSONObject(jsonSchema);
				schema = SchemaLoader.load(rawSchema);	
			}
			schema.validate(json);			
		} catch (RuntimeException e) {
			throw new JSONValidationException(e);
		}
	}
	
//	private void test() throws IOException {
//		try (InputStream schemaStream = getClass().getResourceAsStream("schema.json")) {
//			JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
//			Schema schema = SchemaLoader.load(rawSchema);
//
//			InputStream dataStream = getClass().getResourceAsStream("data.json");
//			JSONObject data = new JSONObject(new JSONTokener(dataStream));
//			try {
//				schema.validate(data);
//				System.out.println("Json is valid");
//			} catch (RuntimeException e) {
//				System.out.println("Json is not valid:" + e.getMessage());
//			}
//		}
//	}
}
