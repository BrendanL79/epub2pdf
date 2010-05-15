/*
epub2pdf, version 0.5 - Copyright 2010 Brendan C. LeFebvre

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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * Because I'm sick of typing "System.out.println".
 * Inspired by a similar class in Bruce Eckel's "Thinking in Java" books.
 * 'import static' for easy console output.
 *
 * @author brendanl
 */
public class Print {

    public static void print(String s) {
        System.out.print(s);
    }

    public static void print(char[] cs) {
        System.out.print(cs);
    }

    public static void print(Object o) {
        String s = o.toString();
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void println(char[] cs) {
        System.out.println(cs);
    }

    public static void println(Object o) {
        String s = o.toString();
        System.out.println(s);
    }

    public static void println() {
        System.out.println();
    }

    public static void printf(String s, Object... args) {
        System.out.printf(s, args);
    }

    public static void printf(Locale L, String s, Object... args) {
        System.out.printf(L, s, args);
    }

    public static void printerr(String s) {
        System.err.print(s);
    }

    public static void printerr(char[] cs) {
        System.err.print(cs);
    }

    public static void printerr(Object o) {
        String s = o.toString();
        System.err.print(s);
    }

    public static void printlnerr(String s) {
        System.err.println(s);
    }

    public static void printlnerr(char[] cs) {
        System.err.println(cs);
    }

    public static void printlnerr(Object o) {
        String s = o.toString();
        System.err.println(s);
    }

    public static void printlnerr() {
        System.err.println();
    }

    public static String printHoursAndMinutes(Date d) {

        TimeZone gmtTZ = TimeZone.getTimeZone("Etc/UTC");
        Calendar cal = Calendar.getInstance(gmtTZ);

        long dMillis = d.getTime();

        cal.setTimeInMillis(dMillis);

        int hours 	= cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        return timeString(hours,minutes);
    }

    public static String timeString(int hours, int minutes) {
        String s = "";
        if (hours>=0 && hours <=23 && minutes>=0 && minutes<=59) {
            if (hours<10) {
                s += "0";
            }
            s += hours + ":";
            if (minutes<10) {
                s += "0";
            }
            s += minutes;
        } else {
            s += "XX:XX";
        }

        return s;
    }

    // Initialization code for gmtString helper objects
    static final TimeZone utc = TimeZone.getTimeZone("Etc/UTC");
    static DateFormat dateFormatter = DateFormat.getDateInstance();
    static {
        dateFormatter.setTimeZone(utc);
    }

    public static String gmtString(Date d) {
        String s = dateFormatter.format(d);
        return s;
    }

    /**
     * Adapted from example in Ivor Horton's "Beginning Java 2"
     * @param docType DOM DocumentType object
     * @return XML DOCTYPE declaration
     */
    public static String getDoctypeString(DocumentType docType) {

        final char QUOTE = '\"';
        String name = docType.getName();
        String sysId = docType.getSystemId();
        String publicId = docType.getPublicId();
        String internalSubset = docType.getInternalSubset();


        StringBuilder sB = new StringBuilder();
        sB.append("<!DOCTYPE ");
        sB.append(name);

        if (publicId != null) {
            sB.append(" PUBLIC ");
            sB.append(QUOTE);
            sB.append(publicId);
            sB.append(QUOTE);
        }
        if (sysId != null) {
            if (publicId == null)
                sB.append(" SYSTEM");
            sB.append(' ');
            sB.append(QUOTE);
            sB.append(sysId);
            sB.append(QUOTE);
        }


        if (internalSubset != null) {
            sB.append('[');
            sB.append(internalSubset);
            sB.append(']');
        }

        sB.append('>');

        return sB.toString();
    }

    public static void printDOMDocument(Document doc) {
        try {
            String doctypeDecl = "";
            DocumentType doctype = doc.getDoctype();
            if (doctype != null) {
                doctypeDecl = getDoctypeString(doctype);
            }
            System.out.println(doctypeDecl);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result2 = new StreamResult(System.out);
            transformer.transform(source, result2);
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
