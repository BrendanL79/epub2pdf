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

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class NcxTreeViewer {

    public static void main(String[] args) {

        final String pathArg;
        if (args.length > 0) {
            pathArg = args[0];
        } else {
            pathArg = null;
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                NcxTreeFrame frame;
                if (pathArg != null) {
                    frame = new NcxTreeFrame(pathArg);
                } else {
                    frame = new NcxTreeFrame();
                }
                frame.setBounds(100, 100, 300, 600);
                frame.setVisible(true);
            }
        }
                              );

    }

}

class NcxTreeFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private DefaultMutableTreeNode base;
    private DefaultMutableTreeNode navMapBase;
    private TreeModel treeModel;
    private JTree treeView;
    private Ncx openNcx;

    public NcxTreeFrame() {
        this("");
    }

    public NcxTreeFrame(String ncxPath) {
        Ncx ncx;
        if (ncxPath.equals("")) {
            ncx = null;
        } else {
            ncx = Ncx.fromFile(ncxPath);
        }
        init(ncx);
    }

    public NcxTreeFrame(Ncx ncx) {
        init(ncx);
    }

    private void init(Ncx ncx) {
        initTree(ncx);
        this.getContentPane().add(new JScrollPane(treeView), BorderLayout.CENTER);
    }

    private void initTree(Ncx ncx) {
        openNcx = ncx;
        populateTree();
    }

    private void populateTree() {
        if (openNcx == null) {
            base = new DefaultMutableTreeNode("No NCX loaded");
        } else {
            base = new DefaultMutableTreeNode(openNcx.file.getPath());
        }

        navMapBase = new DefaultMutableTreeNode("navMap");

        base.add(navMapBase);

        treeModel = new DefaultTreeModel(base);

        treeView = new JTree(treeModel);
    }

    /*
    private void populateNavMapNode() {
    	DefaultMutableTreeNode current = navMapBase;

    	if(current != null) {
    		addChildNavPoints(current, openNcx.ncxNavMap);
    	}
    }

    private void addChildNavPoints(DefaultMutableTreeNode target, List<Ncx.NavPoint> navPoints) {
    	DefaultMutableTreeNode current = target;
    	DefaultMutableTreeNode parent = null;

    }
    */
}
