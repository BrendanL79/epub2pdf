/*
epub2pdf, version 0.4 - Copyright 2010 Brendan C. LeFebvre

This file is part of epub2pdf.

epub2pdf is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epub2pdf is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epub2pdf.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.amphisoft.epub2pdf.style;

import org.w3c.css.sac.LexicalUnit;
/**
 * Provides a more friendly reference to the CSS lexical unit types defined in SAC.
 * In addition to the short values found in the original API, each selector type
 * has a human-readable string associated with it.
 * @author brendanl
 *
 */
public enum SacLexicalUnit {
    /**
     * ,
     */
    SAC_OPERATOR_COMMA	(0,""),
    /**
     * +
     */
    SAC_OPERATOR_PLUS		(1,""),
    /**
     * -
     */
    SAC_OPERATOR_MINUS	(2,""),
    /**
     * *
     */
    SAC_OPERATOR_MULTIPLY	(3,""),
    /**
     * /
     */
    SAC_OPERATOR_SLASH	(4,""),
    /**
     * %
     */
    SAC_OPERATOR_MOD		(5,""),
    /**
     * ^
     */
    SAC_OPERATOR_EXP		(6,""),
    /**
     * <
     */
    SAC_OPERATOR_LT		(7,""),
    /**
     * >
     */
    SAC_OPERATOR_GT		(8,""),
    /**
     * <=
     */
    SAC_OPERATOR_LE		(9,""),
    /**
     * >=
     */
    SAC_OPERATOR_GE		(10,""),
    /**
     * ~
     */
    SAC_OPERATOR_TILDE	(11,""),

    /**
     * identifier <code>inherit</code>.
     */
    SAC_INHERIT		(12,""),
    /**
     * Integers.
     * @see LexicalUnit#getIntegerValue
     */
    SAC_INTEGER		(13,""),
    /**
     * reals.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_REAL		        (14,""),
    /**
     * Relative length<code>em</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_EM		(15,""),
    /**
     * Relative length<code>ex</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_EX		(16,""),
    /**
     * Relative length <code>px</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_PIXEL		(17,""),
    /**
     * Absolute length <code>in</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_INCH		(18,""),
    /**
     * Absolute length <code>cm</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_CENTIMETER	(19,""),
    /**
     * Absolute length <code>mm</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_MILLIMETER	(20,""),
    /**
     * Absolute length <code>pt</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_POINT		(21,""),
    /**
     * Absolute length <code>pc</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_PICA		(22,""),
    /**
     * Percentage.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_PERCENTAGE		(23,""),
    /**
     * URI: <code>uri(...)</code>.
     * @see LexicalUnit#getStringValue
     */
    SAC_URI		        (24,""),
    /**
     * function <code>counter</code>.
     * @see LexicalUnit#getFunctionName
     * @see LexicalUnit#getParameters
     */
    SAC_COUNTER_FUNCTION	(25,""),
    /**
     * function <code>counters</code>.
     * @see LexicalUnit#getFunctionName
     * @see LexicalUnit#getParameters
     */
    SAC_COUNTERS_FUNCTION	(26,""),
    /**
     * RGB Colors.
     * <code>rgb(0, 0, 0)</code> and <code>#000</code>
     * @see LexicalUnit#getFunctionName
     * @see LexicalUnit#getParameters
     */
    SAC_RGBCOLOR		(27,""),
    /**
     * Angle <code>deg</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_DEGREE		(28,""),
    /**
     * Angle <code>grad</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_GRADIAN		(29,""),
    /**
     * Angle <code>rad</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_RADIAN		(30,""),
    /**
     * Time <code>ms</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_MILLISECOND		(31,""),
    /**
     * Time <code>s</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_SECOND		(32,""),
    /**
     * Frequency <code>Hz</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_HERTZ		        (33,""),
    /**
     * Frequency <code>kHz</code>.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_KILOHERTZ		(34,""),

    /**
     * any identifier except <code>inherit</code>.
     * @see LexicalUnit#getStringValue
     */
    SAC_IDENT		        (35,""),
    /**
     * A string.
     * @see LexicalUnit#getStringValue
     */
    SAC_STRING_VALUE		(36,""),
    /**
     * Attribute: <code>attr(...)</code>.
     * @see LexicalUnit#getStringValue
     */
    SAC_ATTR		        (37,""),
    /**
     * function <code>rect</code>.
     * @see LexicalUnit#getFunctionName
     * @see LexicalUnit#getParameters
     */
    SAC_RECT_FUNCTION		(38,""),
    /**
     * A unicode range. @@TO BE DEFINED
     */
    SAC_UNICODERANGE		(39,""),

    /**
     * sub expressions
     * <code>(a)</code> <code>(a + b)</code> <code>(normal/none)</code>
     * @see LexicalUnit#getSubValues
     */
    SAC_SUB_EXPRESSION	(40,""),

    /**
     * unknown function.
     * @see LexicalUnit#getFunctionName
     * @see LexicalUnit#getParameters
     */
    SAC_FUNCTION		(41,""),
    /**
     * unknown dimension.
     * @see LexicalUnit#getFloatValue
     * @see LexicalUnit#getDimensionUnitText
     */
    SAC_DIMENSION		(42,"");

    public final short id;
    public final String hrName;

    SacLexicalUnit(int i, String n) {
        id = (short) i;
        hrName = n;
    }

    @Override
    public String toString() {
        return hrName;
    }

    public static String lookupName(short idQuery) {
        for (SacLexicalUnit s : SacLexicalUnit.values()) {
            if (s.id == idQuery) {
                //return s.hrName;
                return s.name();
            }
        }
        return null;
    }
}
