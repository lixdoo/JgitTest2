package com.utils;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class ShowFileDiff {

	public static void main(String[] args) throws IOException, GitAPIException {
		try (Repository repository = gitHelper.openJGitCookbookRepository()) {
			AbstractTreeIterator oldTreeParser = prepareTreeParser(repository,
					"b019dd6b11eef59d9b1a7b865b1c4620530d57af");
			AbstractTreeIterator newTreeParser = prepareTreeParser(repository,
					"0603c10a309936ebddac7f5f3258f30e5f11bbe7");

			try (Git git = new Git(repository)) {
				List<DiffEntry> diff = git.diff().setOldTree(oldTreeParser).setNewTree(newTreeParser)
						.call();
				for (DiffEntry entry : diff) {
					System.out.println("Entry: " + entry + ", from: " + entry.getOldId() + ", to: " + entry.getNewId());
					try (DiffFormatter formatter = new DiffFormatter(System.out)) {
						formatter.setRepository(repository);
						formatter.format(entry);
					}
				}
			}
		}
	}
//	setPathFilter(PathFilter.create("README.md")).
	private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId)
			throws IOException, MissingObjectException, IncorrectObjectTypeException {
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
			RevTree tree = walk.parseTree(commit.getTree().getId());

			CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
			try (ObjectReader oldReader = repository.newObjectReader()) {
				oldTreeParser.reset(oldReader, tree.getId());
			}

			walk.dispose();

			return oldTreeParser;
		}
	}
}
