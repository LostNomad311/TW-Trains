package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public class UtfRoute extends DefaultRoute {

	public UtfRoute(String id, Town startTown, Town endTown, int distance) throws NullTownException {
		super(id, startTown, endTown, distance);
	}
	
	@Override
	public String toString() {
		return "" + startTown.getUtfId() + endTown.getUtfId() + distance;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)  return false;
		if (obj == this) return true;
		if (!obj.getClass().equals(this.getClass())) return false;
		
		UtfRoute that = (UtfRoute)obj;

		if (!that.getId().equals(this.getId())) return false;
		if (that.getDistance() != this.getDistance()) return false;
		if (!that.getStartTown().equals(this.getStartTown())) return false;
		if (!that.getEndTown().equals(this.getEndTown())) return false;
		
		return true;
	}
	
}
