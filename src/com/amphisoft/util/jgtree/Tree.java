package com.amphisoft.util.jgtree;

import java.util.List;

public interface Tree<N> {
	TreeNode<N> getRoot();
	int getNodeCount();
	int getDepth();
	void registerNode(TreeNode<N> node);
	List<TreeNode<N>> getNodesAsList();
}
