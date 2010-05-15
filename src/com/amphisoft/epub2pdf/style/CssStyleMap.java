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
package com.amphisoft.epub2pdf.style;

import java.util.Map;
import java.util.HashMap;

public class CssStyleMap {
    private Map<String,Map<String,StyleSpecText>> map;

    public CssStyleMap() {
        map = new HashMap<String,Map<String,StyleSpecText>>();
    }

    public CssStyleMap(CssStyleMap csmOrig) {
        this();
        for (String s : csmOrig.map.keySet()) {
            Map<String,StyleSpecText> valueOrig = csmOrig.map.get(s);
            Map<String,StyleSpecText> valueNew =
                new HashMap<String,StyleSpecText>();
            for (String s2 : valueOrig.keySet()) {
                valueNew.put(s2, new StyleSpecText(valueOrig.get(s2)));
            }
            map.put(s, valueNew);
        }
    }

    /**
     *
     * @param elemType XHTML element type to which the style spec applies, or "*" for all types
     * @param elemClass name of element class to which the style spec applies, or "*" for none specified
     * @param sst
     */
    public void addTextStyleSpecFor(String elemType, String elemClass, StyleSpecText sst) {
        if (map.get(elemType) == null) {
            map.put(elemType, new HashMap<String, StyleSpecText>());
        }
        Map<String,StyleSpecText> elemMap = map.get(elemType);

        elemMap.put(elemClass, sst);
    }

    public StyleSpecText getTextStyleSpecFor(String elemType, String elemClass) {
        StyleSpecText defaultClassStyle = null;
        StyleSpecText elemClassStyle = null;
        if (map.get(elemType) != null) {
            elemClassStyle = map.get(elemType).get(elemClass);
        }
        if (map.get("*") != null) {
            defaultClassStyle = map.get("*").get(elemClass);
        }
        if (elemClassStyle == null) {
            return defaultClassStyle;
        } else {
            return elemClassStyle;
        }
    }

    public void updateWith(CssStyleMap mapIn) {
        for (String elemType : mapIn.map.keySet()) {
            for (String elemClass : mapIn.map.get(elemType).keySet()) {
                StyleSpecText sst = mapIn.map.get(elemType).get(elemClass);
                this.addTextStyleSpecFor(elemType, elemClass, sst);
            }
        }
    }
}
