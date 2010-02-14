/*
epub2pdf, version 0.3 - Copyright 2010 Brendan C. LeFebvre

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
package com.amphisoft.epub.metadata;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The NCX "table of contents" metadata component of an EPUB.
 * @author brendanl
 *
 */
public class Ncx {

    private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder docBuilder;
    private DocumentType ncxDocType;
    private ResolvingXMLReader xmlReader;

    public Ncx() { this("etc/ncx/catalog.xml"); }
    private Ncx(String catalogPath)
    {
    	CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
    	xmlReader = new ResolvingXMLReader();
    	
        try {
        	xmlReader.getCatalog().parseCatalog(catalogPath);
            docBuilder  = docBuilderFactory.newDocumentBuilder();
            ncxDocType = docBuilder.getDOMImplementation().
                         createDocumentType(
                             "ncx", "-//NISO//DTD ncx 2005-1//EN",
                             "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private Document doc = null;
    private Element ncxHead = null;
    private Element ncxDocTitleText = null;

    File file;

    private List<MetaHeader> metaHeaders = new ArrayList<MetaHeader>();

    private int playOrderSpacing = 1;

    private String docTitleText = "";

    List<NavPoint> ncxNavMap = new ArrayList<NavPoint>();
    List<NavPoint> ncxNavPointsFlat = new ArrayList<NavPoint>();

    public List<NavPoint> getNavPointsNested() {
        return new ArrayList<NavPoint>(ncxNavMap);
    }
    public List<NavPoint> getNavPointsFlat() {
        return new ArrayList<NavPoint>(ncxNavPointsFlat);
    }


    public void appendNavPoint(NavPoint navPoint) {
        ncxNavMap.add(navPoint);
    }

    public int getPlayOrderSpacing() {
        return playOrderSpacing;
    }

    public void setPlayOrderSpacing(int i) {
        playOrderSpacing = i;
    }

    private void initializeDOMDocument() {
    	docBuilder.setEntityResolver(xmlReader.getEntityResolver());
        doc = docBuilder.getDOMImplementation().createDocument("http://www.daisy.org/z3986/2005/ncx/", "ncx", ncxDocType);
        Element ncxRoot = doc.getDocumentElement();
        ncxRoot.setAttribute("xmlns", "http://www.daisy.org/z3986/2005/ncx/");
        ncxRoot.setAttribute("version", "2005-1");
        ncxRoot.setAttribute("xml:lang", "en");

        System.err.println(ncxDocType);

        ncxHead = doc.createElement("head");

        Element ncxDocTitle = doc.createElement("docTitle");
        ncxDocTitleText = doc.createElement("text");
        ncxDocTitle.appendChild(ncxDocTitleText);
    }

    public org.w3c.dom.Document toDOM() {
        try {
            initializeDOMDocument();
            ncxDocTitleText.setTextContent(docTitleText);
            for (MetaHeader mH : metaHeaders) {
                Element metaE = doc.createElement("meta");
                metaE.setAttribute("name", mH.name);
                metaE.setAttribute("content", mH.content);
                ncxHead.appendChild(metaE);
            }
            doc.getDocumentElement().appendChild(ncxHead);
            return doc;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Set<Node> processedNavPoints = new HashSet<Node>();

    public void processNavPointXml(Node n) {
        processNavPointXml(n, 0);
    }

    public void processNavPointXml(Node n, int depth) {
        if (processedNavPoints.contains(n)) {
            return;
        } else {
            processedNavPoints.add(n);
        }

        List<Node> childNavPointNodes = new ArrayList<Node>();

        String id = "";
        String labelText = "";
        String src = "";

        NamedNodeMap navPointAttribs = n.getAttributes();
        id = navPointAttribs.getNamedItem("id").getNodeValue();

        NodeList children = n.getChildNodes();
        int childCount = children.getLength();
        for (int c = 0; c < childCount; c++) {
            Node child = children.item(c);
            if (child.getNodeType() != org.w3c.dom.Node.TEXT_NODE) {
            if (child.getLocalName().equals("navLabel")) {
                NodeList labelChildren = child.getChildNodes();
                int labelChildCount = labelChildren.getLength();
                LABELCHILDREN:
                for (int d = 0; d < labelChildCount; d++) {
                    Node tN = labelChildren.item(d);
                    if (tN.getNodeType() != org.w3c.dom.Node.TEXT_NODE && tN.getLocalName().equals("text")) {
                        labelText = tN.getTextContent();
                        break LABELCHILDREN;
                    }
                }

                for (int t = 0; t < depth; t++) {
                    //System.out.print(" ");
                }
            } else if (child.getLocalName().equals("content")) {
                NamedNodeMap contentAttribs = child.getAttributes();
                src = contentAttribs.getNamedItem("src").getNodeValue();
            } else if (child.getLocalName().equals("navPoint")) {
                childNavPointNodes.add(child);
            }
            }
        }
        NavPoint newNavPoint = new NavPoint(id,labelText,src);
        ncxNavPointsFlat.add(newNavPoint);
        if (depth == 0) {
            ncxNavMap.add(newNavPoint);
        } else {
            Node parentNavPointNode = n.getParentNode();
            NamedNodeMap parentNavPointAttrs = parentNavPointNode.getAttributes();
            String parentId = parentNavPointAttrs.getNamedItem("id").getNodeValue();
            for (NavPoint nP : ncxNavPointsFlat) {
                if (nP.id.equals(parentId)) {
                    nP.appendChild(newNavPoint);
                    break;
                }
            }
        }
        for (Node child : childNavPointNodes) {
            processNavPointXml(child, depth+1);
        }
    }

    /**
     * Lookup NavPoint by string consisting of relative content filename and (optionally) anchor
     * in standard URL format ("filename.xhtml#anchor").
     * @param s
     * @return
     */
    public NavPoint findNavPoint(String s) {
        return Ncx.findNavPoint(ncxNavMap, s);
    }

    /**
     * Lookup NavPoint by string consisting of relative content filename and (optionally) anchor
     * in standard URL format ("filename.xhtml#anchor").
     * @param navPoints
     * @param s
     * @return
     */
    public static NavPoint findNavPoint(List<NavPoint> navPoints, String s) {
        for (NavPoint nP : navPoints) {
            if (nP.contentSrc.equals(s)) {
                return nP;
            } else {
                NavPoint n2 = findNavPoint(nP.children(), s);
                if (n2 != null)
                    return n2;
            }
        }
        return null;
    }

    public ContentHandler getContentHandler() {
        return new ContentHandler();
    }

    public void printNavPointTree() {
        printNavPointTree(this.ncxNavMap);
    }

    public static void printNavPointTree(List<NavPoint> navPoints) {
        for (NavPoint nP : navPoints) {
            System.out.print(nP.id + "\t");
            for (int i = 1; i < nP.getHierarchyLevel(); i++)
                System.out.print("\t");
            System.out.println(nP.getNavLabelText());
            printNavPointTree(nP.children());
        }
    }


    public static Ncx fromFile(String path) {
        File ncxFile = new File(path);

        if (ncxFile.canRead()) {
            try {
                Ncx ncx = new Ncx();
                ncx.file = ncxFile;
                org.apache.xerces.parsers.SAXParser parser = new org.apache.xerces.parsers.SAXParser();

                parser.setContentHandler(ncx.getContentHandler());
                parser.setEntityResolver(ncx.xmlReader.getEntityResolver());
                parser.setDTDHandler(ncx.xmlReader.getDTDHandler());
                parser.parse(new InputSource(path));

                DOMParser domParser = new DOMParser();
                domParser.setEntityResolver(ncx.xmlReader.getEntityResolver());
                domParser.parse(path);
                org.w3c.dom.Document ncxDoc = domParser.getDocument();
                NodeList navPoints = ncxDoc.getElementsByTagNameNS("*","navPoint");

                int navPointCount = navPoints.getLength();

                for (int i = 0; i < navPointCount; i++) {
                    ncx.processNavPointXml(navPoints.item(i));
                }

                return ncx;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.err.println("Ncx: cannot read from " + path);
            return null;
        }
    }

    public void setDocTitleText(String s) {
        docTitleText = s;
    }

    public static class NavPoint {

        private String id = "";
        private String navLabelText = "";
        private String contentSrc = "";
        private int hierarchyLevel = 1;
        private int playOrder = 0;
        private NavPoint parentNavPoint = null;
        private List<NavPoint> children = new ArrayList<NavPoint>();
        private SourceType srcType = SourceType.UNKNOWN;
        private String filenameBase = "";
        private String anchorId = "";

        public NavPoint(String navPointID, String labelText, String srcPath) {
            id = navPointID;
            navLabelText = labelText;
            contentSrc = srcPath;
            setSourceInfo();
        }

        public int getHierarchyLevel() {
            return hierarchyLevel;
        }

        public void appendChild(NavPoint navPoint) {
            navPoint.setParent(this);
            navPoint.hierarchyLevel = this.getHierarchyLevel() + 1;
            children.add(navPoint);
        }

        private void setParent(NavPoint parent) {
            parentNavPoint = parent;
        }

        void setPlayOrder(int i) {
            playOrder = i;
        }

        public NavPoint getParent() {
            return parentNavPoint;
        }

        public String id() {
            return id;
        }

        public String getNavLabelText() {
            return navLabelText;
        }

        public String getSourcePath() {
            return contentSrc;
        }

        public int getPlayOrderIndex() {
            return playOrder;
        }

        public List<NavPoint> children() {
            return new ArrayList<NavPoint>(children);
        }

        private void setSourceInfo() {
            Pattern afterLastSlashPattern = Pattern.compile(".*?/{0,1}(.*)$");
            Matcher m1 = afterLastSlashPattern.matcher(contentSrc);
            if (m1.find()) {
                String contentSrcBase = m1.group(1);
                Pattern anchorPattern = Pattern.compile(".*?/{0,1}(.*)#(.*)$");
                Matcher m2 = anchorPattern.matcher(contentSrcBase);
                boolean found = m2.find();
                if (!found) {
                    srcType = SourceType.FILENAME;
                    filenameBase = contentSrcBase;
                } else {
                    srcType = SourceType.ELEMENT_ID;
                    filenameBase = m2.group(1);
                    anchorId = m2.group(2);
                }
            }
        }

        public SourceType getSourceType() {
            return srcType;
        }

        public String getAnchorId() {
            return anchorId;
        }

        @Override
        public String toString() {
            return "NavPoint " + id;
        }

        enum SourceType {
            UNKNOWN,
            FILENAME,
            ELEMENT_ID;
        }
    }

    public static class MetaHeader {
        public final String name;
        public final String content;

        public MetaHeader(String n, String c) {
            name = n;
            content = c;
        }

        @Override
        public String toString() {
            StringBuilder sB = new StringBuilder();
            sB.append("<meta name=\"");
            sB.append(name);
            sB.append("\" content=\"");
            sB.append(content);
            sB.append("\" />");
            return sB.toString();
        }
    }


    public static void main(String[] args) {
        String dataPath;
        if (args.length < 1) {
            dataPath = "data/pg-dorian-gray/174/toc.ncx";
        } else {
            dataPath = args[0];
        }

        try {
            Ncx toc = Ncx.fromFile(dataPath);

            /*
            Document doc = toc.toDOM();
            com.amphisoft.util.Print.printDOMDocument(doc);

            toc.removeNavPointById("np-7");
            toc.removeNavPointById("np-1");
            toc.removeNavPointById("np-3");
            toc.removeNavPointById("np-4");
            Ncx.printNavPointTree(toc.ncxNavMap);
            */

            List<NavPoint> nPList = toc.getNavPointsFlat();
            for (NavPoint nP : nPList) {
                String level = Integer.toString(nP.hierarchyLevel);
                String name = nP.navLabelText;
                System.out.println(level + "\t" + name + "\t" + nP.srcType + "\t" + nP.filenameBase + "\t" + nP.anchorId);
            }




        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public class ContentHandler extends org.xml.sax.helpers.DefaultHandler {

        @Override
        public void startElement (String uri, String localName,
                                  String qName, Attributes atts)
        throws SAXException {
            if ("meta".equals(qName)) {
                String name = atts.getValue("name");
                String content = atts.getValue("content");
                MetaHeader mH = new MetaHeader(name,content);
                metaHeaders.add(mH);
            }
        }
    }

    @Override
    public String toString() {
        return "Ncx:" + this.ncxNavPointsFlat.size() + "np";
    }

}
