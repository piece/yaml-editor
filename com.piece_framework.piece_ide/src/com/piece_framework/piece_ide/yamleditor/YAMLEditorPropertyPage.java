package com.piece_framework.piece_ide.yamleditor;

import java.util.ArrayList;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class YAMLEditorPropertyPage extends PropertyPage {
    
    private class TypedViewerFilter extends ViewerFilter {

        private Class[] fAcceptedTypes;
        private Object[] fRejectedElements;

        /**
         * Creates a filter that only allows elements of gives types.
         * @param acceptedTypes The types of accepted elements
         */
        public TypedViewerFilter(Class[] acceptedTypes) {
            this(acceptedTypes, null);
        }

        /**
         * Creates a filter that only allows elements of gives types, but not from a
         * list of rejected elements.
         * @param acceptedTypes Accepted elements must be of this types
         * @param rejectedElements Element equals to the rejected elements are
         * filtered out
         */ 
        public TypedViewerFilter(Class[] acceptedTypes, Object[] rejectedElements) {
            Assert.isNotNull(acceptedTypes);
            fAcceptedTypes= acceptedTypes;
            fRejectedElements= rejectedElements;
        }   

        /**
         * @see ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (fRejectedElements != null) {
                for (int i= 0; i < fRejectedElements.length; i++) {
                    if (element.equals(fRejectedElements[i])) {
                        return false;
                    }
                }
            }
            for (int i= 0; i < fAcceptedTypes.length; i++) {
                if (fAcceptedTypes[i].isInstance(element)) {
                    return true;
                }
            }
            return false;
        }

    }
    
    private Text txtSchemaFolder;
    private Button btnReference;
    
    @Override
    public boolean performOk() {
        
        ILabelProvider lp= new WorkbenchLabelProvider();
        ITreeContentProvider cp= new WorkbenchContentProvider();
        
        FolderSelectionDialog dialog = new FolderSelectionDialog(getShell(), lp, cp);
        dialog.setTitle("スキーマフォルダーの選択");
        dialog.setMessage("スキーマフォルダーを選択してください(&C):");
        
        if (getElement() instanceof IProject) {
            IProject project = (IProject) getElement();
            
            IWorkspaceRoot root= project.getWorkspace().getRoot();
            final Class[] acceptedClasses= new Class[] { IProject.class, IFolder.class };
            IProject[] allProjects= root.getProjects();
            ArrayList rejectedElements= new ArrayList(allProjects.length);
            for (int i= 0; i < allProjects.length; i++) {
                if (!allProjects[i].equals(project)) {
                    rejectedElements.add(allProjects[i]);
                }
            }
            ViewerFilter filter= new TypedViewerFilter(acceptedClasses, rejectedElements.toArray());
            
            dialog.setInput(project.getWorkspace().getRoot());
            dialog.addFilter(filter);
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
