
/** StOrganizrJungFx
   Copyright 2013 Tommy Skodje

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
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;

/**  */
public class StOrganizrJungFx extends Application {

	private static final int FONT_SIZE = 15;
	private static final int GRAPH_WIDTH = 350;
	private static final int GRAPH_HEIGHT = 350;
	private static final int WINDOW_WIDTH = GRAPH_WIDTH + 2 * FONT_SIZE;
	private static final int WINDOW_HEIGHT = GRAPH_HEIGHT + 2 * FONT_SIZE;

	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

		final Text target = new Text(WINDOW_WIDTH / 2 - 60, 20, "DROP ROOT HERE");
		target.setScaleX(2.0);
		target.setScaleY(2.0);

		Group group = new Group();

		/*
		Graph<String, Number> graph = getGraph();
		Layout<String, Number> layout = new SpringLayout<>(graph);
		layout.setSize(new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT));

		Graph<String, Number> graph = getGraph();
		DAGLayout<String, Number> layout = new DAGLayout<>(graph);
		layout.setRoot("v1");
		layout.setSize(new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT));
		*/

		Forest<String, Number> graph = getForest();
		Layout<String, Number> layout = new TreeLayout<>(graph);

		group.getChildren().addAll(nodes2display(graph, layout));
		group.translateXProperty().set(FONT_SIZE);
		group.translateYProperty().set(FONT_SIZE);

		root.getChildren().add(target);
		root.getChildren().add(group);

		stage.setTitle("Graph");
		stage.setScene(scene);
		stage.show();

	}

	private DirectedGraph<String, Number> getGraph() throws Exception {
		Path start = FileSystems.getDefault().getPath(".");
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

			@Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				System.out.println(file.toString());
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
				return false;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if (ignoreDir(dir))
					return FileVisitResult.SKIP_SUBTREE;
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
				if (e == null) {
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}
		});

		DirectedGraph<String, Number> g = new DirectedSparseMultigraph<String, Number>();
		g.addVertex("v1");
		g.addVertex("v2");
		g.addVertex("v3");
		// Add some edges. From above we defined these to be of type String
		// Note that the default is for undirected edges.
		g.addEdge(1, "v1", "v2");
		g.addEdge(2, "v1", "v3");
		return g; // TestGraphs.getOneComponentGraph();
	}

	private Forest<String, Number> getForest() throws Exception {
		// return new DelegateTree<>(getGraph());
		DelegateTree<String, Number> g	= new DelegateTree<>();
		g.addVertex("v1");
		g.addChild(1, "v1", "v2");
		g.addChild(2, "v1", "v3");
		g.addChild(3, "v1", "v4");
		g.addChild(4, "v1", "v5");
		g.addChild(5, "v1", "v6");
		g.addChild(6, "v1", "v7");
		g.addChild(7, "v3", "v8");
		g.addChild(8, "v3", "v9");
		return g;
	}

	/** Get javafx Nodes from graph */
	private List<Node> nodes2display(Graph<String, Number> graph, Layout<String, Number> layout) {
		List<Node> shapes = new ArrayList<>();
		for (String v : graph.getVertices()) {
			Point2D p = layout.transform(v);
			shapes.add(vertex(p, v));
		}
		for (Number n : graph.getEdges()) {
			Pair<String> endpoints = graph.getEndpoints(n);
			Point2D start = layout.transform(endpoints.getFirst());
			Point2D end = layout.transform(endpoints.getSecond());
			shapes.add(edge(start, end));
		}
		return shapes;
	}


	private Node edge(Point2D start, Point2D end) {
		double yA	= start.getX();
		double xA	= start.getY();
		double yB	= end.getX();
		double xB	= end.getY();
		Node line	= LineBuilder.create().startX(xA).startY(yA).endX(xB).endY(yB).build();
		line.setStyle("-fx-stroke: rgb(255,255,100);");
		line.toBack();
		return line;
	}

	private Node vertex(Point2D p, String title) {
		double y	= p.getX();
		double x	= p.getY();
		final Text v = TextBuilder.create().font(new Font(FONT_SIZE)).text(title).x(x).y(y).build();
		v.toFront();

		v.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Mouse clicked");
			}
		});
		v.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
					// System.out.println("drag over");
				} else {
					event.consume();
				}
			}
		});
		v.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					success = true;
					String filePath = null;
					for (File file : db.getFiles()) {
						filePath = file.getAbsolutePath();
						System.out.println(filePath);
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
		return v;
	}

	/**
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
