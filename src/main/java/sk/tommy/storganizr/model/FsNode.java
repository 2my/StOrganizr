package sk.tommy.storganizr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FsNode {

	public final String id;
	public final String label;
	public final boolean isLeaf;
	private final List<FsNode> children	= new ArrayList<>();

	public FsNode(String id, String label, boolean isLeaf) {
		this.id = id;
		this.label = label;
		this.isLeaf = isLeaf;
	}

	public void addChild(FsNode child) {
		children.add(child);
	}

	public List<FsNode> children() {
		return children;
	}

	public Map<String,FsNode> nodeLookup() {
		Map<String,FsNode> lookup	= new HashMap<>();
		lookup.put(id,  this);
		for(FsNode child: children)
			lookup.putAll(child.nodeLookup());
		return lookup;
	}

	@Override
	public String toString() {
		return "FsNode [id=" + id + ", label=" + label + ", isLeaf=" + isLeaf + ", children=" + children + "]";
	}
}
