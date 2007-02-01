package com.piece_framework.yaml_editor.yamleditor.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * YAML ビューアー.
 * YAML ドキュメントを表示するビューアークラス。<br>
 * タブをスペースに変換する。<br>
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.jface.text.rules.SourceViewer
 * 
 */
public class YAMLViewer extends SourceViewer {
    
    // デフォルトタブサイズ
    private static final int DEFAULT_TABSIZE = 4;
    
    private int tabSize;
    private IDocument document;
    
    /**
     * コンストラクタ.
     * 
     * @param parent 親コントローラ
     * @param ruler 垂直ルーラー
     * @param styles スタイル
     */
    public YAMLViewer(Composite parent, IVerticalRuler ruler, int styles) {
        super(parent, ruler, styles);
        
        document = null;
        tabSize = DEFAULT_TABSIZE;
    }
    
    /**
     * ドキュメントをセットする.
     * 
     * @param doc ドキュメント
     * @see org.eclipse.jface.text.source.SourceViewer
     *          #setDocument(org.eclipse.jface.text.IDocument)
     */
    public void setDocument(IDocument doc) {
        document = doc;
    }
    
    /**
     * タブサイズをセットする.
     * 
     * @param size タブサイズ
     */
    public void setTabSize(int size) {
        tabSize = size;
    }
    
    /**
     * ドキュメントをカスタマイズする.
     * YAML ビューアーではタブをスペースに変換する。
     * 
     * @param command ドキュメントコマンド
     * @see org.eclipse.jface.text.TextViewer
     *          #customizeDocumentCommand(
     *              org.eclipse.jface.text.DocumentCommand)
     */
    protected void customizeDocumentCommand(DocumentCommand command) {
        String text = command.text;
        if (text == null || document == null) {
            return;
        }
        
        // デフォルトのライントラッカーを使用
        ILineTracker lineTracker = new DefaultLineTracker();
        
        int index = text.indexOf('\t');
        if (index > -1) {

            StringBuffer buffer = new StringBuffer();

            // ライントラッカーを使用して修正された行数を取得
            // (通常は1行)
            lineTracker.set(command.text);
            int lines = lineTracker.getNumberOfLines();
            
            try {

                for (int i = 0; i < lines; i++) {
                    // 1行分の文字列を取得
                    int offset = lineTracker.getLineOffset(i);
                    int endOffset = offset + lineTracker.getLineLength(i);
                    String line = text.substring(offset, endOffset);
                    
                    // 修正位置を取得
                    int position = 0;
                    if (i == 0) {
                        IRegion firstLine = 
                            document.getLineInformationOfOffset(command.offset);
                        position = command.offset - firstLine.getOffset();
                    }
                    
                    // 1行の文字列を文字単位でチェック
                    for (int j = 0; j < line.length(); j++) {
                        char c = line.charAt(j);
                        if (c == '\t') {
                            // タブ→スペース変換
                            position += insertTabString(buffer, position);
                        } else {
                            buffer.append(c);
                            position++;
                        }
                    }

                }
                command.text = buffer.toString();

            } catch (BadLocationException x) {
            }
        }
        super.customizeDocumentCommand(command);
    }
    
    /**
     * タブをスペースに変換する.
     * タブは常に指定されたタブサイズ分、間が空けられるわけではない。<br>
     * 例えば、挿入位置が6文字目でタブサイズが4の場合、挿入される空白は、<br>
     * 4ではなく2でなければならい。<br>
     * 
     * @param buffer 文字列バッファ
     * @param offsetInLine スペース挿入位置
     * @return 挿入されたスペース数
     */
    private int insertTabString(StringBuffer buffer, int offsetInLine) {

        if (tabSize == 0) {
            return 0;
        }
        
        // タブは常に指定されたタブサイズ分、間が空けられるわけではない。
        // 例えば、挿入位置が6文字目でタブサイズが4の場合、挿入される空白は、
        // 4ではなく2でなければならい。
        int remainder = offsetInLine % tabSize;
        remainder = tabSize - remainder;
        for (int i = 0; i < remainder; i++) {
            buffer.append(' ');
        }
        
        return remainder;
    }
    
    
    
}
