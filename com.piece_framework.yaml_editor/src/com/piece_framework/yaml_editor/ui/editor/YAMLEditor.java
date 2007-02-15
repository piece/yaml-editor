// $Id$
package com.piece_framework.yaml_editor.ui.editor;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IFileEditorInput;

import com.piece_framework.yaml_editor.ui.dialog.YAMLErrorDialog;
import com.piece_framework.yaml_editor.util.YAMLValidator;

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

    private String fSchemaFileName;
    
    private static final String MARKER_ID = 
        "org.eclipse.core.resources.problemmarker"; //$NON-NLS-1$
    

    
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
          
          // TODO: スキーマが指定されたいない場合どうするか？
          if (fSchemaFileName == null) {
              return;
          }
          
          try {

              //編集中のファイルを取得
              IFile yamlFile = ((IFileEditorInput) getEditorInput()).getFile();
              IFile schemaFile = yamlFile.getProject().getFile(
                                      fSchemaFileName); 
              
              //マーカー初期化
              IResource resource =
                      (IResource) getEditorInput().getAdapter(IResource.class);
              resource.deleteMarkers(null, true, IResource.DEPTH_ZERO);

              //バリデーション実行
              List<Map> errorList = YAMLValidator.validation(
                               schemaFile.getContents(), 
                               yamlFile.getContents());
              
              IFileEditorInput fileEdit = (IFileEditorInput) getEditorInput();
              //エラーをマーカーへセット
              for (int i = 0; i < errorList.size(); i++) {
                  Map errorMap = errorList.get(i);

                  IMarker marker = fileEdit.getFile().createMarker(MARKER_ID);
                  marker.setAttributes(errorMap);
              }
      
        } catch (Exception e) {
            //エラーダイアログ表示
            YAMLErrorDialog.showDialog(IStatus.ERROR,
                                       YAMLErrorDialog.ERROR_NUM_OTHER, e);
        }
    }
    
    /**
     * YAML スキーマファイルをセットする.
     * 
     * @param schemaFileName YAML スキーマファイル
     */
    public void setSchemaFileName(String schemaFileName) {
        fSchemaFileName = schemaFileName;
    }
}
