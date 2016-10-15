# StOrganizr
Mind-mappy view for personal wiki


## Developing

Jung version: gradle jfxDeploy  

Graphstream version: gradle fatJar  
java -jar build/libs/StOrganizr-all-0.1.0.jar  

Abego: run from Eclipse  

JGraphX: run from Eclipse  


## TODO
	* read manual for Jung

# Libraries
## [Abego][abego]
Seems just right for horizontal tree layout. 
Swing ui must be set up manually with coordinates from Abego.  

## [Jung][jung]
No control with layout (direction/order).
Swing ui must be set up manually
[Jung and JavaFx][jungandfx] got me started.  

## [Graphstream][Graphstream]
Beautiful force layout graph.  
Hard to get Swing into it.  

## [JGraphX][jgraphx] in [JGraph][jgraph] 
Awesome Swing components, but may be too much?  

subclasses of standard Swing classes for DnD:
See com/mxgraph/swing/handler/mxGraphTransferHandler.java
com/mxgraph/swing/util/mxGraphTransferable.java

http://dev.cs.ovgu.de/java/jgraph/tutorial/t1.html
http://stackoverflow.com/questions/24092091/jgraphx-drop-a-vertex-in-another-vertex


## [Gephi][gephi]
Open source graph visualization software.   [Toolkit][gephilib] is appropriate for library, see [Gephi Toolkit portal][gephilibportal].  




  [abego]: http://treelayout.sourceforge.net  "abego treelayout component"
  [jgraph]: https://www.jgraph.com  "jgraph project"
  [jgraphx]: https://github.com/jgraph/jgraphx  "jgraphx"
  [graphstream]: http://graphstream-project.org  "graphstream project"
	[jungandfx]: https://github.com/jrguenther/JavaFXTutorials  "github project"
	[jung]: http://jung.sourceforge.net  "Jung on sourceforge"
	[gephi]: http://gephi.org  "gephi site"
	[gephilib]: http://gephi.org/toolkit/
	[gephilibportal]: https://wiki.gephi.org/index.php/Toolkit_portal
