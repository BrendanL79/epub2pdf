package com.amphisoft.util.jgtree;

import java.util.*;

public interface TreeNode<N> {
	N getValue();
	
	List<TreeNode<N>> getChildren();
	TreeNode<N> getParent();	
	Tree<N> getOwningTree();
	void setOwningTree(Tree<N> tree);
	
	int childCount();
	
	int getLevel();
	
	boolean addChild(TreeNode<N> childNode);
	TreeNode<N> addChild(N childData);
}
