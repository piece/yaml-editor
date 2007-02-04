// $Id$
package com.piece_framework.yaml_editor.plugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

/**
 * コンフィグレーションファクトリー.
 * YAML Editor ではプロジェクト単位で設定を保持する。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public final class ConfigurationFactory {
    
    private static Map<IProject, IConfiguration> fConfigMap;
    
    /**
     * コンストラクタ.
     *
     */
    private ConfigurationFactory() {
    }
    
    /**
     * 指定されたプロジェクトのコンフィグレーションを返す.
     * 
     * @param project プロジェクト
     * @return コンフィグレーション
     */
    public static IConfiguration getConfiguration(IProject project) {
        if (fConfigMap == null) {
            fConfigMap = new HashMap<IProject, IConfiguration>();
        }
        
        IConfiguration config = fConfigMap.get(project);
        if (config == null) {
            config = new Configuration();
            config.init(project);
        }
        
        return config;
    }
    
}
