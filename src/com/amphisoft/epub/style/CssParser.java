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
package com.amphisoft.epub.style;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;

public class CssParser extends CSSEngine {

    /**
     *
     * @param doc Associated document
     * @param uri Document URI
     * @param p CSS parser
     * @param vm property value managers
     * @param sm shorthand properties managers
     * @param pe supported pseudo-element names
     * @param sns style attribute namespace URI
     * @param sln style attribute local name
     * @param cns class attribute namespace URI
     * @param cln class attribute local name
     * @param hints support non-CSS presentational hints?
     * @param hintsNS hints namespace URI
     * @param ctx CSS context
     */
    protected CssParser(Document doc, ParsedURL uri, ExtendedParser p,
                        ValueManager[] vm, ShorthandManager[] sm, String[] pe, String sns,
                        String sln, String cns, String cln, boolean hints, String hintsNS,
                        CSSContext ctx) {
        super(doc, uri, p, vm, sm, pe, sns, sln, cns, cln, hints, hintsNS, ctx);

    }


    public static CssParser newCssParser() {
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {


    }

}
