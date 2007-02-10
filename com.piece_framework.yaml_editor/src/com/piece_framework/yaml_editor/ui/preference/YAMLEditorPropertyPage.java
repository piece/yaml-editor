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

import com.piece_framework.yaml_editor.plugin.ConfigurationFactory;
import com.piece_framework.yaml_editor.plugin.IConfiguration;
import com.piece_framework.yaml_editor.plugin.YAMLEditorPlugin;
import com.piece_framework.yaml_editor.ui.dialog.SchemaFolderSelectionDialog;

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
    
    private IConfiguration fConfig;
    
    /**
     * OKクリック時処理.
     * 
     * @return boolean 処理結果
     */
    @Override
    public boolean performOk() {
        
        if (!saveYAMLEditorProperty()) {
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
        
        saveYAMLEditorProperty();
        
        super.performApply();
    }
    
    /**
     * YAML Editor の設定を保存する.
     * 
     * @return 処理結果
     */
    private boolean saveYAMLEditorProperty() {
        String schemaFolderName = fSchemaFolderText.getText();
        
        if (schemaFolderName == null || schemaFolderName.equals("")) {
            MessageDialog.openError(getShell(), 
                        "スキーマフォルダー選択", 
                        "スキーマフォルダーを選択して下さい。");
            return false;
        }
        
        // スキーマフォルダーの存在チェック
        IProject project = (IProject) getElement();
        IFolder folder = project.getFolder(schemaFolderName);
        if (!folder.exists()) {
            MessageDialog.openError(getShell(), 
                    "スキーマフォルダー選択", 
                    schemaFolderName + "は存在しません。");
            return false;
        }
        
        // 現在の設定と変更があれば保存し、すべてのエディターに通知する
        if (!schemaFolderName.equals(
                fConfig.get(IConfiguration.KEY_SCHEMAFOLDER))) {
            
            // 現在のYAML ファイル-スキーマファイル対応を削除
            String[] keys = fConfig.getKeys();
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].startsWith(IConfiguration.KEY_PREFIX_SCHEMAFILE)) {
                    fConfig.remove(keys[i]);
                }
            }
            fConfig.set(IConfiguration.KEY_SCHEMAFOLDER, schemaFolderName);
            fConfig.store();
            
            YAMLEditorPlugin.getDefault().notifyPropertyChanged();
        }
        
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
        
        // 現在のスキーマフォルダーをセット
        String schemaFolderName = fConfig.get(IConfiguration.KEY_SCHEMAFOLDER);
        if (schemaFolderName != null) {
            fSchemaFolderText.setText(schemaFolderName);
        }
        
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
                String tmp = schemaFolder.getFullPath().toString();
                // 先頭のプロジェクト名をカット
                int st = tmp.indexOf('/', 1);
                String schemaFolderName = tmp.substring(st);
                
                fSchemaFolderText.setText(schemaFolderName);
            }
        }
        
    }

    
    /**
     * ダイアログを初期化する.
     * 
     * @param testControl テストコントロール
     * @see org.eclipse.jface.dialogs.DialogPage
     *          #initializeDialogUnits(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected void initializeDialogUnits(Control testControl) {
        
        IProject project = (IProject) getElement();
        fConfig = ConfigurationFactory.getConfiguration(project);
        
        super.initializeDialogUnits(testControl);
    }

}
