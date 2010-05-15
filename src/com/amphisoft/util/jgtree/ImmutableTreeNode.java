package com.amphisoft.util.jgtree;

import java.util.*;

public class ImmutableTreeNode<N> implements TreeNode<N> {

	private N _value;
	private List<TreeNode<N>> _children;
	private TreeNode<N> _parent;
	private Tree<N> _owner;
	private int _level;

	public ImmutableTreeNode(N value) {
		_value = value;
		_children = new ArrayList<TreeNode<N>>();
		_level = 0;
	}
	
	public ImmutableTreeNode(N value, TreeNode<N> parent) {
		this(value);
		if(parent == null) {
			throw new IllegalArgumentException();
		}
		_parent = parent;		
		_level = _parent.getLevel() + 1;
		_owner = _parent.getOwningTree();
	}

	public ImmutableTreeNode(N value, ImmutableTree<N> owner) {
		this(value);
		if(owner == null) {
			throw new IllegalArgumentException();
		}
		_owner = owner;
	}

	public void addChildren(Collection<N> childValues) {
		for(N value : childValues) {
			this.addChild(value);
		}
	}
	
	public TreeNode<N> addChild(N valueIn) {
		return addChild(new ImmutableTreeNode<N>(valueIn, this));
	}
	
	protected ImmutableTreeNode<N> addChild(ImmutableTreeNode<N> childNode) {
		childNode.setLevel(_level + 1);
		childNode.setOwningTree(this.getOwningTree());
		if(_children.add(childNode)) {
			_owner.registerNode(childNode);
			return childNode;
		}
		else {
			return null;
		}
	}
	
	@Override
	public int childCount() {
		return _children.size();
	}

	@Override
	public List<TreeNode<N>> getChildren() {
		List<TreeNode<N>> children = new ArrayList<TreeNode<N>>();
		children.addAll(_children);
		return children;
	}

	@Override
	public TreeNode<N> getParent() {
		return _parent;
	}

	@Override
	public int getLevel() {
		return _level;
	}

	private void setLevel(int i) {
		_level = i;
	}
	
	@Override
	public N getValue() {
		return _value;
	}
	
	@Override
	public boolean addChild(TreeNode<N> childNode) {
		if(!(childNode instanceof ImmutableTreeNode<?>)) {
			return false;
		}
		else {
			ImmutableTreeNode<N> immutableChildNode = (ImmutableTreeNode<N>) childNode;
			if(addChild(immutableChildNode) != null) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	@Override
	public Tree<N> getOwningTree() {
		return _owner;
	}

	@Override
	public void setOwningTree(Tree<N> tree) {
		if(_owner != null && _owner != tree) {
			throw new IllegalArgumentException();
		}
		_owner = tree;
	}
	
	

}
