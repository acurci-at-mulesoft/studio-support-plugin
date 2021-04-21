package com.mule.support.handlers;

import java.io.File;

import org.eclipse.core.resources.IProject;

public class CollectFiles {

	public static File getMuleApp(IProject project) {
		System.out.println(project.getFullPath());
		System.out.println(project.getLocationURI());
		System.out.println(project.getLocation());
		System.out.println(project.getRawLocationURI());
		System.out.println(project.getRawLocation());
		//TODO
		
		
		return new File("mule-app-file");
	}
	
	public static File getLog(IProject project) {
		System.out.println(project.getFullPath());
		//TODO
		
		
		return new File("log-file");
	}
	
}
