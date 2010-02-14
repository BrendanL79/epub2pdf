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
package com.amphisoft.epub.metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.ResolvingXMLReader;

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
        	CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
        	ResolvingXMLReader xmlReader = new ResolvingXMLReader();
        	
            Container c = null;
            
            DOMParser domParser = new DOMParser();
            domParser.setEntityResolver(xmlReader.getEntityResolver());
            domParser.parse(path);
            org.w3c.dom.Document containerDoc = domParser.getDocument();
            org.w3c.dom.NodeList rootfilesNodeList = containerDoc.getElementsByTagNameNS("*","rootfiles");
            if(rootfilesNodeList.getLength() < 1) {
            	throw new RuntimeException("Missing required rootfiles node in container XML");
            }
            else 
            {
            	org.w3c.dom.Node rootfilesNode = rootfilesNodeList.item(0);
            	org.w3c.dom.NodeList rootfileNodeList = rootfilesNode.getChildNodes();
            	if(rootfileNodeList.getLength() < 1) {
            		throw new RuntimeException("Rootfiles node in container XML has no children");
            	}
            	else {
            		org.w3c.dom.Node rootfileNode = null;
            		for(int rfi = 0; rfi < rootfileNodeList.getLength(); rfi++) {
            			org.w3c.dom.Node n = rootfileNodeList.item(rfi);
            			if("rootfile".equals(n.getLocalName())) {
            				rootfileNode = n;
            				break;
            			}
            		}
            		if(rootfileNode != null) {
            			org.w3c.dom.NamedNodeMap rfAttrs = rootfileNode.getAttributes();
            			org.w3c.dom.Node mediaTypeNode = rfAttrs.getNamedItem("media-type");
            			org.w3c.dom.Node fullPathNode = rfAttrs.getNamedItem("full-path");
            			if(mediaTypeNode == null || fullPathNode == null) {
            				throw new RuntimeException("rootfile node missing one or both required attributes {full-path, media-type}");
            			}
            			else {
            				String rootPath = fullPathNode.getTextContent();
            				c = new Container(rootPath);
            			}
            		}
            	}
            }
            return c;
        } catch (Exception e) {
        	RuntimeException rE = new RuntimeException(e);
            throw rE;
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
