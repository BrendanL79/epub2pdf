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
package com.amphisoft.epub2pdf.metadata;

import com.amphisoft.epub.metadata.Ncx;

/**
 * Applies relevant information from the given NCX to the output PDF.
 * An NcxParser instance can only be "bound" to a single Ncx instance,
 * either at construction or with a call to setSource(), and this binding is
 * permanent for the lifetime of the NcxParser.
 * @author brendanl
 *
 */
public class NcxParser {
    private Ncx ncxSource;

    public NcxParser() {}

    public NcxParser(Ncx ncx) {
        setSource(ncx);
    }

    public void setSource(Ncx ncx) {
        if (ncxSource == null) {
            ncxSource = ncx;
        } else {
            throw new IllegalArgumentException(
                "This NcxParser is already bound to an Ncx instance");
        }
    }

    // called first by the (as-yet unwritten) query methods
    void checkBoundState() {
        if (ncxSource == null) {
            throw new IllegalStateException("Attempted to query an unbound NcxParser");
        }
    }
}
