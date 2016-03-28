package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.notes.Note;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.models.RepositoryCommit;

public class gitHelper {
	public static Repository openJGitCookbookRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File("e:/test/.git")).readEnvironment().findGitDir().build();
		return repository;
	}

	public static RepositoryCommit getRepositoryCommit() throws IOException, NoHeadException, GitAPIException {
		RepositoryCommit reCommit = new RepositoryCommit();
		reCommit.setRepository("test");
		try (Repository repository = gitHelper.openJGitCookbookRepository()) {
			try (Git git = new Git(repository)) {
				List<RevCommit> list_commits = new LinkedList<>();
				/*commits*/
				Iterable<RevCommit> commits = git.log().call();   
				for (RevCommit commit : commits) {
					list_commits.add(commit);
				}
				reCommit.setCommits(list_commits);
				reCommit.setLastCommit(list_commits.get(0));
				reCommit.setContent(reCommit.getLastCommit().getFullMessage());
				reCommit.setCommitId(reCommit.getLastCommit().getName());
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String Date=sdf.format(list_commits.get(0).getAuthorIdent().getWhen());
				reCommit.setDate(Date);
				reCommit.setAuthor(list_commits.get(0).getAuthorIdent().getName());
				/*branch*/
				List<Ref> list_branches = git.branchList().call();
				reCommit.setBranches(list_branches);
				/*files*/
				reCommit.setFileId(getFileId(repository));
				reCommit.setFileName(getFileName(repository));
				reCommit.setFileType(getFileType(repository));
				reCommit.setFileContent(getFileContent(repository));
			}

		}
		return reCommit;
	}
	
	/**
	 * create by lidxoo 2016.03.28
	 * @param repository
	 * @throws IOException
	 */
	public static HashMap<String,ObjectId> getFile(Repository repository) throws IOException {
		Ref head = repository.exactRef("refs/heads/master");
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = walk.parseTree(commit.getTree().getId());
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			List<String> fileContent = new LinkedList<>();
			List<ObjectId> fileId = new LinkedList<>();
			List<String> fileType= new LinkedList<>();
			List<String> fileName= new LinkedList<>();
			HashMap<String,ObjectId> hash_file = new HashMap<>();
			while (treeWalk.next()) {
				ObjectId objectId = treeWalk.getObjectId(0);
				FileMode fileMode = treeWalk.getFileMode(0);
				if(fileMode.equals(FileMode.TREE)){
					fileType.add("Directory");
				}else{
					fileType.add("file");
				}
				fileId.add(objectId);
				fileName.add(treeWalk.getPathString());
				hash_file.put(treeWalk.getPathString(), treeWalk.getObjectId(0));
				ObjectLoader loader = repository.open(objectId);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				loader.copyTo(baos);
				fileContent.add(baos.toString());
				
			}
			return hash_file;

		}
	}
	
	
	public static List<String> getFileName(Repository repository) throws IOException {
		Ref head = repository.exactRef("refs/heads/master");
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = walk.parseTree(commit.getTree().getId());
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			List<String> fileName= new LinkedList<>();
			while (treeWalk.next()) {
				fileName.add(treeWalk.getPathString());
			}
			return fileName;
		}
	}
	
	
	
	public static List<String> getFileContent(Repository repository) throws IOException {
		Ref head = repository.exactRef("refs/heads/master");
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = walk.parseTree(commit.getTree().getId());
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			List<String> fileContent = new LinkedList<>();
			while (treeWalk.next()) {
				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				loader.copyTo(baos);
				fileContent.add(baos.toString());
			}
			return fileContent;

		}
	}
	
	
	public static List<String> getFileType(Repository repository) throws IOException {
		Ref head = repository.exactRef("refs/heads/master");
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = walk.parseTree(commit.getTree().getId());
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			List<String> fileType= new LinkedList<>();
			HashMap<String,ObjectId> hash_file = new HashMap<>();
			while (treeWalk.next()) {
				FileMode fileMode = treeWalk.getFileMode(0);
				if(fileMode.equals(FileMode.TREE)){
					fileType.add("Directory");
				}else{
					fileType.add("file");
				}
				
			}
			return fileType;
		}
	}
	
	
	
	public static List<ObjectId> getFileId(Repository repository) throws IOException {
		Ref head = repository.exactRef("refs/heads/master");
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = walk.parseTree(commit.getTree().getId());
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			List<ObjectId> fileId = new LinkedList<>();
			while (treeWalk.next()) {
				ObjectId objectId = treeWalk.getObjectId(0);
				fileId.add(objectId);
				
			}
			return fileId;

		}
	}
	
	
	
	
	
	
	
	
	
	

	public static void main(String[] args) throws IOException, NoHeadException, GitAPIException {
		try (Repository repository = gitHelper.openJGitCookbookRepository()) {
			try (Git git = new Git(repository)) {
				Iterable<RevCommit> logs = git.log().call();
				Iterable<RevCommit> commits = git.log().call();
				List<RevCommit> list_commits = new LinkedList<>();
				int count = 0, commitCount = 0;
				for (RevCommit rev : logs) {
					System.out.println("LogCommit: " + rev.getCommitTime());
					count++;
				}
				for (RevCommit commit : commits) {
					System.out.println("LogCommit: " + commit.getTree());
					list_commits.add(commit);
					commitCount++;
				}
				System.out.println(list_commits.get(0).getAuthorIdent().getWhen() + "hehe");
				System.out.println(count);
				System.out.println("已经有 " + count + "次提交 ");
				System.out.println("已经有 " + commitCount + "次提交 ");

				List<Ref> call = git.branchList().call();
				for (Ref ref : call) {
					System.out.println("分支: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
				}

				System.out.println("有以下远程分支");
				call = git.branchList().setListMode(ListMode.ALL).call();
				for (Ref ref : call) {
					System.out.println("分支: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
				}
			}
		}

	}

}
