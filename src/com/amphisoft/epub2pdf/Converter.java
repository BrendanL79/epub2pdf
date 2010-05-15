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
package com.amphisoft.epub2pdf;

import static com.amphisoft.util.Print.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.amphisoft.util.jgtree.Tree;
import com.amphisoft.util.jgtree.TreeNode;

import com.amphisoft.epub.Epub;
import com.amphisoft.epub.metadata.Ncx;
import com.amphisoft.epub.metadata.Opf;
import com.amphisoft.epub.metadata.Ncx.NavPoint;
import com.amphisoft.epub2pdf.content.TextFactory;
import com.amphisoft.epub2pdf.content.XhtmlHandler;
import com.amphisoft.epub2pdf.metadata.TocTreeNode;
import com.amphisoft.pdf.ITPageSize;
import com.amphisoft.util.units.Length;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.RectangleReadOnly;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;

public class Converter {

    protected static float marginTopPt = 8.0f;
    protected static float marginRightPt = 8.0f;
    protected static float marginBottomPt = 8.0f;
    protected static float marginLeftPt = 8.0f;
    protected static Rectangle pageSize = ITPageSize.FOXIT_ESLICK;
    private static Properties epub2pdfProps;
    
    private static void loadProperties() {
    	epub2pdfProps = new Properties();
        String propsFilename = "epub2pdf.properties";
        try {
            epub2pdfProps.load(new FileInputStream(propsFilename));
        } catch (IOException e) {
            printlnerr("IOException reading properties from " + propsFilename + "; continuing anyway");
        }    	
    }

    static {
    	loadProperties();
    }
    
    Epub epubIn;
    File outputDir = new File(System.getProperty("user.home"));

    public void applyProperties(Properties props) {
        SortedSet<String> propsSorted = new TreeSet<String>();
        for (Object o : props.keySet()) {
            propsSorted.add(o.toString());
        }
        for (String propName : propsSorted) {
            if ("font.default.name".equals(propName)) {
                setDefaultFont(props);
            }
            if ("font.default.sizebase".equals(propName)) {
                setDefaultFontBaseSize(props);
            }
            if ("font.monospace.name".equals(propName)) {
                setMonospaceFont(props);
            }
            if ("font.monospace.sizebase".equals(propName)) {
                setMonospaceFontBaseSize(props);
            }
            if ("page.size".equals(propName)) {
                setPageSize(props);
            }
            if ("margins.size".equals(propName)) {
                setMargins(props);
            }
            if ("output.dir".equals(propName)) {
                setOutputDir(props);
            }
            if ("full.justify".equals(propName)) {
                setDefaultAlignment(props);
            }
        }
    }

    private void setDefaultAlignment(Properties props) {
        String justify = props.getProperty("full.justify").trim();
        if (justify.toLowerCase().contains("true")) {
            XhtmlHandler.setDefaultAlignment(Paragraph.ALIGN_JUSTIFIED);
            System.err.println("Default paragraph alignment: justified");
        } else {
            XhtmlHandler.setDefaultAlignment(Paragraph.ALIGN_LEFT);
            System.err.println("Default paragraph alignment: left");
        }
    }

    private void setOutputDir(Properties props) {
        String outputDirStr = props.getProperty("output.dir").trim();
        if (outputDirStr.length() == 0) {
            // leave as default
        } else {
            outputDir = new File(outputDirStr);
            if (!(outputDir.canRead() && outputDir.canWrite())) {
                System.err.println("Cannot access output directory " + outputDirStr);
                System.exit(1);
            }
        }
    }

    private void setPageSize(Properties props) {
        String pageSizeStr = props.getProperty("page.size").trim();
        pageSizeStr = pageSizeStr.toLowerCase();
        if (pageSizeStr.contains("x")) {
            int sepIdx = pageSizeStr.indexOf('x');
            try {
                String widthStr = pageSizeStr.substring(0,sepIdx);
                String heightStr = pageSizeStr.substring(sepIdx+1);

                Length width = Length.fromString(widthStr);
                Length height = Length.fromString(heightStr);

                System.err.println(
                    "Page size (w x h): " + width.toString() +
                    " x " + height.toString());

                pageSize = new RectangleReadOnly(
                    width.toPoints().getMagnitude(),
                    height.toPoints().getMagnitude());

            } catch (IndexOutOfBoundsException ioobe) {
                pageSizeErrorNotice();
            }
        } else {
            pageSizeErrorNotice();
        }

    }

