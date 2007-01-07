package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

/**
 * YAML テキストエディター(スキーマ選択コンボボックス付).
 * YAMLEditor クラスが提供するエディター機能とスキーマ選択
 * コンボボックスを表示・管理する。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.ui.part.EditorPart
 * @see YAMLEditor
 * @see org.eclipse.core.resources.IResourceChangeListener
 */
public class YAMLEditorPart extends EditorPart 
                                   implements IResourceChangeListener {
    
    private static final int SCHEMA_COMBO_WIDTH = 200;
    private static final int SCHEMA_COMBO_HEIGHT = 50;
    
    private YAMLEditor editor;
    
    private Combo schemaCombo;
        
    /**
     * コンストラクタ.
     * YAML Editor の生成及び各初期化を行う。
     *
     */
    public YAMLEditorPart() {
        super();
        editor = new YAMLEditor();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        
        // TODO: デバッグ
        System.out.println("construct"); //$NON-NLS-1$
    }

    /**
     * テキストを保存するときの処理を行う.
     * YAMLEditor クラスの doSave メソッドを呼出す。
     * 
     * @param monitor プログレスモニタ
     * @see YAMLEditor#doSave(IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        editor.setYAMLSchemaFile(getSelectionYAMLSchemaFile());
        YAMLSchemaManager.setSchemaFileForYAML(
                getYAMLFile(), getSelectionYAMLSchemaFile());
        editor.doSave(monitor);
    }

    /**
     * ファイル名を指定してテキストを保存するときの処理を行う.
     * YAMLEditor クラスの doSaveAs メソッドを呼出す。
     * 
     * @see YAMLEditor#doSaveAs()
     */
    @Override
    public void doSaveAs() {
        editor.setYAMLSchemaFile(getSelectionYAMLSchemaFile());
        YAMLSchemaManager.setSchemaFileForYAML(
                getYAMLFile(), getSelectionYAMLSchemaFile());
        editor.doSaveAs();
    }
    
    /**
     * 初期化を行う.
     * 
     * @param site エディターサイト
     * @param input エディターインプット
     * @throws PartInitException 初期化時例外
     */
    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        
        setSite(site);
        setInput(input);
        setPartName(input.getName());
        
        // TODO: デバッグ   
        System.out.println("init"); //$NON-NLS-1$
    }

    /**
     * 終了処理を行う.
     */
    @Override
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    /**
     * 最終保存時から変更されたかを返す.
     * YAMLEditor クラスの doSaveAs メソッドを呼出す。
     * 
     * @return 最終保存時から変更されたか
     * @see YAMLEditor#isDirty()()
     */
    @Override
    public boolean isDirty() {
        return editor.isDirty();
    }

    /**
     * テキストを保存できるかを返す.
     * 
     * @return テキストを保存できるか
     */
    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
     * 各コントロールの生成・配置を行う.
     * コントロールのサイズ修正は ControlListener イベントを利用する。
     * 
     * @param parent 親コントロール
     */
    @Override
    public void createPartControl(Composite parent) {
        // TODO: デバッグ
        System.out.println("createPartControl"); //$NON-NLS-1$
        
        try {

            RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
            rowLayout.marginRight = 0;
            rowLayout.marginLeft = 0;
            rowLayout.marginTop = 0;
            rowLayout.marginBottom = 0;
            rowLayout.marginHeight = 0;
            rowLayout.marginWidth = 0;
            
            final Composite parentComposite = new Composite(parent, SWT.NONE);
            parentComposite.setLayout(rowLayout);
            
            final Group schemaGroup = new Group(parentComposite, SWT.NONE);
            schemaGroup.setText(
                Messages.getString("YAMLEditorPart.SchemaTitle")); //$NON-NLS-1$
            schemaGroup.setLayout(rowLayout);
            
            schemaCombo = new Combo(schemaGroup, SWT.READ_ONLY);
            schemaCombo.setLayoutData(new RowData(SCHEMA_COMBO_WIDTH,
                                                  SCHEMA_COMBO_HEIGHT));
            
            FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
            fillLayout.marginHeight = 0;
            fillLayout.marginWidth = 0;
            fillLayout.spacing = 0;
            
            final Composite editorComposite = 
                            new Composite(parentComposite, SWT.NONE);
            editorComposite.setLayout(fillLayout);
            
            editor.init(getEditorSite(), getEditorInput());
            editor.addPropertyListener(new IPropertyListener() {
                public void propertyChanged(Object source, int propertyId) {
                    firePropertyChange(propertyId);
                }
            });
            
            editor.createPartControl(editorComposite);
            
            parent.addControlListener(new ControlListener() {

                public void controlMoved(ControlEvent e) {
                }

                public void controlResized(ControlEvent e) {
                    
                    Point parentSize = parentComposite.getParent().getSize();
                    
                    // TODO: コンボボックスのサイズ修正
                    
                    editorComposite.setLayoutData(
                            new RowData(parentSize.x, 
                                        parentSize.y - SCHEMA_COMBO_HEIGHT));
                    
                }
                
            });
            
        } catch (PartInitException e) {
            // TODO: 例外処理
            e.printStackTrace();
        }
        
        // スキーマファイルの一覧をコンボボックスにセット
        setSchemaFileList();
    }
    
    /**
     * フォーカスを取得したときの処理を行う.
     * 
     */
    @Override
    public void setFocus() {
        editor.setFocus();

    }

    /**
     * リソースが変更された場合の処理を行う.
     * 
     * @param event リソース変更イベント
     */
    public void resourceChanged(IResourceChangeEvent event) {
        
        if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
            
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    IFile yamlFile = getYAMLFile();
                    if (yamlFile != null) {
                        if (!yamlFile.exists()) {
                            IWorkbenchPage page = 
                                PlatformUI.getWorkbench().
                                getActiveWorkbenchWindow().getActivePage();
                            page.closeEditor(YAMLEditorPart.this, false);
                            
                        } else if (!getPartName().equals(yamlFile.getName())) {
                            setPartName(yamlFile.getName());
                        }
                    }
                }
            });
        }
        
    }
    
    /**
     * 指定されたアダプターに対してオブジェクトを返す.
     * 
     * @param adapter アダプター
     * @return オブジェクト
     */
    @Override
    public Object getAdapter(Class adapter) {
        return editor.getAdapter(adapter);
    }
    
    /**
     * 編集中の YAML ファイルが所属するプロジェクトを返す.
     *  
     * @return プロジェクト
     */
    private IProject getYAMLProject() {
        IProject project = null;
        
        if (getEditorInput() != null) {
            IFile yamlFile = ((IFileEditorInput) getEditorInput()).getFile();
            project = yamlFile.getProject();
        }
        
        return project;
    }
    
    /**
     * 編集中の YAML ファイルを返す.
     * 
     * @return YAML ファイル
     */
    private IFile getYAMLFile() {
        
        IEditorInput input = editor.getEditorInput();
        IFile yamlFile = null;
        
        if (input != null) {
            yamlFile = ((IFileEditorInput) input).getFile();
        }
        
        return yamlFile;
    }


    /**
     * YAML スキーマファイル一覧を取得して、コンボボックスにセットする.
     *
     */
    private void setSchemaFileList() {
        
        if (schemaCombo == null) {
            return;
        }
        
        // スキーマファイルの取得
        IFile[] schemaFiles = 
                    YAMLSchemaManager.getSchemaFiles(getYAMLProject());
        
        // YAML ファイルに対応するスキーマファイルを取得
        IFile schemaFileForYAML = 
                    YAMLSchemaManager.getSchemaFileForYAML(getYAMLFile());
        
        // スキーマ選択メッセージ行の追加
        schemaCombo.add(Messages.getString(
                "YAMLEditorPart.SelectSchemaMessage")); //$NON-NLS-1$
        schemaCombo.select(0);
        
        if (schemaFiles != null) {
            for (int i = 0; i < schemaFiles.length; i++) {
                schemaCombo.add(schemaFiles[i].getName());
                
                if (schemaFileForYAML != null) {
                    if (schemaFiles[i].getFullPath().toString().equals(
                            schemaFileForYAML.getFullPath().toString())) {
                        
                        schemaCombo.select(i + 1);
                    }
                }
            }
        }
        
    }
    
    /**
     * コンボボックスで選択された YAML スキーマファイルを返す.
     * 
     * @return YAML スキーマファイル
     */
    private IFile getSelectionYAMLSchemaFile() {
        
        IFile schemaFile = null;
        
        // 先頭はスキーマ選択メッセージ行なので、
        // 実際の YAML スキーマファイルは2行目から
        if (schemaCombo.getSelectionIndex() > 0) {
            String schemaFileName = schemaCombo.getItem(
                    schemaCombo.getSelectionIndex());
    
            schemaFile = getYAMLProject().getFile(
                YAMLSchemaManager.SCHEMA_FOLDER + "/"  //$NON-NLS-1$
                + schemaFileName);
            
            if (!schemaFile.exists()) {
                schemaFile = null;
            }
            
        }
        
        return schemaFile;
    }
}
