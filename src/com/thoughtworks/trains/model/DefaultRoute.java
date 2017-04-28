package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public class DefaultRoute implements Route {
	
	protected int distance;
	protected Town startTown;
	protected Town endTown;
	protected String id;

	public DefaultRoute(String id, Town startTown, Town endTown, int distance) throws NullTownException {
		if (startTown == null || endTown == null) {
			throw new NullTownException();
		}
		
		this.startTown = startTown;
		this.endTown = endTown;
		this.distance = distance;
		this.id = id;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public Town getStartTown() {
		return startTown;
	}

	@Override
	public Town getEndTown() {
		return endTown;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "" + startTown.getId() + endTown.getId() + distance;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)  return false;
		if (obj == this) return true;
		if (!obj.getClass().equals(this.getClass())) return false;
		
		DefaultRoute that = (DefaultRoute)obj;

		if (!that.getId().equals(this.getId())) return false;
		if (that.getDistance() != this.getDistance()) return false;
		if (!that.getStartTown().equals(this.getStartTown())) return false;
		if (!that.getEndTown().equals(this.getEndTown())) return false;
		
		return true;
	}
	
}
