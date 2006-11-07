package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.IDocumentProvider;

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
     * @param progressMonitor プログレスモニタ
     */
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        
        try {

            //編集中テキストを取得
            IDocumentProvider provider = getDocumentProvider();
            IDocument document = provider.getDocument(getEditorInput());
            String yamlStr = document.get();
            System.out.println(yamlStr.substring(0, 2));
            
            
            //バリデーション実行
            //YAMLValidater.validation(yamlStr, yamlStr);
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    /**
     * テキスト保存時処理を行なう.
     */
    public void doSaveAs() {
        super.doSaveAs();
    }
}
