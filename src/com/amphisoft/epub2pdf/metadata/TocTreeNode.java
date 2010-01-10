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
