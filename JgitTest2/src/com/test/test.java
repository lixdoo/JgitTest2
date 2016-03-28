package com.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import com.utils.gitHelper;

public class test {
	public static void main(String[] args) throws IOException, NoHeadException, GitAPIException {
		try (Repository repository = gitHelper.openJGitCookbookRepository()) {
			Ref head = repository.exactRef("refs/heads/master");
			try (RevWalk walk = new RevWalk(repository)) {
				RevCommit commit = walk.parseCommit(head.getObjectId());
				RevTree tree = walk.parseTree(commit.getTree().getId());

				TreeWalk treeWalk = new TreeWalk(repository);
				// Add an already created tree iterator for walking.

				treeWalk.addTree(tree);
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (treeWalk.next()) {
					ObjectId objectId = treeWalk.getObjectId(0);
					FileMode fileMode = treeWalk.getFileMode(0);
					System.out.println(treeWalk.getPathString());
					// System.out.println(getFileMode(fileMode));
					ObjectLoader loader = repository.open(objectId);
					System.out.println(objectId);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					loader.copyTo(baos);
					String str = baos.toString();
					// System.out.println(str);
					// System.out.println("********************");
				}

				// ObjectId objectId2 = treeWalk.getObjectId(0);
				// ObjectLoader loader2 = repository.open(objectId2);
				// loader2.copyTo(System.out);

				// loader = repository.open(tree.getId());
				// //向OutPutStream中写入，如 message.writeTo(baos);
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//
				// loader.copyTo(baos);
				// String str = baos.toString();
				// OutputStream in = null;
				// System.out.println(str);

				// walk.dispose();
			}

		}
	}

	public static void getFile(ObjectId id, Repository repository) throws IOException {
		Ref head = repository.exactRef("refs/heads/master");
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = walk.parseTree(commit.getTree().getId());
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			HashMap<String,ObjectId> hash_file = new HashMap<>();
			while (treeWalk.next()) {
				ObjectId objectId = treeWalk.getObjectId(0);
				FileMode fileMode = treeWalk.getFileMode(0);
				hash_file.put(treeWalk.getPathString(), treeWalk.getObjectId(0));
				System.out.println(treeWalk.getPathString());
				
				ObjectLoader loader = repository.open(objectId);
				System.out.println(objectId);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				loader.copyTo(baos);
				String str = baos.toString();
			}

		}
	}

	private static RevTree getTree(Repository repository)
			throws AmbiguousObjectException, IncorrectObjectTypeException, IOException, MissingObjectException {
		ObjectId lastCommitId = repository.resolve(Constants.HEAD);

		// a RevWalk allows to walk over commits based on some filtering
		try (RevWalk revWalk = new RevWalk(repository)) {
			RevCommit commit = revWalk.parseCommit(lastCommitId);

			System.out.println("Time of commit (seconds since epoch): " + commit.getCommitTime());

			// and using commit's tree find the path
			RevTree tree = commit.getTree();
			System.out.println("Having tree: " + tree);
			return tree;
		}
	}

	private static void printFile(Repository repository, RevTree tree)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		// now try to find a specific file
		try (TreeWalk treeWalk = new TreeWalk(repository)) {
			treeWalk.addTree(tree);
			treeWalk.setRecursive(false);
			// treeWalk.setFilter(PathFilter.create("README.md"));
			// if (!treeWalk.next()) {
			// throw new IllegalStateException("Did not find expected file
			// 'README.md'");
			// }

			// FileMode specifies the type of file, FileMode.REGULAR_FILE for
			// normal file, FileMode.EXECUTABLE_FILE for executable bit
			// set
			FileMode fileMode = treeWalk.getFileMode(0);
			ObjectLoader loader = repository.open(treeWalk.getObjectId(0));
			System.out.println("README.md: " + getFileMode(fileMode) + ", type: " + fileMode.getObjectType()
					+ ", mode: " + fileMode + " size: " + loader.getSize());
		}
	}

	private static void printDirectory(Repository repository, RevTree tree)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		// look at directory, this has FileMode.TREE
		try (TreeWalk treeWalk = new TreeWalk(repository)) {
			treeWalk.addTree(tree);
			treeWalk.setRecursive(false);
			treeWalk.setFilter(PathFilter.create("src"));
			// if (!treeWalk.next()) {
			// throw new IllegalStateException("Did not find expected file
			// 'README.md'");
			// }

			// FileMode now indicates that this is a directory, i.e.
			// FileMode.TREE.equals(fileMode) holds true
			FileMode fileMode = treeWalk.getFileMode(0);
			System.out.println(
					"src: " + getFileMode(fileMode) + ", type: " + fileMode.getObjectType() + ", mode: " + fileMode);
		}
	}

	private static String getFileMode(FileMode fileMode) {
		if (fileMode.equals(FileMode.EXECUTABLE_FILE)) {
			return "Executable File";
		} else if (fileMode.equals(FileMode.REGULAR_FILE)) {
			return "Normal File";
		} else if (fileMode.equals(FileMode.TREE)) {
			return "Directory";
		} else if (fileMode.equals(FileMode.SYMLINK)) {
			return "Symlink";
		} else {
			// there are a few others, see FileMode javadoc for details
			throw new IllegalArgumentException("Unknown type of file encountered: " + fileMode);
		}
	}
}
