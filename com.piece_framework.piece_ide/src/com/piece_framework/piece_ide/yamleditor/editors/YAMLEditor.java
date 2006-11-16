package com.piece_framework.piece_ide.yamleditor.editors;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IFileEditorInput;

/**
 * YAML テキストエディター.
 * カラーマネージャーの生成、ドキュメント・プロバイダ、
 * ソースビューコンフィギュレーションの設定を行う。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.ui.editors.text.TextEditor
 * 
 */
public class YAMLEditor extends TextEditor {
    
    /**
     * エディターの初期化処理を行う.
     * カラーマネージャーの生成、ドキュメント・プロバイダ、
     * ソース・ビューワ・コンフィギュレーションの設定を行う。
     * 
     * @see org.eclipse.ui.editors.text.TextEditor#initializeEditor()
     */
    protected void initializeEditor() {
        super.initializeEditor();
        
        setDocumentProvider(new YAMLDocumentProvider());
        setSourceViewerConfiguration(new YAMLConfiguration());
        
    }
    
    /**
     * ビューアーを作成する.
     * YAML ビューアーを作成して、返す。
     * 
     * @param parent 親コントローラ
     * @param ruler 垂直ルーラー
     * @param styles スタイル
     * @return ビューアー
     * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor
     *          #createSourceViewer(
     *              org.eclipse.swt.widgets.Composite, 
     *              org.eclipse.jface.text.source.IVerticalRuler, int)
     */
    protected ISourceViewer createSourceViewer(
                                Composite parent, 
                                IVerticalRuler ruler, 
                                int styles) {
        
        YAMLViewer viewer = new YAMLViewer(parent, ruler, styles);
        
        // ドキュメントを設定
        viewer.setDocument(getDocumentProvider().getDocument(getEditorInput()));
        
        // タブサイズを設定
            // 一般→エディター→テキストエディターの「表示するタブサイズ」
            // からタブサイズを取得
        int tabSize = getPreferenceStore().getInt(
            AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH);
        viewer.setTabSize(tabSize);
        
        return viewer;
    }

    /**
     * テキスト保存時処理を行なう.
     * 
     * @param progressMonitor プログレスモニター
     */
    public void doSave(IProgressMonitor progressMonitor) {
          super.doSave(progressMonitor);
          try {

              //編集中のファイルを取得
              IFile  docFile = ((IFileEditorInput) getEditorInput()).getFile();
              
              //プロジェクトのパスの取得
              IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
              IPath  wPath = wRoot.getLocation();
    
              //編集中ファイルからスキーマファイルを取得
              IFileEditorInput fileEdit = (IFileEditorInput) getEditorInput();
              IPath filePath = fileEdit.getFile().getFullPath();
              String schemaPath
                     = filePath.toString().replaceAll(".yaml", ".schema.yaml");
              IFile schemaFile = wRoot.getFileForLocation(
                       new Path(wPath.toString() + schemaPath).makeAbsolute());
              
              //マーカー初期化
              IResource resource =
                      (IResource) getEditorInput().getAdapter(IResource.class);
              resource.deleteMarkers(null, true, IResource.DEPTH_ZERO);
              
              //バリデーション実行
              List<Map> errorList = YAMLValidator.validation(
                               schemaFile.getContents(), docFile.getContents());

              //エラーをマーカーへセット
              for (int i = 0; i < errorList.size(); i++) {
                  Map errorMap = errorList.get(i);

                  IMarker marker = fileEdit.getFile().createMarker(
                                    "org.eclipse.core.resources.problemmarker");
                  marker.setAttributes(errorMap);
              }
      
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
}
