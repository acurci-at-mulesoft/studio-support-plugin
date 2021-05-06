package com.mule.support.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
 
public class FileChooser extends Composite {
 
	Text mText;
	Button mButton;
	String title = null;
	List<File> files = new ArrayList<>();;
 
	public FileChooser(Composite parent) {
		super(parent, SWT.NULL);
		createContent();
	}
 
	public void createContent() {
		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);
		layout.marginTop = 0;
 
		mText = new Text(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		//gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		mText.setLayoutData(gd);
 
 
		mButton = new Button(this, SWT.NONE);
		mButton.setText("...");
		mButton.addSelectionListener(new SelectionListener() {
 
			public void widgetDefaultSelected(SelectionEvent e) {
			}
 
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(mButton.getShell(),  SWT.OPEN | SWT.MULTI  );
				dlg.setText("Open");
				String choose = dlg.open();
 				if (choose == null) return;
				String path = dlg.getFilterPath();
				String[] strfiles = dlg.getFileNames();
				String text = "";
				for(int i=0; i<strfiles.length; i++) {
					String f = path + File.pathSeparator + strfiles[i];
					files.add(new File(f));
					text+= f + "\n" ;
				}
				mText.setText(text);
			}
		});
	}
 
	public String getText() {
		return mText.getText();
 
	}
 
	public Text getTextControl() {
		return mText;		
	}
 
	public List<File> getFiles() {
		return files;
	}
 
	public String getTitle() {
		return title;
	}
 
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}