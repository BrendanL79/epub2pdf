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
package com.amphisoft.epub2pdf.content;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.amphisoft.epub2pdf.style.StyleSpecText;
import com.amphisoft.pdf.ITAlignment;
import com.lowagie.text.Anchor;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.HyphenationAuto;

/**
 * Automates the generation of iText PDF components
 * @author brendanl
 *
 */
@SuppressWarnings("unchecked")
public class TextFactory {

    static {
        FontFactory.registerDirectory("lib/fonts",true);
        Set fontFamsRaw = FontFactory.getRegisteredFamilies();
        Set<String> fontFamilies = new TreeSet<String>();
        for (Object o : fontFamsRaw) {
            fontFamilies.add(o.toString());
        }
        System.err.print("Registered font families: ");
        StringBuilder sB = new StringBuilder();
        for (String s : fontFamilies) {
            sB.append(s);
            sB.append(", ");
        }
        String famList = sB.toString();
        famList = famList.substring(0, famList.length() - 2);
        System.err.println(famList);
    }

    protected static Font defaultFont;
    protected static Font defaultFontMono;
    protected static float defaultFontSize = 12f;
    protected static float defaultFontMonoSize = 10f;
    protected static float defaultLeadingMultiplier = 1.25f;
    protected static ITAlignment defaultITAlignment = ITAlignment.LEFT;

    static void setDefaultFont(Font defaultFont) {
        TextFactory.defaultFont = defaultFont;
    }

    static void setDefaultFontMono(Font defaultFontMono) {
        TextFactory.defaultFontMono = new Font(defaultFontMono);
    }

    static void setDefaultFontSize(float defaultFontSize) {
        TextFactory.defaultFontSize = defaultFontSize;
    }

    static void setDefaultFontMonoSize(float defaultFontMonoSize) {
        TextFactory.defaultFontMonoSize = defaultFontMonoSize;
    }

    static void setDefaultLeadingMultiplier(float defaultLeadingMultiplier) {
        TextFactory.defaultLeadingMultiplier = defaultLeadingMultiplier;
    }

    static void setDefaultITAlignment(ITAlignment defaultITAlignment) {
        TextFactory.defaultITAlignment = defaultITAlignment;
    }

    public static Set<String> getFontFamilies() {
        Set<String> fams = new HashSet<String>();
        for (Object o : FontFactory.getRegisteredFamilies()) {
            fams.add(o.toString());
        }
        return fams;
    }

    public static boolean fontFamilyRegistered(String fam) {
    	fam = fam.trim();
        return getFontFamilies().contains(fam);
    }

    static {
        FontFactory.registerDirectory("lib/fonts",true);

        defaultFont = FontFactory.getFont("helvetica");
        //.getFont("Calibri", FontFactory.defaultEncoding, BaseFont.EMBEDDED);
        defaultFontMono = FontFactory.getFont("courier");
        //.getFont("Consolas", FontFactory.defaultEncoding, BaseFont.EMBEDDED);
        defaultFont.setSize(defaultFontSize);
        defaultFontMono.setSize(defaultFontMonoSize);
    }

    private float baseFontSize = defaultFontSize;
    private ITAlignment alignment = defaultITAlignment;
    private Font currentFont = null;
    private Paragraph currentParagraph = null;
    private float currentLeadingMultiplier = defaultLeadingMultiplier;
    protected float[] HMULTS = { 2f, 1.5f, 1.33f, (14f/12f), 1f, (10f/12f) };

    public TextFactory() {
    }

    Paragraph getCurrentParagraph() {
        return currentParagraph;
    }

    public Paragraph newParagraph() {
        Paragraph p = new Paragraph();
        p.setAlignment(alignment.value);
        p.setFont(defaultFont);
        currentFont = defaultFont;
        p.setHyphenation(new HyphenationAuto("en","US",2,2));
        p.setLeading(0F, getCurrentLeadingMultiplier());
        p.setSpacingAfter(defaultFont.getSize() * 0.33F);
        currentParagraph = p;
        return p;
    }

