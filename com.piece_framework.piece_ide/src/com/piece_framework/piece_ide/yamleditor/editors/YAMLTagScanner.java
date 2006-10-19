package com.piece_framework.piece_ide.yamleditor.editors;


import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

/**
 * YAML タグスキャナー.
 * YAMLタグのルールを定義する。
 * TODO:"タグスキャナー"という名前はふさわしくないかも。
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.RuleBasedScanner
 * 
 */
public class YAMLTagScanner extends RuleBasedScanner {
    
    // 定義されるルール数
    private static final int RULE_NUM = 3;
    
    /**
     * コンストラクタ.
     * YAMLのルールを定義する。
     * 
     * @param colorManager カラーマネージャー
     */
    public YAMLTagScanner(ColorManager colorManager) {
        IToken string =
            new Token(
                new TextAttribute(
                        colorManager.getColor(IYAMLColorConstants.STRING)));

        IRule[] rules = new IRule[RULE_NUM];

        // ダブルクォーテーションルール
        rules[0] = new SingleLineRule("\"", "\"", string, '\\');
        // シングルクォーテーションルール
        rules[1] = new SingleLineRule("'", "'", string, '\\');
        // 一般的なスペースルール
        rules[2] = new WhitespaceRule(new YAMLWhitespaceDetector());

        setRules(rules);
    }
}
