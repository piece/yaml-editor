package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.ui.editors.text.TextEditor;


/**
 * YAML テキストエディター.
 * カラーマネージャーの生成、コンフィギュレーション、
 * ドキュメントプロバイダーの設定を行う。
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.ui.editors.text.TextEditor
 * 
 */
public class YAMLEditor extends TextEditor {

    // カラーマネージャー
    private ColorManager colorManager;

    /**
     * コンストラクタ.
     * カラーマネージャーの生成、コンフィギュレーション、
     * ドキュメントプロバイダーの設定を行う。
     */
    public YAMLEditor() {
        super();
        colorManager = new ColorManager();
        setSourceViewerConfiguration(new YAMLConfiguration(colorManager));
        setDocumentProvider(new YAMLDocumentProvider());
    }
    
    /**
     * カラーマネージャーの終了処理を行う.
     * 
     * @see org.eclipse.ui.editors.text.TextEditor#dispose()
     */
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }

}
