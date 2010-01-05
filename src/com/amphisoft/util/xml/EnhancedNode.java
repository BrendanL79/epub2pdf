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
package com.amphisoft.util.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * Adds convenience lookup and Iterable-returning methods to a standard org.w3c.dom.Node.
 * @author brendanl
 *
 */
public class EnhancedNode implements Node {

    private Node node;

    public EnhancedNode(Node n) {
        node = n;
    }

    /**
     * @param name Node name to look up.
     * @return first child node of this document with the given name, or null if none found.
     */
    public Node getFirstChildNamed(String name) {
        for (Node n : getChildNodesIterable()) {
            if (name.equals(n.getNodeName()) || name.equals(n.getLocalName())) {
                return n;
            }
        }
        return null;
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        Node appendedNode = node.appendChild(newChild);
        return appendedNode;
    }

    @Override
    public Node cloneNode(boolean deep) {
        return node.cloneNode(deep);
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return node.compareDocumentPosition(other);
    }

    @Override
    public NamedNodeMap getAttributes() {
        return node.getAttributes();
    }

    @Override
    public String getBaseURI() {
        return node.getBaseURI();
    }

    @Override
    public NodeList getChildNodes() {
        return node.getChildNodes();
    }

    public Iterable<Node> getChildNodesIterable() {
        List<Node> childrenIterable =
            new ArrayList<Node>();
        NodeList childNodes = node.getChildNodes();
        int childCount = childNodes.getLength();
        for (int i = 0; i < childCount; i++) {
            childrenIterable.add(childNodes.item(i));
        }
        return childrenIterable;
    }

    @Override
    public Object getFeature(String feature, String version) {
        return node.getFeature(feature, version);
    }

    @Override
    public Node getFirstChild() {
        return node.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return node.getLastChild();
    }

    @Override
    public String getLocalName() {
        return node.getLocalName();
    }

    @Override
    public String getNamespaceURI() {
        return node.getNamespaceURI();
    }

    @Override
    public Node getNextSibling() {
        return node.getNextSibling();
    }

    @Override
    public String getNodeName() {
        return node.getNodeName();
    }

    @Override
    public short getNodeType() {
        return node.getNodeType();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return node.getNodeValue();
    }

    @Override
    public Document getOwnerDocument() {
        return node.getOwnerDocument();
    }

    @Override
    public Node getParentNode() {
        return node.getParentNode();
    }

    @Override
    public String getPrefix() {
        return node.getPrefix();
    }

    @Override
    public Node getPreviousSibling() {
        return node.getPreviousSibling();
    }

    @Override
    public String getTextContent() throws DOMException {
        return node.getTextContent();
    }

    @Override
    public Object getUserData(String key) {
        return node.getUserData(key);
    }

    @Override
    public boolean hasAttributes() {
        return node.hasAttributes();
    }

    @Override
    public boolean hasChildNodes() {
        return node.hasChildNodes();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return node.insertBefore(newChild, refChild);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return node.isDefaultNamespace(namespaceURI);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return node.isEqualNode(arg);
    }

    @Override
    public boolean isSameNode(Node other) {
        return node.isSameNode(other);
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return node.isSupported(feature, version);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return node.lookupNamespaceURI(prefix);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return node.lookupPrefix(namespaceURI);
    }

    @Override
    public void normalize() {
        node.normalize();
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        Node removedNode = node.removeChild(oldChild);
        return removedNode;
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        Node replacedNode = node.replaceChild(newChild, oldChild);
        return replacedNode;
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        node.setNodeValue(nodeValue);
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        node.setPrefix(prefix);
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        node.setTextContent(textContent);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        Object replacedData = node.setUserData(key, data, handler);
        return replacedData;
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
