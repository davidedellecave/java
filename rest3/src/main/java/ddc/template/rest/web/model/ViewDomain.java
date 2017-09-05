package ddc.template.rest.web.model;

public class ViewDomain {

	String name;
	String text;

	public ViewDomain(String name, String text) {
		this.name = name;
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

}