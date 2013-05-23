package com.thoughtworks.trains.model;

public class DefaultTown implements Town {

	private char id;
	
	public DefaultTown(char id) {
		this.id = id;
	}
	
	public char getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		DefaultTown that = (DefaultTown) obj;
		if (that.id != this.id) return false;
		
		return true;
	}
	
}