    public Paragraph newParagraphPre() {
        Paragraph p = new Paragraph();
        p.setAlignment(ITAlignment.LEFT.value);
        p.setFont(defaultFontMono);
        currentFont = defaultFontMono;
        p.setLeading(0F, getCurrentLeadingMultiplier());
        p.setSpacingAfter(defaultFont.getSize() * 0.33F);
        currentParagraph = p;
        return p;
    }

    public Paragraph newHeadline(int i) {
        if (i < 1) {
            i = 1;
        }
        if (i > 6) {
            i = 6;
        }
        Font hFont =
            FontFactory.getFont(
                defaultFont.getFamilyname(),
                FontFactory.defaultEncoding,
                BaseFont.EMBEDDED,
                baseFontSize * HMULTS[i-1],
                Font.BOLD);
        Paragraph h = new Paragraph();
        h.setAlignment(ITAlignment.CENTER.value);
        h.setFont(hFont);
        currentFont = hFont;
        h.setLeading(0F, getCurrentLeadingMultiplier());
        h.setSpacingAfter(defaultFont.getSize() * 0.33F);
        currentParagraph = h;
        return h;
    }

    public Anchor newAnchor() {
        Anchor a = new Anchor();
        a.setFont(defaultFont);
        a.setLeading(defaultFont.getSize() * getCurrentLeadingMultiplier());
        currentFont = defaultFont;
        return a;
    }

    /**
     * @return a new Font with the same settings as the Font assigned to the most recently-created Paragraph at time of construction.
     */
    public Font getCurrentFont() {
        if (currentFont == null)
            currentFont = getDefaultFont();
        return new Font(currentFont);
    }

    public Font getModifiedFont(Font font, StyleSpecText styleSpec) {
        Font modFont = font;
        modifyFontInPlace(modFont,styleSpec);
        return modFont;
    }

    void modifyFontInPlace(Font font, StyleSpecText styleSpec) {
        int style = font.getStyle();
        if (styleSpec.isBold()) {
            style |= Font.BOLD;
        }
        if (styleSpec.isItalic()) {
            style |= Font.ITALIC;
        }
        font.setStyle(style);
    }

    public float getCurrentLeadingMultiplier() {
        return currentLeadingMultiplier;
    }

    public Font getDefaultFont() {
        return new Font(defaultFont);
    }

    public Font getDefaultFontMono() {
        return new Font(defaultFontMono);
    }

    public static boolean setDefaultFontByName(String fontName) {
        fontName = fontName.toLowerCase();
        if (TextFactory.fontFamilyRegistered(fontName)) {
            Font newDefaultFont = FontFactory.getFont(fontName, FontFactory.defaultEncoding, BaseFont.EMBEDDED);
            newDefaultFont.setSize(defaultFontSize);
            defaultFont = newDefaultFont;
            return true;
        } else {
            return false;
        }
    }

    public static void setDefaultFontBaseSize(float f) {
        defaultFontSize = f;
    }

    public static boolean setDefaultMonoFontByName(String fontName) {
        fontName = fontName.toLowerCase();
        if (TextFactory.fontFamilyRegistered(fontName)) {
            Font newDefaultMonoFont = FontFactory.getFont(fontName, FontFactory.defaultEncoding, BaseFont.EMBEDDED);
            newDefaultMonoFont.setSize(defaultFontMonoSize);
            defaultFontMono = newDefaultMonoFont;
            return true;
        } else {
            return false;
        }
    }

    public static void setDefaultMonoFontBaseSize(float f) {
        defaultFontMonoSize = f;
    }
    public static void main(String[] args) {

        FontFactoryImp fontFI = FontFactory.getFontImp();

        System.out.println("\nFAMILIES");
        Set<Object> famsRaw = fontFI.getRegisteredFamilies();
        Set<String> fams = new TreeSet<String>();
        for (Object s : famsRaw) {
            fams.add(s.toString());
        }
        for (String s : fams) {
            System.out.println(s);
        }

        System.out.println("\nFONTS");
        Set<Object> fontsRaw = fontFI.getRegisteredFonts();
        Set<String> fonts = new TreeSet<String>();
        for (Object s : fontsRaw) {
            fonts.add(s.toString());
        }
        for (String s : fonts) {
            System.out.println(s);
        }

        TextFactory pF = new TextFactory();
        Paragraph para = pF.newParagraph();
        System.out.println(para.getFont().getFamilyname());
    }

}
