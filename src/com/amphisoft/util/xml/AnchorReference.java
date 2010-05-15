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
package com.amphisoft.util.xml;

/**
 * An anchor "href" referencing a named anchor within its target file.
 * @author brendanl
 *
 */
public class AnchorReference {
    public final String hrefPath;
    public final String anchorId;

    public AnchorReference(String path, String id) {
        hrefPath = path;
        if (id == null)
            anchorId = "";
        else
            anchorId = id;
    }

    public AnchorReference(String fullHref) {
        int hashIndex = fullHref.indexOf("#");
        if (hashIndex<0) {
            hrefPath = fullHref;
            anchorId = "";
        } else {
            String path = fullHref.substring(0, hashIndex);
            String id = fullHref.substring(hashIndex+1);
            hrefPath = path;
            anchorId = id;
        }

    }

    /**
     * Returns the string in "path#id" form suitable for use as the value of an href attribute.
     */
    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();

        sB.append(hrefPath);
        if (!(anchorId.equals(""))) {
            sB.append('#');
            sB.append(anchorId);
        }

        return sB.toString();
    }
}
