package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;


/**
 * YAML タグルール.
 * タグルールを定義する。
 * TODO:タグルールの見直し(そもそもタグでない？)
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.MultiLineRule
 * 
 */
public class TagRule extends MultiLineRule {

    /**
     * コンストラクタ.
     * 
     * @param token タグルールで使用するトークン
     */
    public TagRule(IToken token) {
        super("<", ">", token);
    }
    
    /**
     * タグルールに合致するか検出する.
     * 
     * @param scanner キャラクタスキャナー
     * @param sequence 検出対象キャラクタ
     * @param eofAllowed EOF 許可
     * @return タグであればtrueを返す。
     * @see org.eclipse.jface.text.rules.PatternRule
     *          #sequenceDetected(
     *              org.eclipse.jface.text.rules.ICharacterScanner, 
     *              char[], 
     *              boolean)
     *              
     */
    protected boolean sequenceDetected(
        ICharacterScanner scanner,
        char[] sequence,
        boolean eofAllowed) {
        
        int c = scanner.read();
        if (sequence[0] == '<') {
            if (c == '?') {
                // processing instruction - abort
                scanner.unread();
                return false;
            }
            if (c == '!') {
                scanner.unread();
                // comment - abort
                return false;
            }
        } else if (sequence[0] == '>') {
            scanner.unread();
        }
        return super.sequenceDetected(scanner, sequence, eofAllowed);
    }
}
