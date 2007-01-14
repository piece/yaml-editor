package com.piece_framework.piece_ide.yamleditor;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceSorter;

/**
 * スキーマフォルダー選択ダイアログ.
 * プロジェクト内のフォルダからスキーマフォルダーを選択する。<br>
 * 新規フォルダー作成ボタンを設け、新たにスキーマフォルダーを作成することもできる。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog
 * @see org.eclipse.jface.viewers.ISelectionChangedListener
 */
public class SchemaFolderSelectionDialog extends ElementTreeSelectionDialog 
                                          implements ISelectionChangedListener {

    private Button fNewFolderButton;
    private IContainer fSelectedContainer;

    /**
     * コンストラクタ.
     * タイトル・ラベル及びリスト表示対象の設定を行う。<br>
     * リストには対象プロジェクト及び配下のフォルダーのみ表示する。
     * 
     * @param parent 親コントロール
     * @param project 対象プロジェクト
     */
    public SchemaFolderSelectionDialog(Shell parent, final IProject project) {
        
        super(parent, 
               new WorkbenchLabelProvider(), 
               new WorkbenchContentProvider());
        
        setTitle("スキーマフォルダーの選択");
        setMessage("スキーマフォルダーを選択してください(&C):");
        setAllowMultiple(false);
        
        // ルートから表示し、自プロジェクト以外を表示しないようにする
        setInput(project.getWorkspace().getRoot());
        addFilter(new ViewerFilter() {
            public boolean select(Viewer viewer, 
                                    Object parentElement, 
                                    Object element) {
                
                // 自プロジェクトとそのフォルダーのみ表示
                if (element.equals(project) || element instanceof IFolder) {
                    return true;
                }
                
                return false;
            }
            
        });
        
        setSorter(new ResourceSorter(ResourceSorter.NAME));
        
    }

    /**
     * コントロールの作成・配置を行う.
     * 
     * @param parent 親コントロール
     * @return 作成・配置したコントロール
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        
        Composite composite = (Composite) super.createDialogArea(parent);
        
        getTreeViewer().addSelectionChangedListener(this);
        
        fNewFolderButton = new Button(composite, SWT.PUSH);
        fNewFolderButton.setText("新規フォルダーの作成(&N)..."); 
        fNewFolderButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                newFolderButtonPressed();
            }
        });
        fNewFolderButton.setFont(parent.getFont());
        
        applyDialogFont(composite);

        // TODO: ヘルプ処理をどうするか？
        //PlatformUI.getWorkbench().getHelpSystem().setHelp(
        //    parent, [インターフェイス]);
        
        return composite;
    }
    
    /**
     * 新規フォルダー作成ボタンクリック時の処理.
     * 新規フォルダー作成ダイアログを表示する。
     *
     */
    private void newFolderButtonPressed() {
        
        NewFolderDialog dialog = new NewFolderDialog(
                getShell(), fSelectedContainer) {
            protected Control createContents(Composite parent) {
                // TODO: ヘルプ処理をどうするか？
                //PlatformUI.getWorkbench().getHelpSystem().setHelp(
                //    parent, [インターフェイス]);
                return super.createContents(parent);
            }
        };
        
        if (dialog.open() == Window.OK) {
            // 新規フォルダーを選択状態にする
            TreeViewer treeViewer = getTreeViewer();
            treeViewer.refresh(fSelectedContainer);
            Object createdFolder = dialog.getResult()[0];
            treeViewer.reveal(createdFolder);
            treeViewer.setSelection(new StructuredSelection(createdFolder));
        }
    }
    
    /**
     * リストの選択が変化したときのイベント処理.
     * 
     * @param event イベント
     */
    public void selectionChanged(SelectionChangedEvent event) {
        
        // 選択されたプロジェクト・フォルダーの取得
        IStructuredSelection selection = 
            (IStructuredSelection) getTreeViewer().getSelection();
        fSelectedContainer = null;
        if (selection.size() == 1) {
            fSelectedContainer = (IContainer) selection.getFirstElement();
        }
        
        // プロジェクト・フォルダーが選択されている場合は
        // 新規フォルダー作成ボタンを有効にする。
        fNewFolderButton.setEnabled(fSelectedContainer != null);
    }
    
    /**
     * 選択されているスキーマフォルダーを返す.
     * 
     * @return 選択されているスキーマフォルダー
     */
    public IFolder getSelectedSchemaFolder() {        
        return (IFolder) getResult()[0];
    }

}
