// $Id$
package com.piece_framework.yaml_editor.ui.editor;

import java.util.Vector;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;

/**
 * YAML パーティーションスキャナー.
 * YAML ドキュメントを意味のある単位に分割する。<br>
 * YAML ではソースを次の3つに分割する。<br>
 * ・YAML コード<br>
 * 　　シーケンスやマッピングなどYAMLの一般コード。<br>
 * ・YAML コメント<br>
 * 　　"#"以下の1行。<br>
 * ・YAML 終端<br>
 * 　　"..."以下の全行。<br>
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.jface.text.rules.RuleBasedPartitionScanner
 * 
 */
public final class YAMLPartitionScanner extends RuleBasedPartitionScanner {
    
    /** YAML 終端. */
    private static final String YAML_TERMINATE = 
                                    "__yaml_TERMINATE"; //$NON-NLS-1$
    
    /** パーティーションタイプ配列. */
    public static final String[] YAML_PARTITION_TYPES = new String[] { 
                                        YAML_TERMINATE };
    
    /** YAML 終端色. */
    private static final RGB YAML_TERMINATE_COLOR = new RGB(128, 128, 128);
    
    /** パーティーション色配列. */
    public static final RGB[] YAML_PARTITION_COLORS = new RGB[] { 
                                        YAML_TERMINATE_COLOR };
    
    private static YAMLPartitionScanner fScanner;
    
    /**
     * コンストラクタ.
     * 各パーティーションのルールを作成する。
     */
    private YAMLPartitionScanner() {
        
        Vector<IPredicateRule> rules = new Vector<IPredicateRule>();
        
        // 終端
            // "..."以降の全行
        rules.add(new MultiLineRule("...\n", "\0",  //$NON-NLS-1$ //$NON-NLS-2$
                    new Token(YAML_TERMINATE), (char) 0 , true));
        
        IPredicateRule[] r = new IPredicateRule[1];
        setPredicateRules(rules.toArray(r));
    }
    
    /**
     * YAML パーティーションスキャナーのインスタンスを返す.
     * 
     * @return YAML パーティションスキャナー
     */
    public static YAMLPartitionScanner getScanner() {
        if (fScanner == null) {
            fScanner = new YAMLPartitionScanner();
        }
        return fScanner;
    }
}
