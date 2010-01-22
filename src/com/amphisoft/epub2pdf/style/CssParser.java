/*
epub2pdf, version 0.2 - Copyright 2010 Brendan C. LeFebvre

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.List;

import org.apache.batik.css.parser.Parser;
import org.w3c.css.sac.CSSException;

public class CssParser {

    private Parser parser;
    private CssHandler handler;

    public CssParser() {
        parser = new Parser();
        handler = new CssHandler();
        parser.setDocumentHandler(handler);
    }

    public StyleSpecText getStylesFromStyleAttribute(String attrValue) throws CSSException, IOException {
        parser.parseStyleDeclaration(attrValue);
        return handler.getLatestTextStyleSpec();
    }

    public CssStyleMap getStylesFromStyleTag(String tagContents) throws CSSException, IOException {
        File tempCssFile = File.createTempFile("epub2pdf_tmp_css_", ".css");

        FileWriter tempFW = new FileWriter(tempCssFile);
        tempFW.write(tagContents);
        tempFW.close();

        String uri = tempCssFile.toURI().toString();
        CssStyleMap styleMap = getStylesFromFileURI(uri);

        if (tempCssFile.delete() == false) {
            tempCssFile.deleteOnExit();
        }

        return styleMap;
    }

    public CssStyleMap getStylesFromFileURI(String uri) throws CSSException, IOException {
        try {
            parser.parseStyleSheet(uri);
        } catch (CssAlreadyParsedException e) {
            //System.err.println("CSS:already parsed this URI; use cached data");
        }

        return handler.getLatestCssStyleMap();
    }
}
