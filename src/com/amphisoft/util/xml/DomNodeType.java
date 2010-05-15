package com.amphisoft.util.xml;

import java.util.Map;
import java.util.TreeMap;

public enum DomNodeType {
	ELEMENT_NODE(1),
	ATTRIBUTE_NODE(2),
	TEXT_NODE(3),
	CDATA_SECTION_NODE(4),
	ENTITY_REFERENCE_NODE(5),
	ENTITY_NODE(6),
	PROCESSING_INSTRUCTION_NODE(7),
	COMMENT_NODE(8),
	DOCUMENT_NODE(9),
	DOCUMENT_TYPE_NODE(10),
	DOCUMENT_FRAGMENT_NODE(11),
	NOTATION_NODE(12);
	// 
	private short type_id;
	private DomNodeType(int id) {
		if(id > Short.MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		type_id = (short) id;
		TypeMap.put(type_id, this);
	}
	public short getId() {
		return type_id;
	}
	public static String getName(short id) {
		DomNodeType d = TypeMap.get(id);
		return d.name();
	}
	
	private static class TypeMap {
		private static Map<Short,DomNodeType> map = 
			new TreeMap<Short, DomNodeType>();
		public static DomNodeType put(short s, DomNodeType d) {
			return map.put(s,d);
		}
		public static DomNodeType get(short s) {
			return map.get(s);
		}
	}
}
