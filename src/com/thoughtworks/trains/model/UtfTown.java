package com.thoughtworks.trains.model;

public class UtfTown implements Town {

	private String id;
	
	public UtfTown(String id) {
		this.id = id;
	}

	@Override
	public char getId() {
		throw new UnsupportedOperationException();
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
		UtfTown that = (UtfTown) obj;
		if (!that.id.equals(this.id)) return false;
		
		return true;
	}

}
