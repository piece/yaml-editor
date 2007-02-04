package com.piece_framework.yaml_editor.ui.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.piece_framework.yaml_editor.plugin.YAMLEditorPlugin;

/**
 * YAML スキーマファイル管理クラス.
 * YAML スキーマファイルの管理を行う。<br>
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public final class YAMLSchemaManager {
    
    /** YAML スキーマフォルダ名. */
    public static final String SCHEMA_FOLDER = ".yaml_schemas"; //$NON-NLS-1$
    
    /**
     * コンストラクタ.
     */
    private YAMLSchemaManager() {
    }
    
    /**
     * YAML スキーマファイルを保存するフォルダを作成する.
     * 
     * @param project プロジェクト
     * @return 処理結果
     */
    public static boolean createSchemaFolder(IProject project) {
   
        //プロジェクト内のスキーマフォルダを取得。
        IFolder projectFolder = project.getFolder(SCHEMA_FOLDER);
        
        //フォルダが存在しなければフォルダを作成。
        if (!projectFolder.exists()) {
            try {
                projectFolder.create(false, true, null);
                URL pluginURL = 
                Platform.getBundle(
                    YAMLEditorPlugin.PLUGIN_ID).getEntry(SCHEMA_FOLDER);
                
                //プラグイン側からスキーマフォルダ内のファイル群を取得
                File pluginFolder = 
                           new File(FileLocator.toFileURL(pluginURL).getPath());
                String[] pluginFiles = pluginFolder.list();
                    
                for (int j = 0; j < pluginFiles.length; j++) {
                    //YAML スキーマファイル作成処理を実行
                    copySchemaFile(
                                   projectFolder, pluginFolder, pluginFiles[j]);
                }
            } catch (IOException e1) {
                // TODO 例外処理
                e1.printStackTrace();
            } catch (CoreException e) {
                // TODO 例外処理
                e.printStackTrace();
            }
        }
        
        return true;
    }

    /**
     * YAML スキーマをコピーする. 
     * 
     * @param projectFolder プロジェクト側フォルダ
     * @param pluginFolder プラグイン側フォルダ
     * @param pluginFileName プラグイン側ファイル名
     * @return 処理結果
     * @throws IOException 入出力例外
     * @throws CoreException コア例外
     */
    private static boolean copySchemaFile(IFolder projectFolder,
                                               File pluginFolder,
                                               String pluginFileName)
                                  throws IOException, CoreException {
    
        File pluginFile = new File(pluginFolder, pluginFileName);

        //対象がフォルダの場合は後続処理を行なわない
        if (pluginFile.exists()) {
            if (pluginFile.isDirectory()) {
                return false;
            }
        } else {
            return false;
        }

        //プロジェクトへファイル作成
        IFile projectFile = projectFolder.getFile(pluginFile.getName());
        projectFile.create(new FileInputStream(pluginFile), true, null);
        
        return true;
    }
  
    /**
     * YAML スキーマフォルダから YAML スキーマファイル一覧を取得する.  
     * 
     * @param project プロジェクト
     * @return YAML スキーマファイル一覧
     */
    public static IFile[] getSchemaFiles(IProject project) {
        
        if (project == null) {
            return null;
        }
        
        IFolder folder = project.getFolder(SCHEMA_FOLDER);
        
        ArrayList<IFile> schemaFileList = new ArrayList<IFile>();
        try {
            IResource[] resources = folder.members();
            for (int i = 0; i < resources.length; i++) {
                if (resources[i] instanceof IFile) {
                    IFile file = (IFile) resources[i];
                    String extension = file.getFileExtension();
                    if (extension.equalsIgnoreCase("yaml")) { //$NON-NLS-1$
                        schemaFileList.add(file);
                    }
                }
            }
            
        } catch (CoreException e) {
            // TODO: 例外処理
            e.printStackTrace();
        }
        
        return schemaFileList.toArray(new IFile[0]);
    }
    
    /**
     * YAML ファイルに対応する YAML スキーマファイルを取得する.
     * 該当する YAML スキーマファイルがない場合は null を返す。
     * 
     * @param yamlFile YAML ファイル
     * @return YAML スキーマファイル
     */
    public static IFile getSchemaFileForYAML(IFile yamlFile) {
        
        if (yamlFile == null) {
            return null;
        }
        
        IProject project = yamlFile.getProject();
        IScopeContext projectScope = new ProjectScope(project);
        
        Preferences projectNode = projectScope.getNode(
                                    YAMLEditorPlugin.PLUGIN_ID);
        
        // スキーマフォルダーを取得
        IFolder schemaFolder = getSchemaFolder(project);
        String schemaFolderName = schemaFolder.getFullPath().toString();
        
        IFile schemaFile = null;
        
        try {
            
            // YAML ファイルはプロジェクト名を除いた形式で保存されて
            // いるので、ここでプロジェクト名を削除
            String yamlFileName = 
                    yamlFile.getFullPath().toString().substring(
                            project.getName().length() + 1);
            
            String[] keys = projectNode.keys();
            for (int i = 0; i < keys.length; i++) {
                
                if (keys[i].equals(yamlFileName)) {
                    // スキーマファイルを取得
                    String schemaFileName = projectNode.get(keys[i], null);
                    
                    schemaFile = project.getFile(
                            schemaFolderName + "/" + schemaFileName);
                    break;
                }
            }
        } catch (BackingStoreException e) {
            // TODO: 例外処理
            e.printStackTrace();
        }
        
        return schemaFile;
    }
    
    /**
     * YAML ファイルと YAML スキーマファイルの対応を保存する.
     * 保存には、 Eclipse のプロジェクト単位の設定保存を利用する。
     * 
     * @param yamlFile YAML ファイル
     * @param schemaFile YAML スキーマファイル
     * @return 処理結果
     */
    public static boolean setSchemaFileForYAML(
                                        IFile yamlFile, 
                                        IFile schemaFile) {
        
        if (yamlFile == null || schemaFile == null) {
            return false;
        }
        
        IProject project = yamlFile.getProject();
        IScopeContext projectScope = new ProjectScope(project);
        
        Preferences projectNode = projectScope.getNode(
                                    YAMLEditorPlugin.PLUGIN_ID);
        
        // YAML ファイルフルパスからプロジェクト名を削除
        String yamlFileName = yamlFile.getFullPath().toString().substring(
                                project.getName().length() + 1);
        // スキーマファイルはファイル名のみ
        String schemaFileName = schemaFile.getName();
        
        projectNode.put(yamlFileName, schemaFileName);
        
        try {
            projectNode.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * スキーマフォルダーを返す.
     * 
     * @param project プロジェクト
     * @return スキーマフォルダー
     */
    public static IFolder getSchemaFolder(IProject project) {
        
        IScopeContext projectScope = new ProjectScope(project);
        
        Preferences projectNode = projectScope.getNode(
                                    YAMLEditorPlugin.PLUGIN_ID);
        
        IFolder schemaFolder = null;
        
        String schemaFolderName = projectNode.get("SchemaFolder", null);
        if (schemaFolderName != null) {
            schemaFolder = project.getFolder(schemaFolderName);
        }
        
        return schemaFolder;
    }
    
    /**
     * スキーマフォルダーを保存する.
     * 
     * @param schemaFolder スキーマフォルダー
     * @return 処理結果
     */
    public static boolean setSchemaFolder(IFolder schemaFolder) {
        
        IProject project = schemaFolder.getProject();
        IScopeContext projectScope = new ProjectScope(project);
        
        Preferences projectNode = projectScope.getNode(
                                    YAMLEditorPlugin.PLUGIN_ID);
        
        String schemaFolderName = 
            schemaFolder.getFullPath().toString().substring(
                project.getName().length() + 1);
        
        projectNode.put("SchemaFolder", schemaFolderName);
        
        try {
            projectNode.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
