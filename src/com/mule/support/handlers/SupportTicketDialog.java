package com.mule.support.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class SupportTicketDialog extends TitleAreaDialog {
	
	private static final String TITLE = "Create a ticket for support";
	private static final String DISCLAIMER = "WARNING: Don't attach files with confidential information or private credentials.";

	private static final String PROJECT_LABEL = "Mule Application";
	private static final String DESCRIPTION_LABEL = "What were you doing?";
	private static final String STEPS_LABEL = "How can we reproduce the problem?";	
	private static final String EXPECTED_LABEL = "What is the behavior as you expected?";	
	private static final String INCLUDE_MULEAPP_LABEL = "Include the Mule App";	
	private static final String INCLUDE_LOG_LABEL = "Include the log file";	
	
	
    private Combo cmbProjects;
    private Text txtDescription;
    private Text txtSteps;
    private Text txtExpected;
    private Button btnIncludeMuleApp;
    private Button btnIncludeLogs;
    private GridData defaultLayout;
    
    private String projectName;
    private String description;
    private String steps;
    private String expected;
    
    
    IWorkspace workspace;
    IWorkspaceRoot root;
    IProject[] projects;
    
    
    public SupportTicketDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public void create() {
        super.create();
        setTitle(TITLE);
        setMessage(DISCLAIMER, IMessageProvider.WARNING);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        try {
			createProjectCombo(container);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        defaultLayout = new GridData();
        defaultLayout.grabExcessHorizontalSpace = true;
        defaultLayout.horizontalAlignment = GridData.FILL;
        defaultLayout.widthHint = 100;
        
        createDescription(container);
        createSteps(container);
        createExpected(container);
        
        btnIncludeMuleApp = new Button(container, SWT.CHECK | SWT.NONE);
        btnIncludeMuleApp.setText(INCLUDE_MULEAPP_LABEL);
        btnIncludeMuleApp.setSelection(true);
        btnIncludeMuleApp.setLayoutData(defaultLayout);

        
        btnIncludeLogs = new Button(container, SWT.CHECK | SWT.WRAP);
        btnIncludeLogs.setText(INCLUDE_LOG_LABEL);
        btnIncludeLogs.setSelection(true);
        btnIncludeMuleApp.setLayoutData(defaultLayout);

        return area;
    }

    private void createProjectCombo(Composite container) throws CoreException {

        Label lbtProjectCombo = new Label(container, SWT.NONE);
        lbtProjectCombo.setText(PROJECT_LABEL);

        cmbProjects = new Combo(container, SWT.NONE);
        cmbProjects.setLayoutData(defaultLayout);
        
        workspace = ResourcesPlugin.getWorkspace();
        root = workspace.getRoot();
        projects = root.getProjects();
        int i=0;
        for(IProject p: projects) {
        	if(p.hasNature("org.mule.tooling.core.muleStudioNature")) {
        		cmbProjects.add(p.getName(), i);
        		if(p.isOpen()) {
        			cmbProjects.select(i);
        		}
        		i++;
        	}
        }        
    }

    private void createDescription(Composite container) {
        Label lbtDescription = new Label(container, SWT.NONE);
        lbtDescription.setText(DESCRIPTION_LABEL);
        txtDescription = new Text(container, SWT.BORDER);
        txtDescription.setSize(40, 10);
        txtDescription.setLayoutData(defaultLayout);
    }

    private void createSteps(Composite container) {
        Label lbtSteps = new Label(container, SWT.NONE);
        lbtSteps.setText(STEPS_LABEL);
        txtSteps = new Text(container, SWT.BORDER);
        txtSteps.setSize(40, 10);
        txtSteps.setLayoutData(defaultLayout);
    }
    
    private void createExpected(Composite container) {
        Label lbtSteps = new Label(container, SWT.NONE);
        lbtSteps.setText(EXPECTED_LABEL);
        txtExpected = new Text(container, SWT.BORDER);
        txtExpected.setSize(40, 10);
        txtExpected.setLayoutData(defaultLayout);
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private void saveInput() {
        projectName = cmbProjects.getText();
        description = txtDescription.getText();
        steps = txtSteps.getText();
        expected = txtExpected.getText();
        
        IProject project = root.getProject(cmbProjects.getText());
        
        String message = " * " + PROJECT_LABEL				+ ": " + project.getName() +  "\n" + 
        				 " * " + DESCRIPTION_LABEL			+ ": " + description +  "\n" + 
        				 " * " + STEPS_LABEL 			 	+ ": " + steps 		 +  "\n" + 
        				 " * " + EXPECTED_LABEL				+ ": " + expected    +  "\n"  ;
        System.out.println(message);

        List<File> attachments = new ArrayList<>();
        if(btnIncludeMuleApp.getSelection()){
        	attachments.add(CollectFiles.getMuleApp(project));
        }
        if(btnIncludeLogs.getSelection()){
        	attachments.add(CollectFiles.getLog(project));     	
        }

        String issueNumber = SalesforceServices.createSupportCase(projectName, message, attachments);
        

    }

    @Override
    protected void okPressed() {
        saveInput();
        // super.okPressed();
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }
    
    public String getSteps() {
    	return steps;
    }
    
    public String getExpected() {
    	return expected;
    }
    
    
    
    
    

}
