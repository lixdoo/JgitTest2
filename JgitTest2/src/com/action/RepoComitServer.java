package com.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import com.constants.JSONHelper;
import com.constants.ServletMaster;
import com.models.RepositoryCommit;
import com.utils.gitHelper;


public class RepoComitServer extends ServletMaster{
	private static final long serialVersionUID = 1L;
	
	public RepoComitServer(){
		super();
	}
	
	public void check(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException, NoHeadException, GitAPIException  {
		RepositoryCommit commits = gitHelper.getRepositoryCommit();
		JSONHelper.ResponseBean(commits, response);
	}
}
