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
package com.amphisoft.epub.content;

import java.io.IOException;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XhtmlParser implements XMLReader {
    private ResolvingXMLReader reader;

    public XhtmlParser() {
        this("etc/xhtml/catalog.xml");
    }

    private XhtmlParser(String catalogPath) {
        CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
        reader = new ResolvingXMLReader();
        Catalog catalog = reader.getCatalog();
        try {
            catalog.parseCatalog(catalogPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContentHandler getContentHandler() {
        return reader.getContentHandler();
    }

    @Override
    public DTDHandler getDTDHandler() {
        return reader.getDTDHandler();
    }

    @Override
    public EntityResolver getEntityResolver() {
        return reader.getEntityResolver();
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return reader.getErrorHandler();
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException,
                SAXNotSupportedException {
        return reader.getFeature(name);
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException,
                SAXNotSupportedException {
        return reader.getProperty(name);
    }

    @Override
    public void parse(InputSource input) throws IOException, SAXException {
        reader.parse(input);

    }

    @Override
    public void parse(String systemId) throws IOException, SAXException {
        reader.parse(systemId);
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        reader.setContentHandler(handler);
    }

    @Override
    public void setDTDHandler(DTDHandler handler) {
        reader.setDTDHandler(handler);
    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {
        reader.setEntityResolver(resolver);
    }

    @Override
    public void setErrorHandler(ErrorHandler handler) {
        reader.setErrorHandler(handler);
    }

    @Override
    public void setFeature(String name, boolean value)
    throws SAXNotRecognizedException, SAXNotSupportedException {
        reader.setFeature(name, value);
    }

    @Override
    public void setProperty(String name, Object value)
    throws SAXNotRecognizedException, SAXNotSupportedException {
        reader.setProperty(name, value);
    }

}
