package com.piece_framework.yaml_editor.plugin;

/**
 * エディタークラスが実装するインターフェイス.
 * プラグインからの通知を受け取る。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public interface IYAMLEditor {
    
    /**
     * 設定の変更通知を受け取る.
     *
     */
    void changeProperty();
}
