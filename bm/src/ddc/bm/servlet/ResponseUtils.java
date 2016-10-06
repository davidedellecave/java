package ddc.bm.servlet;

import java.util.Map;

public class ResponseUtils {
	private String leftName = "";
	private String rightName = ":";
	private String leftValue = "[";
	private String rightValue = "]";
	private String separator = " ";

	public ResponseUtils(String leftName, String rightName, String leftValue, String rightValue, String separator) {
		super();
		this.leftName = leftName;
		this.rightName = rightName;
		this.leftValue = leftValue;
		this.rightValue = rightValue;
		this.separator = separator;
	}

	public String toString(Map<String, String> props) {
		StringBuffer buff = new StringBuffer(256);
		int counter = props.size();
		for (Map.Entry<String, String> item : props.entrySet()) {
			buff.append(leftName)
			.append(item.getKey())
			.append(rightName)
			.append(leftValue)
			.append(item.getValue())
			.append(rightValue);
			counter--;
			if (counter>0) buff.append(separator);
		}
		return buff.toString();
	}
}
