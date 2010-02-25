package com.amphisoft.logging;

public enum Priority {
	ALL(Integer.MIN_VALUE),
	DEBUG(10000),
	INFO(20000),
	WARN(30000),
	ERROR(40000),
	FATAL(50000),
	OFF(Integer.MAX_VALUE),
;
	private final int _value;

	Priority(int value) {
		_value = value;
	}
	
	public int value() {
		return _value;
	}
	
}
