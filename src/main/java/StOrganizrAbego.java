/** StOrganizrGraphStream
   Copyright 2016 Tommy Skodje

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.abego.treelayout.Configuration;
import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import sk.tommy.storganizr.model.DirTreeBuilder;
import sk.tommy.storganizr.model.FsNode;

public class StOrganizrAbego {

	private static void showInDialog(JComponent panel) {
		JDialog dialog = new JDialog();
		Container contentPane = dialog.getContentPane();
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
				10, 10, 10, 10));
		contentPane.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private static TreeForTreeLayout<TextInBox> getSampleTree(String treeName) {
		TreeForTreeLayout<TextInBox> tree;
		if (treeName.equals("2")) {
			tree = SampleTreeFactory.createSampleTree2();
		} else if (treeName.equals("")) {
			Path start = FileSystems.getDefault().getPath(".");
			FsNode root	= DirTreeBuilder.buildTree(start);
			tree = SampleTreeFactory.createSampleTree(root);
		} else {
			throw new RuntimeException(String.format("Invalid tree name: '%s'",
					treeName));
		}
		return tree;
	}

	/**
	 * Shows a dialog with a tree in a layout created by {@link TreeLayout},
	 * using the Swing component {@link TextInBoxTreePane}.
	 * 
	 * @param args args[0]: treeName (default="")
	 */
	public static void main(String[] args) {
		// get the sample tree
		String treeName = (args.length > 0) ? args[0] : "";
		TreeForTreeLayout<TextInBox> tree = getSampleTree(treeName);
				
		// setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		// Configura TextInBox
		Configuration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes, Location.Left);

		// create the NodeExtentProvider for TextInBox nodes
		TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		// create the layout
		TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
		showInDialog(panel);
	}

}

class SampleTreeFactory {

	public static TreeForTreeLayout<TextInBox> createSampleTree(FsNode rootNode) {
		TextInBox root = new TextInBox(rootNode.label, 40, 20);
		DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<>(root);
		addChildren(rootNode, root, tree);
		return tree;
	}

	private static void addChildren(FsNode rootNode, TextInBox root, DefaultTreeForTreeLayout<TextInBox> tree) {
		for (FsNode node : rootNode.children()) {
			TextInBox child	= new TextInBox(node.label, node.label.length() * 9, 20);
			tree.addChild(root, child);
			addChildren(node, child, tree);
		}
	}

	/**
	 * @return a "Sample" tree with {@link TextInBox} items as nodes.
	 */
	public static TreeForTreeLayout<TextInBox> createSampleTree() {
		TextInBox root = new TextInBox("root", 40, 20);
		TextInBox n1 = new TextInBox("n1", 30, 20);
		TextInBox n1_1 = new TextInBox("n1.1 (first node)", 80, 20);
		TextInBox n1_2 = new TextInBox("n1.2", 40, 20);
		TextInBox n1_3 = new TextInBox("n1.3 (last node)", 80, 20);
		TextInBox n2 = new TextInBox("n2", 30, 20);
		TextInBox n2_1 = new TextInBox("n2", 30, 20);

		DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(
				root);
		tree.addChild(root, n1);
		tree.addChild(n1, n1_1);
		tree.addChild(n1, n1_2);
		tree.addChild(n1, n1_3);
		tree.addChild(root, n2);
		tree.addChild(n2, n2_1);
		return tree;
	}
	
	/**
	 * @return a "Sample" tree with {@link TextInBox} items as nodes.
	 */
	public static TreeForTreeLayout<TextInBox> createSampleTree2() {
		TextInBox root = new TextInBox("prog", 40, 20);
		TextInBox n1 = new TextInBox("classDef", 65, 20);
		TextInBox n1_1 = new TextInBox("class", 50, 20);
		TextInBox n1_2 = new TextInBox("T", 20, 20);
		TextInBox n1_3 = new TextInBox("{", 20, 20);
		TextInBox n1_4 = new TextInBox("member", 60, 20);
		TextInBox n1_5 = new TextInBox("member", 60, 20);
		TextInBox n1_5_1 = new TextInBox("<ERROR:int>", 90, 20);
		TextInBox n1_6 = new TextInBox("member", 60, 20);
		TextInBox n1_6_1 = new TextInBox("int", 30, 20);
		TextInBox n1_6_2 = new TextInBox("i", 20, 20);
		TextInBox n1_6_3 = new TextInBox(";", 20, 20);
		TextInBox n1_7 = new TextInBox("}", 20, 20);
		         
		         
		DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(
				root);
		tree.addChild(root, n1);
		tree.addChild(n1, n1_1);
		tree.addChild(n1, n1_2);
		tree.addChild(n1, n1_3);
		tree.addChild(n1, n1_4);
		tree.addChild(n1, n1_5);
		tree.addChild(n1_5, n1_5_1);
		tree.addChild(n1, n1_6);
		tree.addChild(n1_6,n1_6_1);
		tree.addChild(n1_6,n1_6_2);
		tree.addChild(n1_6,n1_6_3);
		tree.addChild(n1, n1_7);
		return tree;
	}
}

class TextInBox {

	public final String text;
	public final int height;
	public final int width;

	public TextInBox(String text, int width, int height) {
		this.text = text;
		this.width = width;
		this.height = height;
	}
}

class TextInBoxNodeExtentProvider implements NodeExtentProvider<TextInBox> {

	@Override
	public double getWidth(TextInBox treeNode) {
		return treeNode.width;
	}

	@Override
	public double getHeight(TextInBox treeNode) {
		return treeNode.height;
	}
}

class TextInBoxTreePane extends JComponent {
	private static final long serialVersionUID = -7149973930137101400L;
	private final TreeLayout<TextInBox> treeLayout;

	private TreeForTreeLayout<TextInBox> getTree() {
		return treeLayout.getTree();
	}

	private Iterable<TextInBox> getChildren(TextInBox parent) {
		return getTree().getChildren(parent);
	}

	private Rectangle2D.Double getBoundsOfNode(TextInBox node) {
		return treeLayout.getNodeBounds().get(node);
	}

	/**
	 * Specifies the tree to be displayed by passing in a {@link TreeLayout} for
	 * that tree.
	 * 
	 * @param treeLayout the {@link TreeLayout} to be displayed
	 */
	public TextInBoxTreePane(TreeLayout<TextInBox> treeLayout) {
		this.treeLayout = treeLayout;

		Dimension size = treeLayout.getBounds().getBounds().getSize();
		setPreferredSize(size);
	}

	// -------------------------------------------------------------------
	// painting

	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.orange;
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color TEXT_COLOR = Color.black;

	private void paintEdges(Graphics g, TextInBox parent) {
		if (!getTree().isLeaf(parent)) {
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (TextInBox child : getChildren(parent)) {
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(),
						(int) b2.getCenterY());

				paintEdges(g, child);
			}
		}
	}

	private void paintBox(Graphics g, TextInBox textInBox) {
		// draw the box in the background
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		g.setColor(BORDER_COLOR);
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String[] lines = textInBox.text.split("\n");
		FontMetrics m = getFontMetrics(getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + m.getAscent() + m.getLeading() + 1;
		for (int i = 0; i < lines.length; i++) {
			g.drawString(lines[i], x, y);
			y += m.getHeight();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		paintEdges(g, getTree().getRoot());

		// paint the boxes
		for (TextInBox textInBox : treeLayout.getNodeBounds().keySet()) {
			paintBox(g, textInBox);
		}
	}
}