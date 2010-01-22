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

import static com.amphisoft.util.Print.*;

public class StderrLogger implements LogEventSubscriber {

    @Override
    public void notify(LogEvent ev) {
        StringBuilder outSB = new StringBuilder("");
        for (String propName : ev.getPropNames()) {
            if (outSB.length()>0) {
                outSB.append(",");
            }
            outSB.append(propName);
            outSB.append(",");
            outSB.append(ev.getPropertyValue(propName));
        }
        printlnerr(outSB.toString());
    }

}
