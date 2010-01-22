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

import java.util.ArrayList;
import java.util.List;

import org.apache.batik.css.parser.DefaultDocumentHandler;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

public class CssHandler extends DefaultDocumentHandler {

    private static List<Short> luTypesWithString = new ArrayList<Short>();
    private static List<Short> luTypesWithFloatDim = new ArrayList<Short>();

    static {
        luTypesWithString.add(LexicalUnit.SAC_URI);
        luTypesWithString.add(LexicalUnit.SAC_ATTR);
        luTypesWithString.add(LexicalUnit.SAC_IDENT);
        luTypesWithString.add(LexicalUnit.SAC_STRING_VALUE);
        luTypesWithString.add(LexicalUnit.SAC_UNICODERANGE);

        luTypesWithFloatDim.add(LexicalUnit.SAC_EM);
        luTypesWithFloatDim.add(LexicalUnit.SAC_INCH);
        luTypesWithFloatDim.add(LexicalUnit.SAC_MILLIMETER);
        luTypesWithFloatDim.add(LexicalUnit.SAC_PERCENTAGE);
        luTypesWithFloatDim.add(LexicalUnit.SAC_PICA);
        luTypesWithFloatDim.add(LexicalUnit.SAC_PIXEL);
        luTypesWithFloatDim.add(LexicalUnit.SAC_POINT);
    }

    private String currentUri;
    private List<String> parsedUris;

    public CssHandler() {
        parsedUris = new ArrayList<String>();
    }

    private CssStyleMap latestCssStyleMap;
    private boolean cssStyleMapComplete;

    CssStyleMap getLatestCssStyleMap() {
        if (!cssStyleMapComplete) {
            return null;
        } else {
            return new CssStyleMap(latestCssStyleMap);
        }
    }

    @Override
    public void startDocument(InputSource source) throws CSSException {
        super.startDocument(source);
        latestCssStyleMap = new CssStyleMap();
        cssStyleMapComplete = false;
        currentUri = source.getURI();
        //System.err.println("CSS-start|" + currentUri);
        if (parsedUris.contains(currentUri)) {
            //throw new CssAlreadyParsedException();
        } else {
            parsedUris.add(currentUri);
        }
    }

    private StyleSpecText latestTextSpec;

    StyleSpecText getLatestTextStyleSpec() {
        return new StyleSpecText(latestTextSpec);
    }

    @Override
    public void startSelector(SelectorList selectors) throws CSSException {
        latestTextSpec = new StyleSpecText();
        //System.err.println("CSS-sel-start");
        //for(int i = 0; i < selectors.getLength(); i++) {
        //Selector s = selectors.item(i);
        //short sId = s.getSelectorType();
        //System.err.print("CSS-sel|" + SacSelector.lookupName(sId) + "|" + s.toString());
        //System.err.println();
        //}
        //System.err.print("");
    }

    @Override
    public void endSelector(SelectorList selectors) throws CSSException {
        //System.err.println("CSS-sel-end");
        //for(int i = 0; i < selectors.getLength(); i++) {
        //Selector s = selectors.item(i);
        //short sId = s.getSelectorType();
        //System.err.println("CSS-sel|" + SacSelector.lookupName(sId) + "|" + s.toString());
        //}

        for (int i = 0; i < selectors.getLength(); i++) {
            Selector s = selectors.item(i);
            if (s.getSelectorType() == Selector.SAC_CONDITIONAL_SELECTOR) {
                ConditionalSelector cS = (ConditionalSelector) s;
                String condStr = cS.toString();
                int sepIndex = condStr.indexOf('.');
                String elemType, elemClass;
                if (sepIndex < 0) {
                    elemType = condStr;
                    elemClass = "*";
                } else {
                    elemType = condStr.substring(0, sepIndex);
                    elemClass = condStr.substring(sepIndex+1);
                }
                latestCssStyleMap.addTextStyleSpecFor(elemType, elemClass, latestTextSpec);
            }
        }

        latestTextSpec = null;
        //System.err.print("");
    }

    @Override
    public void property(String name, LexicalUnit value, boolean important)
    throws CSSException {

        short luType = value.getLexicalUnitType();
        //System.err.print(
        //"CSS-prop|" + name + "|" +
        //SacLexicalUnit.lookupName(luType) + "|"
        //);

        if ("font-weight".equals(name)) {
            String weight = value.getStringValue();
            if (weight.startsWith("bold")) {
                latestTextSpec.setBold(true);
            }
        }

        if ("font-style".equals(name)) {
            String fstyle = value.getStringValue();
            if ("italic".equals(fstyle) || "oblique".equals(fstyle)) {
                latestTextSpec.setItalic(true);
            }
        }

        if (luTypesWithString.contains(luType)) {
            //System.err.print(value.getStringValue());
        } else if (luTypesWithFloatDim.contains(luType)) {
            //System.err.print(value.getFloatValue());
            //System.err.print(value.getDimensionUnitText());
        } else if (luType == LexicalUnit.SAC_INTEGER) {
            //System.err.print(value.getIntegerValue());
        } else if (luType == LexicalUnit.SAC_INHERIT) {
            // no-op
        } else {
            //System.err.print("?VALUE?");
        }


        //System.err.println();
    }

    @Override
    public void startFontFace() {
        //System.err.print("CSS-fontface-start");
        //System.err.println();
    }

    @Override
    public void endFontFace() {
        //System.err.print("CSS-fontface-end");
        //System.err.println();
    }

    @Override
    public void endDocument(InputSource source) {
        cssStyleMapComplete = true;
        //System.err.print("");
    }
}