    private void pageSizeErrorNotice() {
        System.err.println("Could not parse page size string; using default 90mmx115mm");
    }

    private void setMonospaceFontBaseSize(Properties props) {
        String sizeStr = props.getProperty("font.monospace.sizebase").trim();
        if (sizeStr == null) {
            sizeStr = "10pt";
        }
        Length mfbsLength = Length.fromString(sizeStr);
        float size = mfbsLength.toPoints().getMagnitude();
        TextFactory.setDefaultFontMonoSize(size);
        System.err.println("Monospace base size: " + size + "pt");
    }

    private void setMonospaceFont(Properties props) {
        String fontName = props.getProperty("font.monospace.name").trim();
        if (TextFactory.setDefaultFontMonoByName(fontName)) {
            System.err.println("Default monospace font: " + fontName);
        } else {
            System.err.println("Failed to set default monospace font to " + fontName + "; retaining previous value");
        }
    }

    private void setMargins(Properties props) {
        String marginStr = props.getProperty("margins.size").trim();
        StringTokenizer sT = new StringTokenizer(marginStr, ",");
        ArrayList<String> marginParams = new ArrayList<String>();
        while (sT.hasMoreTokens()) {
            marginParams.add(sT.nextToken());
        }
        int marginParamCount = marginParams.size();
        if (marginParamCount < 1 || marginParamCount > 4) {
            throw new IllegalArgumentException("Could not parse margins.size; retaining previous value");
        } else {
            ArrayList<Length> margins = new ArrayList<Length>();
            for (String param : marginParams) {
                margins.add(Length.fromString(param));
            }
            marginTopPt = margins.get(0).toPoints().getMagnitude();
            marginRightPt = margins.get(1).toPoints().getMagnitude();
            marginBottomPt = marginTopPt;
            marginLeftPt = marginRightPt;
            if (margins.size()>2)
                marginBottomPt = margins.get(2).toPoints().getMagnitude();
            if (margins.size()>3)
                marginLeftPt = margins.get(3).toPoints().getMagnitude();

            System.err.print("Margins (top right bottom left): ");
            for (Length el: margins) {
                System.err.print(el.toString() + " ");
            }
            System.err.println();
        }
    }

    private void setDefaultFontBaseSize(Properties props) {
        String sizeStr = props.getProperty("font.default.sizebase").trim();
        if (sizeStr == null) {
            sizeStr = "12pt";
        }
        Length dfbsLength = Length.fromString(sizeStr);
        float size = dfbsLength.toPoints().getMagnitude();
        TextFactory.setDefaultFontSize(size);
        System.err.println("Default font base size set to " + size + "pt");
    }

    private void setDefaultFont(Properties props) {
        String fontName = props.getProperty("font.default.name").trim();
        if (TextFactory.setDefaultFontByName(fontName)) {
            System.err.println("Default font set to " + fontName);
        } else {
            System.err.println("Failed to reset default font to " + fontName);
        }
    }

