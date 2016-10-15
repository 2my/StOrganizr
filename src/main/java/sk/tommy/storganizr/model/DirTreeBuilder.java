package sk.tommy.storganizr.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Stack;

public class DirTreeBuilder extends SimpleFileVisitor<Path> {
	final FsNode root;
	final Stack<FsNode> dirStack	= new Stack<>();

	private DirTreeBuilder(Path start) {
		root	= new FsNode(start.toString(), start.toFile().getName(), start.toFile().isDirectory());
	}

	public static FsNode buildTree( Path start ) {
		try {
			DirTreeBuilder builder	= new DirTreeBuilder(start);
			Files.walkFileTree(start, builder);
			return builder.root;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	private FsNode node(Path file) {
		File f	= file.toFile();
		return new FsNode(file.toString(), f.getName(), f.isDirectory());
	}

	@Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		FsNode node	= node(file);
		dirStack.peek().children.add(node);
		return FileVisitResult.CONTINUE;
	}

	@Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		if (ignoreDir(dir))
			return FileVisitResult.SKIP_SUBTREE;

		FsNode node	= node(dir);
		if ( ! dirStack.isEmpty() ) {
			FsNode parentN	= dirStack.peek();
			parentN.children.add(node);
		}
		dirStack.push(node);
		// System.out.println("In: " + dir.toString());
		return FileVisitResult.CONTINUE;
	}

	@Override public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
		if (e == null) {
			// System.out.println("Out: " + dir.toString());
			dirStack.pop();
			return FileVisitResult.CONTINUE;
		} else {
			// directory iteration failed
			throw e;
		}
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


	public static void main(String[] args) {
    	// graphStreamHelloWorld
		Path start = FileSystems.getDefault().getPath(".");
		FsNode root	= DirTreeBuilder.buildTree(start);
    }
}
