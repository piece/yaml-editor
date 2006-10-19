package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

/**
 * YAML スキャナー.
 * YAMLヘッダーのルールを定義する。
 * TODO:ヘッダーはないので、このクラス自体不要？
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.RuleBasedScanner
 * 
 */
public class YAMLScanner extends RuleBasedScanner {

    /**
     * コンストラクタ.
     * YAMLヘッダーのルールを定義する。
     * 
     * @param colorManager カラーマネージャー
     */
    public YAMLScanner(ColorManager colorManager) {
        IToken procInstr =
            new Token(
                new TextAttribute(
                        colorManager.getColor(IYAMLColorConstants.PROC_INSTR)));

        IRule[] rules = new IRule[2];
        // "<?"～"?>"ルール
        rules[0] = new SingleLineRule("<?", "?>", procInstr);
        // 一般的なスペースルール
        rules[1] = new WhitespaceRule(new YAMLWhitespaceDetector());

        setRules(rules);
    }
}
