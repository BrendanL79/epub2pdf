/*
epub2pdf, version 0.1 - Copyright 2010 Brendan C. LeFebvre

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
package com.amphisoft.epub.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The OCF container file component of an EPUB. <br />
 *
 * The container has exactly one variable piece of information: the path
 * to the OPF metadata file.
 *
 * @see Opf
 * @author brendanl
 *
 */
public class Container {
    /**
     * Path to the main, or "root", OPF metadata file, expressed relative to archive root.
     */
    public final String rootMetadataPath;

    /**
     *
     * @param metadataPath path to the main, or "root", OPF metadata file, expressed relative to archive root
     */
    public Container(String metadataPath) {
        rootMetadataPath = metadataPath;
    }

    public Container() {
        this("metadata.opf");
    }

    public String generateContents() {
        StringBuilder sB = new StringBuilder();
        sB.append(line1);
        sB.append(line2);
        sB.append(line3);
        sB.append(line4a + rootMetadataPath + line4b);
        sB.append(line5);
        sB.append(line6);
        return sB.toString();
    }

    public boolean outputToFile(String path) {
        try {
            FileWriter fW = new FileWriter(new File(path));
            fW.write(generateContents());
            fW.close();
            return true;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        }
    }

    private static final String line1  = "<?xml version=\"1.0\"?>\n";
    private static final String line2  = "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\n";
    private static final String line3  = "   <rootfiles>\n";
    private static final String line4a = "      <rootfile full-path=\"";
    private static final String line4b = "\" media-type=\"application/oebps-package+xml\"/>\n";
    private static final String line5  = "   </rootfiles>\n";
    private static final String line6  = "</container>\n";

    public static Container fromFile(String path) {
        try {
            BufferedReader fR = new BufferedReader(new FileReader(new File(path)));
            String line = fR.readLine();
            Container c = null;
            while (line != null) {
                if (line.contains("rootfile") && line.contains("full-path=\"")) {
                    int pathFirstIdx = line.lastIndexOf("full-path=\"") + "full-path=\"".length();
                    String tail = line.substring(pathFirstIdx);
                    int pathLastIdx = tail.indexOf("\"");
                    String rootPath = tail.substring(0, pathLastIdx);
                    //System.out.println("Root-Path: " + rootPath);
                    c = new Container(rootPath);
                }
                line = fR.readLine();
            }
            fR.close();
            return c;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "OCF: root-file = " + rootMetadataPath;
    }

    public static void main(String[] args) {
        Container c = new Container("content.opf");
        System.out.println(c.toString());
        c.outputToFile("out/container.xml");
        Container c2 = Container.fromFile("out/container.xml");
        System.out.println(c2.toString());
        System.out.println(c2.generateContents());
    }
}
