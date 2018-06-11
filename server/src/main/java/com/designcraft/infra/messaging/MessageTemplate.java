package com.designcraft.infra.messaging;

public class MessageTemplate {
	private String type;
	private Object value;
	
	public MessageTemplate(String type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	public Object getValue() {
		return value;
	}
}
