package com.models;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;


public class RepositoryCommit {

	private String repository;

	private List<Ref> branches;

	private List<RevCommit> commits;
	
	private String author;
	
	private RevCommit lastCommit;
	
	private String content;
	
	private String commitId;
	
	private String date;
	
	private List<String> fileContent ;
	
	private List<ObjectId> fileId ;
	
	private List<String> fileType;
	
	private List<String> fileName;
	
	
	public List<String> getFileContent() {
		return fileContent;
	}

	public void setFileContent(List<String> fileContent) {
		this.fileContent = fileContent;
	}

	public List<ObjectId> getFileId() {
		return fileId;
	}

	public void setFileId(List<ObjectId> fileId) {
		this.fileId = fileId;
	}

	public List<String> getFileType() {
		return fileType;
	}

	public void setFileType(List<String> fileType) {
		this.fileType = fileType;
	}

	public List<String> getFileName() {
		return fileName;
	}

	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}

	public RepositoryCommit(){
		
	}
	
	public RepositoryCommit(HttpServletRequest request)throws IllegalAccessException,InvocationTargetException,UnsupportedEncodingException{
		BeanUtils.populate(this,request.getParameterMap());
	}


	public String getRepository() {
		return repository;
	}


	public void setRepository(String repository) {
		this.repository = repository;
	}


	public List<Ref> getBranches() {
		return branches;
	}

	public void setBranches(List<Ref> branches) {
		this.branches = branches;
	}

	public List<RevCommit> getCommits() {
		return commits;
	}

	public void setCommits(List<RevCommit> commits) {
		this.commits = commits;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public RevCommit getLastCommit() {
		return lastCommit;
	}

	public void setLastCommit(RevCommit lastCommit) {
		this.lastCommit = lastCommit;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	


	
	

}
