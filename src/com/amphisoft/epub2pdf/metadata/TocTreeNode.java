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
package com.amphisoft.epub2pdf.metadata;

import java.util.ArrayList;
import java.util.List;

import com.amphisoft.util.jgtree.ImmutableTree;
import com.amphisoft.util.jgtree.ImmutableTreeNode;
import com.amphisoft.util.jgtree.Tree;
import com.amphisoft.util.jgtree.TreeNode;

import com.amphisoft.epub.metadata.Ncx;
import com.amphisoft.epub.metadata.Ncx.NavPoint;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfOutline;

public class TocTreeNode {

	NavPoint navPoint;
	PdfDestination pdfDestination = null;
	PdfOutline pdfOutline = null;
	
	public TocTreeNode(NavPoint nP) {
		navPoint = nP;
	}
	
	public PdfDestination getPdfDestination() {
		return pdfDestination;
	}

	public void setPdfDestination(PdfDestination d) {
		if(pdfDestination != null)
			throw new RuntimeException(new IllegalAccessException());
		pdfDestination = d;
	}

	public PdfOutline getPdfOutline() {
		return pdfOutline;
	}
	
	public void setPdfOutline(PdfOutline o) {
		if(pdfOutline != null) {
			throw new RuntimeException(new IllegalAccessException());
		}
		pdfOutline = o;
	}
	
	public static Tree<TocTreeNode> buildTocTree(Ncx ncx) {
        List<NavPoint> ncxTocNested = new ArrayList<NavPoint>();
        if(ncx != null) {
        	ncxTocNested.addAll(ncx.getNavPointsNested());
        }
        Tree<TocTreeNode> tocTree;
        if(ncxTocNested.size() == 1) {
        	TocTreeNode rootTocTreeNode = new TocTreeNode(ncxTocNested.get(0));
        	tocTree = new ImmutableTree<TocTreeNode>(rootTocTreeNode);
        }
        else {
        	tocTree = new ImmutableTree<TocTreeNode>();
        	for(NavPoint npTopLevel : ncxTocNested) {
        		TreeNode<TocTreeNode> childNode = 
        			new ImmutableTreeNode<TocTreeNode>(new TocTreeNode(npTopLevel));
        		tocTree.getRoot().addChild(childNode);
        	}
        	for(TreeNode<TocTreeNode> nodeTopLevel : tocTree.getRoot().getChildren()) {
        		TocTreeNode.addChildren(nodeTopLevel);
        	}
        }
		return tocTree;
	}

	public static TreeNode<TocTreeNode> findInTreeByNavPoint(Tree<TocTreeNode> tocTree, NavPoint nP) {
		List<TreeNode<TocTreeNode>> tocTreeNodeList = tocTree.getNodesAsList();
		for(TreeNode<TocTreeNode> node : tocTreeNodeList) {
			if(checkNode(node,nP)) {
				return node;
			}
		}
		return null;
	}
	
	
	private static boolean checkNode(TreeNode<TocTreeNode> node, NavPoint nP) {
		TocTreeNode ttn = node.getValue();
		if(ttn != null && ttn.navPoint == nP) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void addChildren(TreeNode<TocTreeNode> parent) {
		List<NavPoint> npChildren = new ArrayList<NavPoint>();
		NavPoint nP = parent.getValue().navPoint;
		if(nP != null) {
			npChildren.addAll(nP.children());
		}
		addChildren(parent,npChildren);
	}
	public static void addChildren(TreeNode<TocTreeNode> parent, List<NavPoint> childNPs) {
		for(NavPoint value : childNPs) {
			TreeNode<TocTreeNode> newParent = parent.addChild(new TocTreeNode(value));
			addChildren(newParent, value.children());
		}
	}
}
