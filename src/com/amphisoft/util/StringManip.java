/*
epub2pdf, version 0.3 - Copyright 2010 Brendan C. LeFebvre

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
package com.amphisoft.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManip {
    public static boolean nothingButSpaces(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    public static String changeLineBreaksToSpaces(String s1) {
        StringBuilder sB = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            if (c == '\n')
                sB.append(' ');
            else if (c == '\r') {
                //no-op
            } else
                sB.append(c);
        }
        return sB.toString();

    }

    public static String removeLineBreaks(String s1) {
        StringBuilder sB = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            if (c != '\n')
                sB.append(c);
        }
        return sB.toString();
    }

    public static String[] parseSpecifiedLength(String s) {
        s = s.replace(" ","");
        s = s.toLowerCase();
        Pattern p = Pattern.compile("([0-9.]+)([a-z]*)");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        if (!b)
            return null;
        else
            return new String[] {
                       m.group(1),m.group(2)
                   };
    }

    public static void main(String[] args) {
        for (String s : parseSpecifiedLength("12.333pt")) {
            System.out.print(":" + s);
        }
        System.out.println(":");
        for (String s : parseSpecifiedLength("12.5")) {
            System.out.print(":" + s);
        }
        System.out.println(":");
        parseSpecifiedLength("12");
    }
}
