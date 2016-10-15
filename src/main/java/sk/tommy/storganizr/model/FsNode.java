package sk.tommy.storganizr.model;

import java.util.ArrayList;
import java.util.List;

public class FsNode {

	public final String id;
	public final String label;
	public final boolean isLeaf;
	public final List<FsNode> children	= new ArrayList<>();

	public FsNode(String id, String label, boolean isLeaf) {
		this.id = id;
		this.label = label;
		this.isLeaf = isLeaf;
	}

}
