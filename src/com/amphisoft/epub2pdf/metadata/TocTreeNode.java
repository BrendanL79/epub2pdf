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

import javax.swing.tree.DefaultMutableTreeNode;

import com.lowagie.text.pdf.PdfDestination;

public class TocTreeNode extends DefaultMutableTreeNode {

	Payload payload = new Payload();
	
	public TocTreeNode() {
		super();
		this.setUserObject(payload);
	}
	
	public void setDisplayedTitle(String s) {
		payload.setDisplayedTitle(s);
	}
	
	public void setPdfDestination(PdfDestination d) {
		payload.setDestination(d);
	}
	
	public String getDisplayedTitle() {
		return payload.displayedTitle;
	}
	
	public PdfDestination getPdfDestination() {
		return payload.pdfDestination;
	}
	
	private static final long serialVersionUID = 1985017816114097551L;

	public class Payload {
		PdfDestination pdfDestination = null;
		String displayedTitle = null;
		
		public void setDisplayedTitle(String s) {
			if(displayedTitle != null)
				throw new RuntimeException(new IllegalAccessException());
			displayedTitle = s;
		}
		public void setDestination(PdfDestination d) {
			if(pdfDestination != null)
				throw new RuntimeException(new IllegalAccessException());
			pdfDestination = d;
		}
	}
}
