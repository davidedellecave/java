package ddc.task;

import org.apache.commons.lang3.StringUtils;

import ddc.util.BTree;
import ddc.util.BTreeListener;

public class TaskSchema extends BTree<Class<? extends Task>> {
		
	public TaskSchema(Class<? extends Task> clazz) {
		super(clazz);
	}
	
	public String getName() {
		return this.getValue().getSimpleName();
	}
	
	public Class<? extends Task> getTaskClass() {
		return this.getValue();
	}
	
	public TaskSchema getChild(Class<? extends Task> clazz) {
		return (TaskSchema)this.search(clazz);
	}
	
	public void setOnSuccess(Class<? extends Task> clazz) {
		this.setLeft(new TaskSchema(clazz));
	}

	public void pathSuccess(Class<? extends Task>[] clazzList) {
		TaskSchema current = this;
		for (Class<? extends Task> t : clazzList) {
			current = current.nextSuccess(t);
		}
	}

	public void pathFail(Class<? extends Task>[] clazzList) {
		TaskSchema current = this;
		for (Class<? extends Task> t : clazzList) {
			current = current.nextFail(t);
		}
	}

	public TaskSchema nextSuccess(Class<? extends Task> clazz) {
		TaskSchema t = new TaskSchema(clazz);
		this.setLeft(t);
		return t;
	}
	
	public TaskSchema nextFail(Class<? extends Task> clazz) {
		TaskSchema t = new TaskSchema(clazz);
		this.setRight(t);
		return t;
	}
	
	public void next(Class<? extends Task> onSuccessClass, Class<? extends Task> onFailClass) {
		this.setLeft(new TaskSchema(onSuccessClass));
		this.setRight(new TaskSchema(onFailClass));
	}
	
	public void setOnFail(Class<? extends Task> clazz) {
		this.setRight(new TaskSchema(clazz));
	}

	public TaskSchema getOnSuccess() {
		return (TaskSchema)this.getLeft();
	}

	public TaskSchema getOnFail() {
		return (TaskSchema)this.getRight();
	}
	

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();		
		b.append("task:[" + this.getName() + "] ");
		b.append(getOnSuccess() !=null ? "onSuccess:[" + getOnSuccess().getName() + "] " : "");
		b.append(getOnFail() !=null ? "onFail:[" + getOnFail().getName() + "] " : "");
		b.append(getParent() !=null ? "parent:[" + ((TaskSchema)getParent()).getName() + "] " : "");
		return b.toString();
	}
	
	public String toSchemaString() {
		String s = "";
		s += "\n---------------------- Task Schema ---------------------\n";
		s += schemaString(this, "");
		s += "\n--------------------------------------------------------";
		return s;
	}

	private String PREFIX = "|(x)-> ";

	private String schemaString(TaskSchema node, String space) {
		String s = node.getName();
		if (space.length() == 0)
			space += "!" + StringUtils.repeat("-", s.length() - 1);
		else
			space += "|" + StringUtils.repeat("-", s.length() - 1 + PREFIX.length());
		if (node.getOnSuccess() != null)
			s += "\n" + space + "|(s)-> " + schemaString(node.getOnSuccess(), space);
		if (node.getOnFail() != null)
			s += "\n" + space + "|(f)-> " + schemaString(node.getOnFail(), space);
		return s;
	}
	
	public void print() {
		System.out.println("\n---------------------- Tree Node List ---------------------");		
		this.visit(new BTreeListener<Class<? extends Task>>() {			
			@Override
			public boolean visit(BTree<Class<? extends Task>> node) {
				System.out.println(node.toString());
				return true;
			}
		});
	}
}
