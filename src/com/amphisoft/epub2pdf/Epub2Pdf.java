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
package com.amphisoft.epub2pdf;

public class Epub2Pdf {
    public static final String VERSION = "0.5";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        System.err.println("epub2pdf v" + VERSION + " - Copyright (C) 2010  Brendan C. Lefebvre");
        System.err.println("This program comes with ABSOLUTELY NO WARRANTY.");
        System.err.println("This is free software, and you are welcome to redistribute it");
        System.err.println("under certain conditions.");
        System.err.println("See file 'COPYING' for full license and warranty details.");
        System.err.println("");

        Converter.main(args);
    }
}
