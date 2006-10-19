package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.swt.graphics.RGB;

/**
 * YAML カラー定義インターフェイス.
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.MultiLineRule
 * 
 */
public interface IYAMLColorConstants {
    
    /** YAML コメント色. */
    RGB YAML_COMMENT = new RGB(128, 128, 128);
    /** YAML マッピング(キー)色. */
    RGB YAML_MAPPING_KEY = new RGB(0, 0, 128);
    /** YAML マッピング(値)色. */
    RGB YAML_MAPPING_VAL = new RGB(0, 128, 0);
    
    /** タグ色. */
    //RGB TAG = new RGB(0, 0, 128);
    
    /** プロセス文字色. */
    RGB PROC_INSTR = new RGB(128, 128, 128);
    /** 文字列色. */
    RGB STRING = new RGB(0, 128, 0);
    /** デフォルト色. */
    RGB DEFAULT = new RGB(0, 0, 0);
    
    
    /** 
     * ダミーメソッド.
     * TODO:YAML カラー定義インターフェイス見直し。
     */
    void dummy();
}
