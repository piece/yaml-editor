package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;


/**
 * YAML パーティーションスキャナー.
 * YAML ドキュメントを意味のある単位に分割する。
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.RuleBasedPartitionScanner
 * 
 */
public class YAMLPartitionScanner extends RuleBasedPartitionScanner {
    
    private static final int PART_NUM = 3;
    
    // TODO:パーティーション見直し
    /** YAML コメントパーティーション. */
    public static final String YAML_COMMENT = "__yaml_comment";
    /** YAML マッピング(キー)パーティーション. */
    public static final String YAML_MAPPING_KEY = "__yaml_mapping_key";
    /** YAML マッピング(値)パーティーション. */
    public static final String YAML_MAPPING_VAL = "__yaml_mapping_val";
    /** YAML タグパーティーション. */
    //public static final String XML_TAG = "__xml_tag";

    /**
     * コンストラクタ.
     * 各パーティーションのルールを作成する。
     */
    public YAMLPartitionScanner() {
        
        IToken yamlComment = new Token(YAML_COMMENT);
        IToken yamlMappingKey = new Token(YAML_MAPPING_KEY);
        IToken yamlMappingVal = new Token(YAML_MAPPING_VAL);
        //IToken tag = new Token(XML_TAG);

        IPredicateRule[] rules = new IPredicateRule[PART_NUM];
        
        rules[0] = new SingleLineRule("#", null, yamlComment);
        rules[1] = new SingleLineRule("*", ":", yamlMappingKey);
        rules[2] = new SingleLineRule(":", null, yamlMappingVal);
        //rules[3] = new TagRule(tag);
        
        setPredicateRules(rules);
    }
}
