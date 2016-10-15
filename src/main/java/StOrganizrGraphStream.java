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
import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

/**  
 * @author tommy
*/
public class StOrganizrGraphStream {

	private static String name(Path path) {
		return path.toFile().getName();
	}
	private static Graph getGraph() throws Exception {
		Graph graph = new MultiGraph("embedded");
		graph.addAttribute("ui.stylesheet", "edge { fill-color: rgb(255,0,0); }");

		// Graph graph = new SingleGraph("embedded");
		Stack<Node> dirStack	= new Stack<>();

		Path start = FileSystems.getDefault().getPath(".");

		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

			@Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if (ignoreDir(dir))
					return FileVisitResult.SKIP_SUBTREE;
				Node dirN	= graph.addNode(dir.toString());
				if ( ! dirStack.isEmpty() ) {
					Node parentN	= dirStack.peek();
					Element e	= graph.addEdge(parentN.getId() + " - " + dirN.getId(), parentN, dirN);
					dirN.addAttribute("ui.label", dir.getFileName());
				}
				dirStack.push(dirN);
				// dirN.addAttribute("ui.label", dir.toString());
				return FileVisitResult.CONTINUE;
			}

			@Override public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
				if (e == null) {
					dirStack.pop();
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}

			@Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				System.out.println(file.toString());
				if ( ! file.toFile().isDirectory() ) {
					/* do not show files, only directories
					Node dirN	= dirStack.peek();
					Node fileN	= graph.addNode(file.toString());
					Element e	= graph.addEdge(dirN.getId() + " - " + fileN.getId(), dirN, fileN);
					fileN.addAttribute("ui.label", file.getFileName());
					*/
				}
				return FileVisitResult.CONTINUE;
			}

			private boolean ignoreDir(Path file) {
				String fName = file.toFile().getName();
				if (fName.equals("."))
					return false;
				if (fName.startsWith("."))
					return true;
				if (fName.startsWith("bin"))
					return true;
				if (fName.startsWith("build"))
					return true;
				if (fName.startsWith("target"))
					return true;
				if (3 <= dirStack.size())
					return true;
				return false;
			}
		});

		return graph;
	}

	public static Graph graph() {
		Graph graph = new MultiGraph("embedded");
		// Graph graph = new SingleGraph("embedded");

		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

		// return graph;
		try {
			return getGraph();
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
	}

    private static JComponent graphStreamInSwing(Graph graph) {
    	Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
    	// Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    	viewer.enableAutoLayout();
    	View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
    	return (JComponent) view;
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("StOrganizrGraphStream");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add( graphStreamInSwing(graph()), BorderLayout.CENTER );

        //Display the window.
        frame.pack();
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


	public static void graphStreamHelloWorld(String args[]) {
		graph().display();
	}

	public static void main(String[] args) {
    	// graphStreamHelloWorld

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
