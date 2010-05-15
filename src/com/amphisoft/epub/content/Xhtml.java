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
package com.amphisoft.epub.content;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.w3c.dom.Document;

/**
 * An XHTML file that is part of the "content" component of an EPUB.
 * XHTML files used as EPUB content must be XHTML-1.1 compliant.
 * @author brendanl
 *
 */
public class Xhtml implements ContentFile {
    private Document doc;
    public Xhtml(String s) {
        this(new File(s));
    }
    public Xhtml(File f) {
        DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dB = dBF.newDocumentBuilder();
            CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
            ResolvingXMLReader rxR = new ResolvingXMLReader();
            rxR.getCatalog().parseCatalog("etc/xhtml/catalog.xml");
            dB.setEntityResolver(rxR);
            doc = dB.parse(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Document getDOMDocument() {
        return doc;
    }

    public static void main(String[] args) {
        Xhtml xH = new Xhtml(
            new File("data/pg-prufrock/1459/0.html"));
        System.out.println(xH.toString());
    }
}
