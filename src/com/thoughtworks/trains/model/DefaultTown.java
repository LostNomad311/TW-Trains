package com.thoughtworks.trains.model;

public class DefaultTown implements Town {

	private String id;
	
	public DefaultTown(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		DefaultTown that = (DefaultTown) obj;
		if (!that.id.equals(this.id)) return false;
		
		return true;
	}
	
}
