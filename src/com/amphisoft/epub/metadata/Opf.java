/*
epub2pdf, version 0.4 - Copyright 2010 Brendan C. LeFebvre

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amphisoft.util.xml.EnhancedDocument;
import com.amphisoft.util.xml.EnhancedNode;
import com.amphisoft.util.xml.NamespaceAttribute;
import com.amphisoft.util.xml.SpecifiedAttribute;
import static com.amphisoft.util.Print.*;

/**
 * The OPF metadata component of an EPUB. <br />
 * At minimum, the OPF specifies: <br />
 * the <em>metadata</em>, various pieces of information about the document as a whole, such as its title, author, and language, <br />
 * the <em>manifest</em>, an inventory of content and metadata files, and <br />
 * the <em>spine</em> or "reading order", an ordered list of content files, all of which must appear as manifest items (but not all content items in the manifest must necessarily be specified in the spine).
 * @author brendanl
 *
 */
public class Opf {

    private Document doc = null;

    private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder docBuilder;

    {
        try {
            docBuilder  = docBuilderFactory.newDocumentBuilder();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private OpfMetadata metadata = new OpfMetadata();
    private Map<String, ManifestItem> manifestItems = new HashMap<String,ManifestItem>();
    private List<GuideItem> guideItems = new ArrayList<GuideItem>();
    private List<SpineItem> spineItems = new ArrayList<SpineItem>();

    // appears as "unique-identifier" attrib of package tag
    // AND "id" attrib of dc:identifier tag
    // furthermore, must match uniqueId in NCX meta.dtb:uid (
    //   (or must it? I have an EPUB where OPF/NCX don't match yet it validates)
    private String uniqueId = "";

    private String tocHref;

    public String getUniqueId() {
        return uniqueId;
    }

    public String manifestHrefFromId(String i) {
        ManifestItem mI = manifestItems.get(i);
        if (mI == null) {
            return null;
        } else {
            return mI.href;
        }
    }

    public boolean sanityCheck() {
        boolean sane = true;
        for (SpineItem sI : spineItems) {
            String idRef = sI.idref;
            boolean found = false;
            ManifestItem mI = manifestItems.get(idRef);
            if (mI != null) {
                found = true;
            }
            if (!(found)) {
                System.err.println("Spine idref " + idRef + " not in manifest");
                sane = false;
            }
        }
        if (tocHref == null) {
            sane = false;
        }
        return sane;
    }

    public List<String> spineHrefs() {
        List<String> hrefs = new ArrayList<String>();
        for (SpineItem sI : spineItems) {
            String href = manifestItems.get(sI.idref).href;
            if (href != null)
                hrefs.add(href);
        }
        return hrefs;
    }

    public String tocHref() {
        return tocHref;
    }

    private void initializeDOMDocument() {
        doc = docBuilder.newDocument();
    }

    public OpfContentHandler getContentHandler() {
        return new OpfContentHandler();
    }

    public OpfMetadata getMetadata() {
        return metadata;
    }

    public org.w3c.dom.Document toDOM() {
        try {
            initializeDOMDocument();
            System.out.println(doc.toString());
            return doc;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public class OpfContentHandler extends org.xml.sax.helpers.DefaultHandler {
        @Override
        public void startElement (String uri, String localName,
                                  String qName, Attributes atts)
        throws SAXException {
            if ("navPoint".equals(qName)) {
            }
            if ("meta".equals(qName)) {
            }
        }

        @Override
        public void endElement (String uri, String localName,
                                String qName)
        throws SAXException {
            if ("navPoint".equals(qName)) {
            }
        }
    }

    public String getDcTitle() {
    	String dcTitle = "";
    	for(MetadataItem mI : metadata.items) {
    		if("dc:title".equals(mI.name)) {
    			dcTitle = mI.textContent;
    			break;
    		}
    	}
    	return dcTitle;
    }
    
    public static Opf fromFile(String path) {

        try {
            File opfFile = new File(path);
            if (opfFile.canRead()) {
                Opf opf = new Opf();
                org.apache.xerces.parsers.SAXParser parser = new org.apache.xerces.parsers.SAXParser();
                parser.setContentHandler(opf.getContentHandler());
                parser.parse(new InputSource(path));

                DOMParser domParser = new DOMParser();
                domParser.parse(path);
                EnhancedDocument opfDoc = new EnhancedDocument(domParser.getDocument());
                EnhancedNode opfPackageNode = new EnhancedNode(opfDoc.getFirstChildNamed("package"));

                Node mdN = opfPackageNode.getFirstChildNamed("metadata");
                EnhancedNode metadataNode = new EnhancedNode(mdN);

                Node mfN = opfPackageNode.getFirstChildNamed("manifest");
                EnhancedNode manifestNode = new EnhancedNode(mfN);

                Node spN = opfPackageNode.getFirstChildNamed("spine");
                EnhancedNode spineNode = new EnhancedNode(spN);


                EnhancedNode guideNode = null;
                if (opfPackageNode.getFirstChildNamed("guide") != null) {
                    guideNode = new EnhancedNode(opfPackageNode.getFirstChildNamed("guide"));
                }

                opf.setMetadata(metadataNode);
                opf.setManifest(manifestNode);
                opf.setSpine(spineNode);
                if (guideNode != null) {
                    opf.setGuide(guideNode);
                }

                opf.sanityCheck();
                return opf;
            } else {
                System.err.println("Failed to read OPF at " + path);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setMetadata(Node metadataNode) {
        NamedNodeMap attrs = metadataNode.getAttributes();
        int attrCount = attrs.getLength();
        for (int i = 0; i < attrCount; i++) {
            Node attr = attrs.item(i);

            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();
            if (attrName.startsWith("xmlns")) {
                String nsLocalName = attr.getLocalName();
                metadata.namespaceAttributes.add(new NamespaceAttribute(nsLocalName,attrValue));
            } else if (attrName.equals("id")) {
                metadata.optionalIdAttribute = new SpecifiedAttribute(attrName,attrValue);
            }
        }
        NodeList metanodeChildren = metadataNode.getChildNodes();
        List<Node> metaNodes = new ArrayList<Node>();
        for(int i = 0; i < metanodeChildren.getLength(); i++) {
        	Node n = metanodeChildren.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                metaNodes.add(n);
            }
        }
        for (Node mNode : metaNodes) {
        	String mdName = mNode.getNodeName();
        	String mdValue = "";
        	Node mdKid = mNode.getFirstChild();
        	if(mdKid != null && mdKid.getNodeType() == Node.TEXT_NODE) {
        		mdValue = mdKid.getNodeValue();
        	}
        	
            MetadataItem item = new MetadataItem(mdName,mdValue);
            metadata.items.add(item);
        }
    }

    private void setManifest(EnhancedNode manifestNode) {
        Iterable<Node> nodesAll = manifestNode.getChildNodesIterable();
        List<Node> manifestItemNodes = new ArrayList<Node>();
        for (Node n : nodesAll) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                manifestItemNodes.add(n);
            }
        }
        for (Node itemNode : manifestItemNodes) {
            NamedNodeMap attrs = itemNode.getAttributes();

            String id = attrs.getNamedItem("id").getNodeValue();
            String href = attrs.getNamedItem("href").getNodeValue();
            String mediaType = attrs.getNamedItem("media-type").getNodeValue();

            ManifestItem item = new ManifestItem(id,href,mediaType);
            manifestItems.put(id, item);
        }
    }

    private void setSpine(EnhancedNode spineNode) {

        String tocManifestItemId = "";

        NamedNodeMap spineAttrs = spineNode.getAttributes();
        Node spineTocAttr = spineAttrs.getNamedItem("toc");
        if (spineTocAttr != null)
            tocManifestItemId = spineAttrs.getNamedItem("toc").getNodeValue();
        else {
            for (ManifestItem mI : manifestItems.values()) {
                if (mI.mediaType.contains("ncx")) {
                    tocManifestItemId = mI.id;
                    break;
                }
            }
        }
        tocHref = manifestHrefFromId(tocManifestItemId);

        Iterable<Node> spineChildren = spineNode.getChildNodesIterable();
        List<Node> itemrefNodes = new ArrayList<Node>();
        for (Node n : spineChildren) {
            if ("itemref".equals(n.getNodeName())) {
                itemrefNodes.add(n);
            }
        }
        for (Node ref : itemrefNodes) {
            NamedNodeMap attrs = ref.getAttributes();
            String idRef = attrs.getNamedItem("idref").getNodeValue();
            SpineItem s = new SpineItem(idRef);
            spineItems.add(s);
        }
    }

    private void setGuide(EnhancedNode guideNode) {
        Iterable<Node> guideChildren = guideNode.getChildNodesIterable();
        List<Node> referenceNodes = new ArrayList<Node>();
        for (Node n : guideChildren) {
            if ("reference".equals(n.getNodeName())) {
                referenceNodes.add(n);
            }
        }
        for (Node ref : referenceNodes) {
        	NamedNodeMap attrs = ref.getAttributes();

        	Node hrefNode = attrs.getNamedItem("href");
        	String href = "";
        	if(hrefNode != null)
        		href = hrefNode.getNodeValue();
        	else
        		continue;
            
        	Node titleNode = attrs.getNamedItem("title");
        	String title = "";
            if(titleNode != null)
            	title = attrs.getNamedItem("title").getNodeValue();

            GuideItem g;
            
            String declaredMetadataType = "";
            Node typeNode = attrs.getNamedItem("type");
            if(typeNode != null)
            	declaredMetadataType = typeNode.getNodeValue();
            else
            	continue;
            
            GuideItemType giType = GuideItemType.getByAttrValue(
                                       declaredMetadataType);

            if (giType != null) {
                g = new GuideItem(href, giType, title);
                guideItems.add(g);
            } else {
                printlnerr("Opf: ignoring unrecognized guide item type " + declaredMetadataType);
            }

        }
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        sB.append("OPF:");

        int metadataCount = 0;
        if (metadata.items != null)
            metadataCount = metadata.items.size();
        sB.append(Integer.toString(metadataCount) + "md");
        sB.append(",");

        int manifestCount = 0;
        if (manifestItems != null)
            manifestCount = manifestItems.size();
        sB.append(Integer.toString(manifestCount) + "mf");
        sB.append(",");

        int spineCount = 0;
        if (spineItems != null)
            spineCount = spineItems.size();
        sB.append(Integer.toString(spineCount) + "sp");



        return sB.toString();
    }

    public static Opf fromFileBruteForce(String path) {
        try {
            Opf opf = new Opf();
            BufferedReader fR = new BufferedReader(new FileReader(new File(path)));
            String line = fR.readLine();
            while (line != null) {
                if (line.contains("<item ")) {
                    ManifestItem item = ManifestItem.fromLine(line);
                    opf.manifestItems.put(item.id, item);
                } else if (line.contains("<spine")) {
                    System.out.println(line);
                    Pattern p = Pattern.compile("spine toc=\"(.*?)\"");
                    Matcher m = p.matcher(line);
                    System.out.println(m.find());
                    String tocItem = m.group(1);
                    opf.tocHref = opf.manifestHrefFromId(tocItem);
                } else if (line.contains("<itemref ")) {
                    opf.spineItems.add(SpineItem.fromLine(line));
                }
                line = fR.readLine();
            }
            opf.sanityCheck();
            return opf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static class SpineItem {
        public final String idref;

        public static SpineItem fromLine(String line) {
            //System.out.println(line);
            String idRefDeclaration = "idref=\"";
            int idRefFirstIdx = line.lastIndexOf(idRefDeclaration) + idRefDeclaration.length();
            String tail = line.substring(idRefFirstIdx);
            int idRefEndIdx = tail.indexOf("\"");
            String i = tail.substring(0, idRefEndIdx);
            //System.out.println(i);
            return new SpineItem(i);
        }

        public SpineItem(String i) {
            idref = i;
        }

        @Override
        public String toString() {
            StringBuilder sB = new StringBuilder();
            sB.append("    <itemref idref=\"");
            sB.append(idref);
            sB.append("\"/>");
            return sB.toString();
        }
    }

    public static enum GuideItemType {
        COVER("cover"),
        TITLE_PAGE("title-page"),
        TABLE_OF_CONTENTS("toc"),
        INDEX("index"),
        GLOSSARY("glossary"),
        ACKNOWLEDGEMENTS("acknowledgements"),
        BIBLIOGRAPHY("bibliography"),
        COLOPHON("colophon"),
        COPYRIGHT_PAGE("copyright-page"),
        DEDICATION("dedication"),
        EPIGRAPH("epigraph"),
        FOREWORD("foreword"),
        LIST_OF_ILLUSTRATIONS("loi"),
        LIST_OF_TABLES("lot"),
        NOTES("notes"),
        PREFACE("preface"),
        TEXT("text"),
        ;

        private final String typeString;

        private GuideItemType(String s) {
            typeString = s;
        }

        public static GuideItemType getByAttrValue(String s) {
            for (GuideItemType t : GuideItemType.values()) {
                if (t.typeString.equals(s)) {
                    return t;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return typeString;
        }
    }



    public static class GuideItem {
        public final String href;
        public final GuideItemType type;
        public final String title;

        /**
         *
         * @param h path to this item within the EPUB
         * @param ty type of this item
         * @param ti title of this item as displayed on the user's reading system
         */
        public GuideItem(String h, GuideItemType ty, String ti) {
            href = h;
            type = ty;
            title = ti;
        }

        @Override
        public String toString() {
            StringBuilder sB = new StringBuilder();
            sB.append("<reference href=\"");
            sB.append(href);
            sB.append("\" type=\"");
            sB.append(type.toString());
            sB.append("\" title=\"");
            sB.append(title);
            sB.append("\" />");

            return sB.toString();
        }
    }


    public static class ManifestItem {
        public final String href;
        public final String id;
        public final String mediaType;

        public static ManifestItem fromLine(String line) {
            // System.out.println(line);

            String hrefDecl = "href=\"";
            String idDecl = "id=\"";
            String mediaTypeDecl = "media-type=\"";

            int hrefStartIdx = line.indexOf(hrefDecl) + hrefDecl.length();
            int idStartIdx = line.indexOf(idDecl) + idDecl.length();
            int mediaTypeStartIdx = line.indexOf(mediaTypeDecl) + mediaTypeDecl.length();

            String tail1 = line.substring(hrefStartIdx);
            int hrefCloseQuoteIdx = tail1.indexOf("\"");
            String href = tail1.substring(0,hrefCloseQuoteIdx);

            String tail2 = line.substring(idStartIdx);
            int idCloseQuoteIdx = tail2.indexOf("\"");
            String id = tail2.substring(0,idCloseQuoteIdx);

            String tail3 = line.substring(mediaTypeStartIdx);
            int mediaTypeCloseQuoteIdx = tail3.indexOf("\"");
            String mediaType = tail3.substring(0,mediaTypeCloseQuoteIdx);

            //System.out.println(href + " " + id + " " + mediaType);

            return new ManifestItem(id,href,mediaType);
        }

        public ManifestItem(String i, String h, String m) {
            id = i;
            href = h;
            mediaType = m;
        }

        @Override
        public String toString() {
            StringBuilder sB = new StringBuilder();
            sB.append("    <item id=\"");
            sB.append(id);
            sB.append("\" href=\"");
            sB.append(href);
            sB.append("\" media-type=\"");
            sB.append(mediaType);
            sB.append("\"/>");
            return sB.toString();
        }
    }

    public class OpfMetadata {
        List<NamespaceAttribute> namespaceAttributes =
            new ArrayList<NamespaceAttribute>();
        SpecifiedAttribute optionalIdAttribute = null;
        List<MetadataItem> items =
            new ArrayList<MetadataItem>();
    }

    class MetadataItem {
        public String name;
        List<SpecifiedAttribute> attributes =
            new ArrayList<SpecifiedAttribute>();
        public String textContent;

        MetadataItem(String n, String c) {
            name = n;
            textContent = c;
        }

        public void addAttribute(SpecifiedAttribute attr) {
            attributes.add(attr);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String args[]) {
        Opf o = Opf.fromFile("data/pg-dorian-gray/174/content.opf");
        Document d = o.toDOM();
        com.amphisoft.util.Print.printDOMDocument(d);
    }
}
