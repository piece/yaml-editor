// $Id$
package com.piece_framework.yaml_editor.plugin;

import org.eclipse.core.resources.IProject;

/**
 * コンフィグレーションインターフェイス.
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public interface IConfiguration {
    
    /** キー：スキーマフォルダー. */
    String KEY_SCHEMAFOLDER = "SchemaFolder";
    
    /**
     * プロジェクトから設定を取得する.
     * 
     * @param project プロジェクト
     */
    void init(IProject project);
    
    /**
     * 設定値を取得する.
     * 
     * @param key キー
     * @return 設定値
     */
    String get(String key);
    
    /**
     * 値を設定する.
     * 
     * @param key キー
     * @param value 値
     */
    void set(String key, String value);
    
    /**
     * 設定した値を保存する.
     *
     */
    void store();
    
}
