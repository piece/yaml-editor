package com.piece_framework.piece_ide.yamleditor;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class YAMLEditorPropertyPage extends PropertyPage {
    
    private Text txtSchemaFolder;
    private Button btnReference;
    
    @Override
    public boolean performOk() {
        
        ILabelProvider lp= new WorkbenchLabelProvider();
        ITreeContentProvider cp= new WorkbenchContentProvider();

        
        //NewFolderDialog dialog = new NewFolderDialog(this.getShell(), this.);
        FolderSelectionDialog dialog = new FolderSelectionDialog(getShell(), lp, cp);
        dialog.setTitle("スキーマフォルダーの選択");
        
        if (getElement() instanceof IProject) {
            IProject project = (IProject) getElement();
            
            dialog.setInput(getElement());
        }
        
        
        if (dialog.open() == Window.OK) {
            
            System.out.println(dialog.getResult()[0]);
        }
        
        return super.performOk();
    }

    @Override
    protected Control createContents(Composite parent) {
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = false;
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(gridLayout);
        composite.setSize(new Point(500, 50));
        
        Label label = new Label(composite, SWT.NONE);
        label.setText("スキーマフォルダー(&S):");
        
        Label dummy = new Label(composite, SWT.NONE);
        dummy.setText("");
        
        GridData gridData = null;
        
        txtSchemaFolder = new Text(composite, SWT.BORDER);
        gridData = new GridData();
        gridData.widthHint = 300;
        txtSchemaFolder.setLayoutData(gridData);
        
        btnReference = new Button(composite, SWT.NONE);
        btnReference.setText("参照(&W)...");
        gridData = new GridData();
        gridData.widthHint = 100;
        btnReference.setLayoutData(gridData);
        
        return composite;
    }

}
