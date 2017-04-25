package ddc.commons.jdbc.schema;

import java.util.ArrayList;

public class LiteDbColumns extends ArrayList<LiteDbColumn> {
	private static final long serialVersionUID = -8802484989478960541L;
	
	@Override
    public boolean add(LiteDbColumn column) {
		boolean result = super.add(column);
		reindex();
        return result;
    }
	
	@Override
    public void add(int index, LiteDbColumn column) {
		super.add(index, column);
		reindex();
    }

	private void reindex() {
		int counter = 0;
		for (LiteDbColumn c : this) {
			counter++;
			c.setIndex(counter);
		}
	}
}
