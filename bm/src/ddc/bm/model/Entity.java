package ddc.bm.model;

import java.util.UUID;

import ddc.util.Chronometer;

public class Entity {
	private UUID id;
	private long insert;
	private long update;
	private boolean trashed;

	public Entity() {
		id = UUID.randomUUID();
		insert = Chronometer.getNowMillis();
		update = insert;
		trashed = false;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public long getInsert() {
		return insert;
	}

	public void setInsert(long insert) {
		this.insert = insert;
	}

	public long getUpdate() {
		return update;
	}

	public void setUpdate(long update) {
		this.update = update;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

}
