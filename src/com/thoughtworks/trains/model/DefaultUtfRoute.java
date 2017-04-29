package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public class DefaultUtfRoute implements UtfRoute {

	protected String id;
	protected UtfTown startTown;
	protected UtfTown endTown;
	protected int distance;
	
	public DefaultUtfRoute(String id, UtfTown startTown, UtfTown endTown, int distance) throws NullTownException {
		this.id = id;
		this.startTown = startTown;
		this.endTown = endTown;
		this.distance = distance;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public UtfTown getStartTown() {
		return startTown;
	}

	@Override
	public UtfTown getEndTown() {
		return endTown;
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
		
		DefaultUtfRoute that = (DefaultUtfRoute)obj;

		if (!that.getId().equals(this.getId())) return false;
		if (that.getDistance() != this.getDistance()) return false;
		if (!that.getStartTown().equals(this.getStartTown())) return false;
		if (!that.getEndTown().equals(this.getEndTown())) return false;
		
		return true;
	}
	
}
