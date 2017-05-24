package com.thoughtworks.trains.model;

import java.util.EnumSet;

public abstract class TripInfoCommand implements Command {

	protected EnumSet<TripInfoOption> options;
	protected UtfTown startTown;
	protected UtfTown endTown;

}
