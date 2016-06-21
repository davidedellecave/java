package ddc.core.db.berkleydb;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class Entity1 {

	@PrimaryKey
	private String id;

	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private String fk_id;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFk_id() {
		return fk_id;
	}

	public void setFk_id(String fk_id) {
		this.fk_id = fk_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
