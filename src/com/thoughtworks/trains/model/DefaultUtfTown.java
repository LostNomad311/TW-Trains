package com.thoughtworks.trains.model;

public class DefaultUtfTown implements UtfTown {

	private String id;
	
	public DefaultUtfTown(String id) {
		this.id = id;
	}

	@Override
	public String getUtfId() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		DefaultUtfTown that = (DefaultUtfTown) obj;
		if (!that.id.equals(this.id)) return false;
		
		return true;
	}

}
