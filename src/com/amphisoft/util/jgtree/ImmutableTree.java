package com.amphisoft.util.jgtree;

import java.util.ArrayList;
import java.util.List;

public class ImmutableTree<N> implements Tree<N> {

	private TreeNode<N> _root;
	private int _depth;
	private int _nodeCount;
	private List<TreeNode<N>> _nodeList = new ArrayList<TreeNode<N>>();
	
	public ImmutableTree(TreeNode<N> rootNode) {
		if(rootNode == null) {
			_root = new ImmutableTreeNode<N>(null, this);
		}
		else {
			_root = rootNode;
			_root.setOwningTree(this);
		}
		_nodeList.add(_root);
		_depth = 1;
		_nodeCount = 1;
	}

	public ImmutableTree(N rootContents) {
		this(new ImmutableTreeNode<N>(rootContents));
	}
	
	public ImmutableTree() {
		_root = new ImmutableTreeNode<N>(null, this);
		_nodeList.add(_root);
		_depth = 1;
		_nodeCount = 1;
	}

	@Override
	public int getDepth() {
		return _depth;
	}

	@Override
	public TreeNode<N> getRoot() {
		return _root;
	}

	@Override
	public int getNodeCount() {
		return _nodeCount;
	}
	
	void newNodeAtLevel(int el) {
		if(el+1 > _depth) {
			_depth = el+1;
		}
	}

	@Override
	public void registerNode(TreeNode<N> node) {
		TreeNode<N> parent = node.getParent();
		if(parent != null && parent.getOwningTree() != this) {
			throw new RuntimeException();
		}
		if(node.getOwningTree() != this) {
			throw new IllegalArgumentException();
		}
		newNodeAtLevel(node.getLevel());
		_nodeList.add(node);
		_nodeCount++;
	}

	public List<TreeNode<N>> getNodesAsList() {
		List<TreeNode<N>> nodeList = new ArrayList<TreeNode<N>>();
		nodeList.addAll(_nodeList);
		return nodeList;
	}
}
