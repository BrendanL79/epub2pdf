/*
epub2pdf, version 0.1 - Copyright 2010 Brendan C. LeFebvre

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
package com.amphisoft.pdf;

import java.io.FileOutputStream;

/**
 *
 * @author brendanl
 *
 */
public class ITPdf implements Pdf {
    public final com.lowagie.text.Document document;
    public final com.lowagie.text.pdf.PdfWriter writer;

    public ITPdf(String path) {
        try {

            float
            mLeft = marginUniformPt,
                    mRight = marginUniformPt,
                             mTop = marginUniformPt,
                                    mBottom = marginUniformPt;

            if (!(makeUniformMargins)) {
                mLeft = marginLeftPt;
                mRight = marginRightPt;
                mTop = marginTopPt;
                mBottom = marginBottomPt;
            }
            document = new com.lowagie.text.Document(
                pageRectangle, mLeft, mRight, mTop, mBottom);
            writer = com.lowagie.text.pdf.PdfWriter.getInstance(
                         document, new FileOutputStream(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // public static members/mutators for params that must be set pre-Document.open()

    public static com.lowagie.text.Rectangle pageRectangle = ITPageSize.FOXIT_ESLICK;

    static boolean makeUniformMargins = false;
    static float marginUniformPt = 5.0F;

    static float marginLeftPt = 4.0F;
    static float marginRightPt = 8.0F;
    static float marginTopPt = 4.0F;
    static float marginBottomPt = 6.0F;

    public static void setMarginsUniformly(boolean b) {
        makeUniformMargins = b;
    }
    public static void setMarginUniformPt(float f) {
        marginUniformPt = f;
    }

    public static void setMarginLeftPt(float f) {
        marginLeftPt = f;
    }
    public static void setMarginRightPt(float f) {
        marginRightPt = f;
    }
    public static void setMarginTopPt(float f) {
        marginTopPt = f;
    }
    public static void setMarginBottomPt(float f) {
        marginBottomPt = f;
    }
}
