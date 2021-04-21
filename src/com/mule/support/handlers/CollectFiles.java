package com.mule.support.handlers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

public class CollectFiles {

	public static List<File> getMuleApp(IProject project) {
		List<File> listFiles = new ArrayList<>();
		File dir = new File(project.getLocation() + File.separator + "target");
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".jar");
		    }
		});
		for (File file : files) {
			listFiles.add(file);
		}
		return listFiles;
	}
	
	public static List<File>  getLog(IProject project) {
		List<File> listFiles = new ArrayList<>();
		File dir = new File(project.getLocation() + File.separator + "target");
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".log");
		    }
		});
		for (File file : files) {
			listFiles.add(file);
		}
		return listFiles;
	}
	
}
