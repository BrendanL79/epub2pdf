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
package com.amphisoft.util.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

/**
 * Adds convenience lookup and Iterable-returning methods to a standard org.w3c.dom.Document.
 * @author brendanl
 *
 */
public class EnhancedDocument extends EnhancedNode implements Document {

    private Document doc;

    public EnhancedDocument(Document d) {
        super(d);
        doc = d;
    }

    /**
     * @param name Node name to look up.
     * @return first child node of this document with the given name, or null if none found.
     */
    @Override
    public Node getFirstChildNamed(String name) {
        for (Node n : getChildNodesIterable()) {
            if (name.equals(n.getNodeName())) {
                return n;
            }
        }
        return null;
    }

    @Override
    public Node adoptNode(Node source) throws DOMException {
        return doc.adoptNode(source);
    }

    @Override
    public Attr createAttribute(String name) throws DOMException {
        return doc.createAttribute(name);
    }

    @Override
    public Attr createAttributeNS(String namespaceURI, String qualifiedName)
    throws DOMException {
        return doc.createAttributeNS(namespaceURI, qualifiedName);
    }

    @Override
    public CDATASection createCDATASection(String data) throws DOMException {
        return doc.createCDATASection(data);
    }

    @Override
    public Comment createComment(String data) {
        return doc.createComment(data);
    }

    @Override
    public DocumentFragment createDocumentFragment() {
        return doc.createDocumentFragment();
    }

    @Override
    public Element createElement(String tagName) throws DOMException {
        return doc.createElement(tagName);
    }

    @Override
    public Element createElementNS(String namespaceURI, String qualifiedName)
    throws DOMException {
        return doc.createElementNS(namespaceURI, qualifiedName);
    }

    @Override
    public EntityReference createEntityReference(String name)
    throws DOMException {
        return doc.createEntityReference(name);
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target,
            String data) throws DOMException {
        return doc.createProcessingInstruction(target, data);
    }

    @Override
    public Text createTextNode(String data) {
        return doc.createTextNode(data);
    }

    @Override
    public DocumentType getDoctype() {
        return doc.getDoctype();
    }

    @Override
    public Element getDocumentElement() {
        return doc.getDocumentElement();
    }

    @Override
    public String getDocumentURI() {
        return doc.getDocumentURI();
    }

    @Override
    public DOMConfiguration getDomConfig() {
        return doc.getDomConfig();
    }

    @Override
    public Element getElementById(String elementId) {
        return doc.getElementById(elementId);
    }

    @Override
    public NodeList getElementsByTagName(String tagname) {
        return doc.getElementsByTagName(tagname);
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return doc.getElementsByTagNameNS(namespaceURI, localName);
    }

    @Override
    public DOMImplementation getImplementation() {
        return doc.getImplementation();
    }

    @Override
    public String getInputEncoding() {
        return doc.getInputEncoding();
    }

    @Override
    public boolean getStrictErrorChecking() {
        return doc.getStrictErrorChecking();
    }

    @Override
    public String getXmlEncoding() {
        return doc.getXmlEncoding();
    }

    @Override
    public boolean getXmlStandalone() {
        return doc.getXmlStandalone();
    }

    @Override
    public String getXmlVersion() {
        return doc.getXmlVersion();
    }

    @Override
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        return doc.importNode(importedNode, deep);
    }

    @Override
    public void normalizeDocument() {
        doc.normalizeDocument();
    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName)
    throws DOMException {
        return doc.renameNode(n, namespaceURI, qualifiedName);
    }

    @Override
    public void setDocumentURI(String documentURI) {
        doc.setDocumentURI(documentURI);
    }

    @Override
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        doc.setStrictErrorChecking(strictErrorChecking);
    }

    @Override
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        doc.setXmlStandalone(xmlStandalone);
    }

    @Override
    public void setXmlVersion(String xmlVersion) throws DOMException {
        doc.setXmlVersion(xmlVersion);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        Node appendedNode = doc.appendChild(newChild);
        return appendedNode;
    }

    @Override
    public Node cloneNode(boolean deep) {
        return doc.cloneNode(deep);
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return doc.compareDocumentPosition(other);
    }

    @Override
    public NamedNodeMap getAttributes() {
        return doc.getAttributes();
    }

    @Override
    public String getBaseURI() {
        return doc.getBaseURI();
    }

    @Override
    public NodeList getChildNodes() {
        return doc.getChildNodes();
    }

    @Override
    public Object getFeature(String feature, String version) {
        return doc.getFeature(feature, version);
    }

    @Override
    public Node getFirstChild() {
        return doc.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return doc.getLastChild();
    }

    @Override
    public String getLocalName() {
        return doc.getLocalName();
    }

    @Override
    public String getNamespaceURI() {
        return doc.getNamespaceURI();
    }

    @Override
    public Node getNextSibling() {
        return doc.getNextSibling();
    }

    @Override
    public String getNodeName() {
        return doc.getNodeName();
    }

    @Override
    public short getNodeType() {
        return doc.getNodeType();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return doc.getNodeValue();
    }

    @Override
    public Document getOwnerDocument() {
        return doc.getOwnerDocument();
    }

    @Override
    public Node getParentNode() {
        return doc.getParentNode();
    }

    @Override
    public String getPrefix() {
        return doc.getPrefix();
    }

    @Override
    public Node getPreviousSibling() {
        return doc.getPreviousSibling();
    }

    @Override
    public String getTextContent() throws DOMException {
        return doc.getTextContent();
    }

    @Override
    public Object getUserData(String key) {
        return doc.getUserData(key);
    }

    @Override
    public boolean hasAttributes() {
        return doc.hasAttributes();
    }

    @Override
    public boolean hasChildNodes() {
        return doc.hasChildNodes();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return doc.insertBefore(newChild, refChild);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return doc.isDefaultNamespace(namespaceURI);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return doc.isEqualNode(arg);
    }

    @Override
    public boolean isSameNode(Node other) {
        return doc.isSameNode(other);
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return doc.isSupported(feature, version);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return doc.lookupNamespaceURI(prefix);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return doc.lookupPrefix(namespaceURI);
    }

    @Override
    public void normalize() {
        doc.normalize();
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        Node removedNode = doc.removeChild(oldChild);
        return removedNode;
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        Node replacedNode = doc.replaceChild(newChild, oldChild);
        return replacedNode;
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        doc.setNodeValue(nodeValue);
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        doc.setPrefix(prefix);
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        doc.setTextContent(textContent);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        Object replacedData = doc.setUserData(key, data, handler);
        return replacedData;
    }

    @Override
    public String toString() {
        return doc.toString();
    }
}
