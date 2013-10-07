/** StOrganizr
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
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.*;
import edu.uci.ics.jung.visualization.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**  */
public class StOrganizr extends Application {

    private static final int FONT_SIZE = 15;
    private static final int GRAPH_WIDTH	= 350;
    private static final int GRAPH_HEIGHT	= 350;
    private static final int WINDOW_WIDTH		= GRAPH_WIDTH + 2 * FONT_SIZE;
    private static final int WINDOW_HEIGHT	= GRAPH_HEIGHT + 2 * FONT_SIZE;

    @Override public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene( root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE );
        Group group = new Group();

        // JUNG TestGraphs
        Graph<String, Number> graph = TestGraphs.getOneComponentGraph();

        Layout<String, Number> layout = new SpringLayout<>( graph );
        layout.setSize( new Dimension( GRAPH_WIDTH, GRAPH_HEIGHT ) );

				group.getChildren().addAll( nodes2display( graph, layout ) );
				group.translateXProperty().set( FONT_SIZE );
				group.translateYProperty().set( FONT_SIZE );
        root.getChildren().add( group );

        stage.setTitle("Graph");
        stage.setScene(scene);
        stage.show();

    }
    
    /** Get javafx Nodes from graph */
    private List<Node> nodes2display( Graph<String, Number> graph, Layout<String, Number> layout ) {
    	List<Node> shapes	= new ArrayList<>();
			for (String v : graph.getVertices()) {
				Point2D p = layout.transform( v );
				shapes.add( vertex( p, v ) );
			}
			for (Number n : graph.getEdges()) {
				Pair<String> endpoints = graph.getEndpoints( n );
				Point2D start = layout.transform( endpoints.getFirst() );
				Point2D end = layout.transform( endpoints.getSecond() );
				shapes.add( edge( start, end ) );
			}
			return shapes;
    }

		private Node edge( Point2D start, Point2D end ) {
			return LineBuilder.create()
						.startX( start.getX() )
						.startY( start.getY() )
						.endX( end.getX() )
						.endY( end.getY() )
						.build()
					;
		}

		private Node vertex( Point2D p, String title ) {
			final Text v = TextBuilder.create()
							.font( new Font( FONT_SIZE ) )
							.text( title )
							.x( p.getX() )
							.y( p.getY() )
							.build()
					;

			v.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent event) {
					System.out.println("Mouse clicked");
				}
			});
			v.setOnDragOver(new EventHandler<DragEvent>() {
				@Override public void handle(DragEvent event) {
					Dragboard db = event.getDragboard();
					if (db.hasFiles()) {
						event.acceptTransferModes(TransferMode.COPY);
						System.out.println("drag over");
					} else {
						event.consume();
					}
				}
			});
			v.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override public void handle(DragEvent event) {
					Dragboard db = event.getDragboard();
					boolean success = false;
					if (db.hasFiles()) {
						success = true;
						String filePath = null;
						for (File file:db.getFiles()) {
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

    /** main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
