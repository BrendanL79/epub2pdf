package com.amphisoft.logging;

import java.io.PrintStream;
import java.util.List;

public abstract class PrintStreamLogger implements LogEventSubscriber {
	
	protected final PrintStream _destination;
	
	public PrintStreamLogger(PrintStream destination) {
		_destination = destination;
	}
	
	public String buildOutput(LogEvent ev) {
		StringBuilder outSB = new StringBuilder("");

		List<String> propNames = ev.getPropNames();

		if(propNames.size()>0) {
			StringBuilder propSB = new StringBuilder("");
			for (String propName : ev.getPropNames()) {
				if (propSB.length()>0) {
					propSB.append("; ");
				}
				propSB.append(propName);
				propSB.append(":");
				propSB.append(ev.getPropertyValue(propName));
			}
			outSB.append(propSB.toString());
		}
		
		return outSB.toString();
	}
	
	@Override
	public void notify(LogEvent ev) {
		_destination.println(buildOutput(ev));
	}
}
