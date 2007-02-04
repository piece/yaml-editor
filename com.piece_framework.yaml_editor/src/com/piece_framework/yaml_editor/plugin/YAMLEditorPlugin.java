package com.piece_framework.yaml_editor.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.piece_framework.yaml_editor.ui.editor.YAMLColorManager;

/**
 * プラグインのライフサイクルを管理する.
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public class YAMLEditorPlugin extends AbstractUIPlugin {

    /** プラグインID. */
    public static final String PLUGIN_ID = 
                "com.piece_framework.yaml_editor"; //$NON-NLS-1$

    // インスタンス
    private static YAMLEditorPlugin fPlugin;
    
    /**
     * コンストラクタ.
     */
    public YAMLEditorPlugin() {
        fPlugin = this;
    }
    
    /**
     * プラグインを開始する.
     * 
     * @param context コンテキスト
     * @exception Exception 一般的な例外
     * @see org.eclipse.ui.plugin.AbstractUIPlugin
     *          #start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }
    
    /**
     * プラグインを停止する.
     * カラーマネージャーの終了処理を行う.
     * 
     * @param context コンテキスト
     * @throws Exception 一般的な例外
     * @see org.eclipse.ui.plugin.AbstractUIPlugin
     *          #stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        YAMLColorManager.getColorManager().dispose();
        fPlugin = null;
        super.stop(context);
    }
    
    /**
     * 共有インスタンスを取得する.
     *
     * @return 共有インスタンス
     */
    public static YAMLEditorPlugin getDefault() {
        return fPlugin;
    }
    
    /**
     * 指定されたイメージファイルのイメージディスクリプタ取得する.
     * 
     * @param path イメージファイルパス
     * @return イメージディスクリプタ
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
