//package com.utils;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.api.ListBranchCommand;
//import org.eclipse.jgit.api.ResetCommand.ResetType;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.diff.DiffEntry;
//import org.eclipse.jgit.diff.DiffFormatter;
//import org.eclipse.jgit.diff.EditList;
//import org.eclipse.jgit.diff.RawText;
//import org.eclipse.jgit.errors.IncorrectObjectTypeException;
//import org.eclipse.jgit.lib.ObjectId;
//import org.eclipse.jgit.lib.ObjectLoader;
//import org.eclipse.jgit.lib.ObjectReader;
//import org.eclipse.jgit.lib.Ref;
//import org.eclipse.jgit.lib.Repository;
//import org.eclipse.jgit.revwalk.RevCommit;
//import org.eclipse.jgit.revwalk.RevObject;
//import org.eclipse.jgit.revwalk.RevTag;
//import org.eclipse.jgit.revwalk.RevTree;
//import org.eclipse.jgit.revwalk.RevWalk;
//import org.eclipse.jgit.storage.file.FileRepository;
//import org.eclipse.jgit.treewalk.CanonicalTreeParser;
//import org.eclipse.jgit.treewalk.TreeWalk;
///*
// * 
// * Child 子版本号
// * Parent 父版本号
// */
//public class JgitDiff {
//	
//	public JgitDiff() {
//	}
//	static String URL="D:/cfrManage/ConfigFile/.git";
//	static Git git;
//	public static Repository repository;
//	public static void main(String[] args) {
//		JgitDiff jgitDiff = new JgitDiff();
//		
//		jgitDiff.diffMethod("Child","Parent");
//	}
//	/*
//	 * 
//	 */
//	public void diffMethod(String Child, String Parent){
//		try {
//			git=Git.open(new File("D:/cfrManage/ConfigFile/.git"));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		repository=git.getRepository();
//		ObjectReader reader = repository.newObjectReader();
//		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
//	
//		try {
//			ObjectId old = repository.resolve(Child + "^{tree}");
//			ObjectId head = repository.resolve(Parent+"^{tree}");
//					oldTreeIter.reset(reader, old);
//			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
//			newTreeIter.reset(reader, head);
//			List<DiffEntry> diffs= git.diff()
//                    .setNewTree(newTreeIter)
//                    .setOldTree(oldTreeIter)
//                    .call();
//			
//			 ByteArrayOutputStream out = new ByteArrayOutputStream();
//			    DiffFormatter df = new DiffFormatter(out);
//			    df.setRepository(git.getRepository());
//			
//			for (DiffEntry diffEntry : diffs) {
//		         df.format(diffEntry);
//		         String diffText = out.toString("UTF-8");
//		         System.out.println(diffText);
//		       //  out.reset();
//			}
//		} catch (IncorrectObjectTypeException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (GitAPIException e) {
//			e.printStackTrace();
//		}
//	}
//}
//
//
