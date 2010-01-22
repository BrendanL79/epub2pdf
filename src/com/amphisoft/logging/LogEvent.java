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
package com.amphisoft.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class LogEvent {
    LogEventProperties props = new LogEventProperties();

    List<String> getPropNames() {

        List<String> propNames = new ArrayList<String>();
        for (Property p : props.propSet) {
            propNames.add(p.key);
        }
        return propNames;
    }

    String getPropertyValue(String k) {
        return props.getProp(k);
    }
}

class LogEventProperties {
    LinkedHashSet<Property> propSet = new LinkedHashSet<Property>();
    Map<String,Property> propMap = new HashMap<String,Property>();

    void addProp(String key, String value) {
        Property prop = new Property(key,value);
        propSet.add(prop);
        propMap.put(key,prop);
    }

    String getProp(String key) {
        Property keyProp = propMap.get(key);
        if (keyProp != null)
            return propMap.get(key).value;
        else
            return "";
    }

}

class Property {
    final String key;
    final String value;

    Property(String k, String v) {
        key = k;
        value = v;
    }
}