// $Id$
package com.piece_framework.yaml_editor.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * コンフィグレーション.
 * コンフィグレーション実装クラス。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see IConfiguration
 */
public class Configuration implements IConfiguration {

    private IProject fProject;
    private Map<String, String> fConfigMap;
    
    /**
     * プロジェクトから設定を取得する.
     * 
     * @param project プロジェクト
     */
    public void init(IProject project) {
        
        fProject = project;
        fConfigMap = new HashMap<String, String>();
        
        readConfig();
        
    }

    /**
     * 設定値を取得する.
     * 
     * @param key キー
     * @return 設定値
     */
    public String get(String key) {
        return fConfigMap.get(key);
    }
    
    /**
     * 値を設定する.
     * 
     * @param key キー
     * @param value 値
     */
    public void set(String key, String value) {
        fConfigMap.put(key, value);
    }
    
    /**
     * すべてのキーを取得する.
     * 
     * @return キーの文字列配列
     */
    public String[] getKeys() {
        Set<String> keySet = fConfigMap.keySet();
        String[] keys = keySet.toArray(new String[0]);
        
        return keys;
    }
    
    /**
     * 指定されたキーの値を削除する.
     * 
     * @param key キー
     */
    public void remove(String key) {
        fConfigMap.remove(key);
    }
    
    /**
     * 設定した値を保存する.
     *
     */
    public void store() {

        IScopeContext projectScope = new ProjectScope(fProject);
        
        Preferences projectNode = projectScope.getNode(
                                    YAMLEditorPlugin.PLUGIN_ID);
        
        try {
            projectNode.clear();
            
            Set<String> set = fConfigMap.keySet();
            Iterator<String> ite = set.iterator();
            
            while (ite.hasNext()) {
                String key = ite.next();
                String value = fConfigMap.get(key);
                
                if (value != null) {
                    projectNode.put(key, fConfigMap.get(key));
                }
            }
            
            projectNode.flush();
        } catch (BackingStoreException e) {
            // TODO: 例外処理
            e.printStackTrace();
        }
        
        // 設定を再読込
        readConfig();
    }
    
    /**
     * 設定値を読込む.
     *
     */
    private void readConfig() {
        
        fConfigMap.clear();
        
        IScopeContext projectScope = new ProjectScope(fProject);
        Preferences projectNode = projectScope.getNode(
                                    YAMLEditorPlugin.PLUGIN_ID);
        
        try {
            
            String[] keys = projectNode.keys();
            for (int i = 0; i < keys.length; i++) {
                
                String value = projectNode.get(keys[i], null);
                
                fConfigMap.put(keys[i], value);
                
            }
        } catch (BackingStoreException e) {
            // TODO: 例外処理
            e.printStackTrace();
        }
        
    }

}
