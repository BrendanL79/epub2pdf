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
package com.amphisoft.util.units;

import com.amphisoft.util.StringManip;

public abstract class Length {
    public abstract float getMagnitude();
    public abstract LengthPoints toPoints();

    public static Length fromString(String s) {
        String[] parts = StringManip.parseSpecifiedLength(s);
        float magnitude = Float.valueOf(parts[0]);
        String units = parts[1];
        if (units.equals("")) {
            return new LengthPoints(magnitude);
        } else if (units.equals("cm")) {
            magnitude *= 10.0;
            return new LengthMillimeters(magnitude);
        } else if (units.equals("mm")) {
            return new LengthMillimeters(magnitude);
        } else if (units.equals("in")) {
            return new LengthInches(magnitude);
        } else if (units.equals("pt")) {
            return new LengthPoints(magnitude);
        } else {
            return null;
        }
    }
}
