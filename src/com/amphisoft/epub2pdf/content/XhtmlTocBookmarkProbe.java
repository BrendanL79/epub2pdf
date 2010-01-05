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
package com.amphisoft.epub2pdf.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amphisoft.epub.content.XhtmlTags;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.html.SAXmyHtmlHandler;
import static com.amphisoft.util.Print.*;
/**
 * XHTML parser that simply notes the files and element id's encountered.
 * Intended to verify order of NCX TOC elements matches the order in which they will occur while parsing EPUB content files in the order specified by the OPF spine.
 * @author brendanl
 */
public class XhtmlTocBookmarkProbe extends SAXmyHtmlHandler {

    protected Document document;
    protected Stack<Element> stack = new Stack<Element>();;
    protected String currentTag = "";
    protected String previousTag = "";
    protected File xhtmlDir = null;
    protected String inputFilename = "";

    // currently-active style declarations

    // some object "current-style"

    @Override
    public void characters(char[] ch, int start, int length) {
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) {
        Map<String,String> attrMap = new HashMap<String,String>();
        // parse attributes
        for (int ai = 0; ai < attributes.getLength(); ai++) {
            attrMap.put(attributes.getQName(ai), attributes.getValue(ai));
        }

        if (attrMap.get("id") != null) {
            System.err.println("id: " + attrMap.get("id"));
        }
        previousTag = currentTag;
        currentTag = qName;

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        try {

            if (document.isOpen()) {
                if (XhtmlTags.HTML.equals(qName)) {
                    if (this.controlOpenClose) {
                        document.close();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void startDocument() {
        System.err.println("File: " + inputFilename);
    }

    public XhtmlTocBookmarkProbe(String xhtml, Document docInProgress) throws MalformedURLException, IOException, SAXException {
        super(docInProgress);
        this.controlOpenClose = false;
        document = docInProgress;

        if (!(document.isOpen())) {
            document.open();
            document.newPage();
        }

        parseXhtml(xhtml);
    }

    void parseXhtml(String xhtml) throws MalformedURLException, IOException, SAXException {

        File xhtmlFile = new File(xhtml);
        xhtmlDir = xhtmlFile.getParentFile();

        inputFilename = xhtmlFile.getName();

        CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
        ResolvingXMLReader reader = new ResolvingXMLReader();
        Catalog catalog = reader.getCatalog();
        catalog.parseCatalog("etc/xhtml/catalog.xml");
        reader.setContentHandler(this);
        reader.parse(new InputSource(new FileInputStream(xhtml)));
    }

    public static void process(String xhtml, Document doc) {
        try {
            new XhtmlTocBookmarkProbe(xhtml,doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String htmlInPath;

        if (args.length==1) {
            htmlInPath = args[0];
        } else {
            htmlInPath = "data/pg-prufrock/1459/0.html";
        }
        try {
            Document doc = new Document();
            process(htmlInPath, doc);
        } catch (Exception e) {
            printlnerr(e.getLocalizedMessage());
        }
    }

}
