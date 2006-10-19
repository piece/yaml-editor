package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * YAML スペース検出クラス.
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.IWhitespaceDetector
 * 
 */
public class YAMLWhitespaceDetector implements IWhitespaceDetector {

    /**
     * スペース・タブ・改行(CR,LF)かを判定する.
     * 
     * @param c 判定対象文字
     * @return 指定された文字がスペース・タブ・改行(CR,LF)のいずれかであればtrue
     * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
     */
    public boolean isWhitespace(char c) {
        return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
    }
}
