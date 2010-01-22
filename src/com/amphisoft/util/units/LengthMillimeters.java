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

public class LengthMillimeters extends Length {
    private final float magnitude;

    public LengthMillimeters(float f) {
        magnitude = f;
    }

    @Override
    public float getMagnitude() {
        return magnitude;
    }

    @Override
    public LengthPoints toPoints() {
        float magInches = magnitude / 25.4f;
        LengthInches inches = new LengthInches(magInches);
        return inches.toPoints();
    }

    @Override
    public String toString() {
        return String.format("%.2f", magnitude) + "mm";
    }
}
