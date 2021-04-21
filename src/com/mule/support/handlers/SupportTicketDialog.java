package com.mule.support.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
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
	private static final String MESSAGE = "You are creating a ticket for technical support team. In this ticket will have the mule app and logs as attachments.";
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
    
    
    private String projectName;
    private String description;
    private String steps;
    private String expected;
    
    
    public SupportTicketDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public void create() {
        super.create();
        setTitle(TITLE);
        setMessage(MESSAGE, IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        createProjectCombo(container);
        createDescription(container);
        createSteps(container);
        createExpected(container);
        
        btnIncludeMuleApp = new Button(container, SWT.CHECK | SWT.WRAP);
        btnIncludeMuleApp.setText(INCLUDE_MULEAPP_LABEL);
        btnIncludeMuleApp.setSelection(true);
        
        btnIncludeLogs = new Button(container, SWT.CHECK | SWT.WRAP);
        btnIncludeLogs.setText(INCLUDE_LOG_LABEL);
        btnIncludeLogs.setSelection(true);
        
        
        
        
        return area;
    }

    private void createProjectCombo(Composite container) {

        Label lbtProjectCombo = new Label(container, SWT.NONE);
        lbtProjectCombo.setText(PROJECT_LABEL);

        
        GridData dataProjectCombo = new GridData();
        dataProjectCombo.grabExcessHorizontalSpace = true;
        dataProjectCombo.horizontalAlignment = GridData.FILL;

        cmbProjects = new Combo(container, SWT.BORDER);
        cmbProjects.setLayoutData(dataProjectCombo);
        
        //TODO delete this
        cmbProjects.add("00287755-testcase", 0);

    }

    private void createDescription(Composite container) {
        Label lbtDescription = new Label(container, SWT.NONE);
        lbtDescription.setText(DESCRIPTION_LABEL);

        GridData dataDescription = new GridData();
        dataDescription.grabExcessHorizontalSpace = true;
        dataDescription.horizontalAlignment = GridData.FILL;
        txtDescription = new Text(container, SWT.BORDER);
        txtDescription.setSize(40, 10);
        txtDescription.setLayoutData(dataDescription);
    }

    private void createSteps(Composite container) {
        Label lbtSteps = new Label(container, SWT.NONE);
        lbtSteps.setText(STEPS_LABEL);

        GridData dataSteps = new GridData();
        dataSteps.grabExcessHorizontalSpace = true;
        dataSteps.horizontalAlignment = GridData.FILL;
        txtSteps = new Text(container, SWT.BORDER);
        txtSteps.setSize(40, 10);
        txtSteps.setLayoutData(dataSteps);
    }
    
    private void createExpected(Composite container) {
        Label lbtSteps = new Label(container, SWT.NONE);
        lbtSteps.setText(EXPECTED_LABEL);

        GridData dataExpected = new GridData();
        dataExpected.grabExcessHorizontalSpace = true;
        dataExpected.horizontalAlignment = GridData.FILL;
        txtExpected = new Text(container, SWT.BORDER);
        txtExpected.setSize(40, 10);
        txtExpected.setLayoutData(dataExpected);
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
        
        String message = " * projectName: " + projectName + 
        				 "\n * description: " + description + 
        				 "\n * steps: " + steps + 
        				 "\n * expected: " + expected ;
        
        List<File> attachments = new ArrayList<>();
        if(btnIncludeMuleApp.getSelection()){
        	attachments.add(CollectFiles.getMuleApp());
        }
        if(btnIncludeLogs.getSelection()){
        	attachments.add(CollectFiles.getLog());     	
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
