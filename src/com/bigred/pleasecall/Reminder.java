package com.bigred.pleasecall;

public class Reminder {
	
	private long id;
	private String uri;
	private int frequency;
	private int enabled;
	private int SMSenabled;
	private String description;
	
	public Reminder(){
		
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setUri(String uri){
		this.uri = uri;
	}
	
	public void setFrequency(int frequency){
		this.frequency = frequency;
	}
	
	public void setSMSEnabled(int enabled){
		this.SMSenabled = enabled;
	}
	
	public void setEnabled(int enabled){
		this.enabled = enabled;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public long getId() {
		return id;
	}
	
	public String getUri(){
		return uri;
	}
	
	public int getFrequency(){
		return frequency;
	}
	
	public int getEnabled(){
		return enabled;
	}
	
	public int getSMSEnabled(){
		return SMSenabled;
	}
	
	public String getDescription(){
		return description;
	}
}
