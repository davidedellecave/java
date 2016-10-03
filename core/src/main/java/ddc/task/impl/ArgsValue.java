package ddc.task.impl;

import ddc.config.ConfigurationException;

public class ArgsValue {
	private String[] args;
	private Class<?> confClass;
	
	public ArgsValue(String[] args) {
		this.args=args;
	}
		
	public String getArgs(int index) throws ConfigurationException {
		if (args.length<=index) 
			throw new ConfigurationException("Input arg is expected index:[" + index + "]");
		return args[index];
	}
	
	public void setArgs(String[] args) {
		this.args = args;
	}

	public Class<?> getConfClass() {
		return confClass;
	}

	public void setConfClass(Class<?> confClass) {
		this.confClass = confClass;
	}
}
