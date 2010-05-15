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
package com.amphisoft.epub2pdf.content;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.amphisoft.epub2pdf.style.StyleSpecText;
import com.amphisoft.pdf.ITAlignment;
import com.lowagie.text.Anchor;
import com.lowagie.text.ExceptionConverter;
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

	protected static Font _defaultFont;
	protected static Font _defaultFontMono;
	protected static float _defaultFontSize = 12f;
	protected static float _defaultFontMonoSize = 10f;
	protected static float _defaultLeadingMultiplier = 1.25f;
	protected static ITAlignment _defaultITAlignment = ITAlignment.LEFT;

	static void setDefaultFont(Font defaultFont) {
		_defaultFont = new Font(defaultFont);
	}

	static void setDefaultFontMono(Font defaultFontMono) {
		_defaultFontMono = new Font(defaultFontMono);
	}

	public static void setDefaultFontSize(float defaultFontSize) {
		_defaultFontSize = defaultFontSize;
		_defaultFont.setSize(_defaultFontSize);
	}

	public static void setDefaultFontMonoSize(float defaultFontMonoSize) {
		_defaultFontMonoSize = defaultFontMonoSize;
		_defaultFontMono.setSize(_defaultFontMonoSize);
	}

	static void setDefaultLeadingMultiplier(float defaultLeadingMultiplier) {
		_defaultLeadingMultiplier = defaultLeadingMultiplier;
	}

	static void setDefaultITAlignment(ITAlignment defaultITAlignment) {
		_defaultITAlignment = defaultITAlignment;
	}

	public static Set<String> getFontFamilies() {
		Set<String> fams = new HashSet<String>();
		for (Object o : FontFactory.getRegisteredFamilies()) {
			fams.add(o.toString());
		}
		return fams;
	}

	public static boolean fontFamilyRegistered(String fam) {
		return getFontFamilies().contains(fam);
	}

	static {
		FontFactory.registerDirectory("lib/fonts",true);

		_defaultFont = FontFactory.getFont("helvetica");
		_defaultFontMono = FontFactory.getFont("courier");
		_defaultFont.setSize(_defaultFontSize);
		_defaultFontMono.setSize(_defaultFontMonoSize);
	}

	private float baseFontSize = _defaultFontSize;
	private ITAlignment alignment = _defaultITAlignment;
	private Font currentFont = null;
	private Paragraph currentParagraph = null;
	private float currentLeadingMultiplier = _defaultLeadingMultiplier;
	protected float[] HMULTS = { 2f, 1.5f, 1.33f, (14f/12f), 1f, (10f/12f) };

	public TextFactory() {
	}

	Paragraph getCurrentParagraph() {
		return currentParagraph;
	}

	public Paragraph newParagraph() {
		Paragraph p = new Paragraph();
		p.setAlignment(alignment.value);
		p.setFont(_defaultFont);
		currentFont = _defaultFont;
		p.setHyphenation(new HyphenationAuto("en","US",2,2));
		p.setLeading(0F, getCurrentLeadingMultiplier());
		p.setSpacingAfter(_defaultFont.getSize() * 0.33F);
		currentParagraph = p;
		return p;
	}

	public Paragraph newParagraphPre() {
		Paragraph p = new Paragraph();
		p.setAlignment(ITAlignment.LEFT.value);
		p.setFont(_defaultFontMono);
		currentFont = _defaultFontMono;
		p.setLeading(0F, getCurrentLeadingMultiplier());
		p.setSpacingAfter(_defaultFont.getSize() * 0.33F);
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
					_defaultFont.getFamilyname(),
					FontFactory.defaultEncoding,
					BaseFont.EMBEDDED,
					baseFontSize * HMULTS[i-1],
					Font.BOLD);
		Paragraph h = new Paragraph();
		h.setAlignment(ITAlignment.CENTER.value);
		h.setFont(hFont);
		currentFont = hFont;
		h.setLeading(0F, getCurrentLeadingMultiplier());
		h.setSpacingAfter(_defaultFont.getSize() * 0.33F);
		currentParagraph = h;
		return h;
	}

	public Anchor newAnchor() {
		Anchor a = new Anchor();
		a.setFont(_defaultFont);
		a.setLeading(_defaultFont.getSize() * getCurrentLeadingMultiplier());
		currentFont = _defaultFont;
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
		return new Font(_defaultFont);
	}

	public Font getDefaultFontMono() {
		return new Font(_defaultFontMono);
	}

	public static boolean setDefaultFontByName(String fontName) {
		fontName = fontName.toLowerCase().trim();
		if (TextFactory.fontFamilyRegistered(fontName)) {
			Font newDefaultFont = null;
			try {
				newDefaultFont = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (ExceptionConverter eC) {
				if(eC.getException() instanceof UnsupportedEncodingException) {
					newDefaultFont = FontFactory.getFont(fontName, FontFactory.defaultEncoding, BaseFont.EMBEDDED);
				}
				else {
					throw new RuntimeException(eC);
				}
			}
			newDefaultFont.setSize(_defaultFontSize);
			_defaultFont = newDefaultFont;
			return true;
		} else {
			return false;
		}
	}

	public static boolean setDefaultFontMonoByName(String fontName) {
		fontName = fontName.toLowerCase();
		if (TextFactory.fontFamilyRegistered(fontName)) {
			Font newDefaultMonoFont = FontFactory.getFont(fontName, FontFactory.defaultEncoding, BaseFont.EMBEDDED);
			newDefaultMonoFont.setSize(_defaultFontMonoSize);
			_defaultFontMono = newDefaultMonoFont;
			return true;
		} else {
			return false;
		}
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