    public void convert(String epubPath) throws IOException,DocumentException {
        File epubFile = new File(epubPath);
        if (!(epubFile.canRead())) {
            throw new IOException("Could not read " + epubPath);
        } else {
            System.err.println("Converting " + epubFile.getAbsolutePath());
        }
        String epubFilename = epubFile.getName();
        String epubFilenameBase = epubFilename.substring(0, epubFilename.length()-5);
        String pdfFilename = epubFilenameBase + ".pdf";

        File outputFile = new File(outputDir.getAbsolutePath() + File.separator + pdfFilename);

        epubIn = Epub.fromFile(epubPath);
        XhtmlHandler.setSourceEpub(epubIn);

        Opf opf = epubIn.getOpf();
        List<String> contentPaths = opf.spineHrefs();
        List<File> contentFiles = new ArrayList<File>();
        for (String path : contentPaths) {
            contentFiles.add(new File(epubIn.getContentRoot(),path));
        }
        Ncx ncx = epubIn.getNcx();
        
        List<NavPoint> ncxToc = new ArrayList<NavPoint>();
        if(ncx != null) {
        	ncxToc.addAll(ncx.getNavPointsFlat());
        }
        
        Tree<TocTreeNode> tocTree = TocTreeNode.buildTocTree(ncx);
        XhtmlHandler.setTocTree(tocTree);
        
        Document doc = new Document();
        boolean pageSizeOK = doc.setPageSize(pageSize);
        boolean marginsOK = doc.setMargins(marginLeftPt, marginRightPt, marginTopPt, marginBottomPt);

        System.err.println("Writing PDF to " + outputFile.getAbsolutePath());
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outputFile));
        writer.setStrictImageSequence(true);
        PdfOutline bookmarkRoot = null;

        if (!(pageSizeOK && marginsOK)) {
            throw new RuntimeException("Failed to set PDF page size a/o margins");
        }
        
        int fileCount = contentFiles.size();
        printlnerr("Processing " + fileCount + " HTML file(s): ");
        int currentFile = 0;

        for (File file : contentFiles) {
        	currentFile++;
        	
        	char progressChar;
        	
        	int mod10 = currentFile % 10;
        	if(mod10 == 5)
        		progressChar = '5';
        	else if(mod10 == 0) 
        		progressChar = '0';
        	else
        		progressChar = '.';
        	
        	printerr(progressChar);
            if (!(doc.isOpen())) {
                doc.open();
                doc.newPage();
                bookmarkRoot = writer.getRootOutline();
                XhtmlHandler.setBookmarkRoot(bookmarkRoot);
            }
            NavPoint fileLevelNP = Ncx.findNavPoint(ncxToc, file.getName());
            TreeNode<TocTreeNode> npNode = TocTreeNode.findInTreeByNavPoint(tocTree, fileLevelNP);
            
            if(fileLevelNP != null) {
            	doc.newPage();
            	PdfOutline pdfOutlineParent = bookmarkRoot;
            	if(npNode != null) {
            		TreeNode<TocTreeNode> parent = npNode.getParent();
            		if(parent != null) {
            			TocTreeNode parentTTN = parent.getValue();
            			if(parentTTN != null && parentTTN.getPdfOutline() != null) {
            				pdfOutlineParent = parentTTN.getPdfOutline();
            			}
            		}
            	}
            	
            	PdfDestination here = new PdfDestination(PdfDestination.FIT);
            	PdfOutline pdfTocEntry = new PdfOutline(pdfOutlineParent, here, fileLevelNP.getNavLabelText());
            	if(npNode != null) {
            		npNode.getValue().setPdfDestination(here);
            		npNode.getValue().setPdfOutline(pdfTocEntry);
            	}
            }
            XhtmlHandler.process(file.getCanonicalPath(), doc);
        }
        printlnerr();

        doc.close();
        System.err.println("PDF written to " + outputFile.getAbsolutePath());
        epubIn.cleanup();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
       if (args.length < 1)
            usage();
        else {
            for (String infile : args) {
            	System.err.println();
            	try {
                    Converter c = new Converter();
                    c.applyProperties(epub2pdfProps);
                    c.convert(infile);
            	} catch(Exception e) {
            		System.err.println("Exception converting " + infile + ":");
            		System.err.println("\t" + e.getClass().getSimpleName() + " at:");
            		StackTraceElement topElem = e.getStackTrace()[0];
            		System.err.print("\t\t" + topElem.getClassName() + ".");
            		System.err.print(topElem.getMethodName());
            		int line = topElem.getLineNumber();
            		if(line > 9)
            			System.err.print(":" + topElem.getLineNumber());
            		System.err.println();
            	}
            }
        }
    }

    public static void usage() {
        println("Usage: com.amphisoft.epub2pdf.Converter <path-to-epub>");
    }
    
    /**
     * @return a new Properties object populated with the properties 
     * as read from the epub2pdf.properties file.
     */
    public static Properties getProperties() {
    	return new Properties(epub2pdfProps);
    }
}
