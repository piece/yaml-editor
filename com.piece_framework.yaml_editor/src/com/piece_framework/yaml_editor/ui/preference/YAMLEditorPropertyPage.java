// $Id$
package com.piece_framework.yaml_editor.ui.preference;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.ui.dialogs.PropertyPage;

import com.piece_framework.yaml_editor.ui.dialog.SchemaFolderSelectionDialog;
import com.piece_framework.yaml_editor.ui.editor.YAMLSchemaManager;

/**
 * プロジェクト設定ページ.
 * スキーマフォルダーを指定する。<br>
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.ui.dialogs.PropertyPage
 */
public class YAMLEditorPropertyPage extends PropertyPage {
    
    private static final int BASE_WIDTH = 500;
    private static final int BASE_HEIGHT = 50;
    private static final int TEXT_WIDTH = 300;
    private static final int BUTTON_WIDTH = 100;
    
    private Text fSchemaFolderText;
    
    /**
     * OKクリック時処理.
     * 
     * @return boolean 処理結果
     */
    @Override
    public boolean performOk() {
        
        if (!saveSchemaFolder()) {
            return false;
        }
        
        return super.performOk();
    }

    
    /**
     * 適用クリック時処理.
     * 
     */
    @Override
    protected void performApply() {
        
        saveSchemaFolder();
        
        super.performApply();
    }

    /**
     * スキーマフォルダーのパス名を取得する.
     * 
     * @return スキーマフォルダーパス
     */
    private String getSchemaFolder() {
        
        IProject project = (IProject) getElement();
        IFolder schemaFolder = YAMLSchemaManager.getSchemaFolder(project);
        
        String schemaFolderName = "";
        if (schemaFolder != null) {
            schemaFolderName = 
                schemaFolder.getFullPath().toString().substring(
                    project.getName().length() + 1);
        }
        
        return schemaFolderName;
    }
    
    /**
     * スキーマフォルダーのパス名を保存する.
     * 
     * @return 処理結果
     */
    private boolean saveSchemaFolder() {
        String schemaFolderName = fSchemaFolderText.getText();
        
        if (schemaFolderName == null || schemaFolderName.equals("")) {
            MessageDialog.openError(getShell(), 
                        "スキーマフォルダー選択", 
                        "スキーマフォルダーを選択して下さい。");
            return false;
        }
        
        IProject project = (IProject) getElement();
        IFolder schemaFolder = project.getFolder(schemaFolderName);
        YAMLSchemaManager.setSchemaFolder(schemaFolder);
        
        return true;
        
    }

    /**
     * コントロールの作成・配置を行う.
     * 
     * @param parent 親コントロール
     * @return 作成・配置したコントロール
     */
    @Override
    protected Control createContents(Composite parent) {
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = false;
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(gridLayout);
        composite.setSize(new Point(BASE_WIDTH, BASE_HEIGHT));
        
        Label label = new Label(composite, SWT.NONE);
        label.setText("スキーマフォルダー(&S):");
        
        Label dummy = new Label(composite, SWT.NONE);
        dummy.setText("");
        
        GridData gridData = null;
        
        fSchemaFolderText = new Text(composite, SWT.BORDER);
        gridData = new GridData();
        gridData.widthHint = TEXT_WIDTH;
        fSchemaFolderText.setLayoutData(gridData);
        fSchemaFolderText.setText(getSchemaFolder());
        
        Button button = new Button(composite, SWT.NONE);
        button.setText("参照(&W)...");
        gridData = new GridData();
        gridData.widthHint = BUTTON_WIDTH;
        button.setLayoutData(gridData);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                openSchemaFolderSelectionDialog();
            }
        });
        
        return composite;
    }
    
    /**
     * スキーマフォルダー選択ダイアログを表示する.
     *
     */
    private void openSchemaFolderSelectionDialog() {
        
        // プロジェクトのプロパティーでしか表示されないので、強制的にキャスト
        IProject project = (IProject) getElement();
        SchemaFolderSelectionDialog dialog = 
            new SchemaFolderSelectionDialog(getShell(), project);
        
        if (dialog.open() == Window.OK) {
            IFolder schemaFolder = dialog.getSelectedSchemaFolder();
            if (schemaFolder != null) {
                fSchemaFolderText.setText(
                        schemaFolder.getFullPath().toString());
            }
        }
        
    }

}
