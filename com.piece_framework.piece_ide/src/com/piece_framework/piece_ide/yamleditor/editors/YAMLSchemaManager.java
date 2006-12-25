package com.piece_framework.piece_ide.yamleditor.editors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

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
    
    // YAML プラグインID
    private static final String YAML_PLUGIN_ID
                               = "com.piece_framework.piece_ide.yamleditor";

    /** YAML スキーマフォルダ名. */
    public static final String SCHEMA_FOLDER = ".yaml_schemas";
    
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
                Platform.getBundle(YAML_PLUGIN_ID).getEntry(SCHEMA_FOLDER);
                
                //プラグイン側からスキーマフォルダ内のファイル群を取得
                File pluginFolder = 
                           new File(FileLocator.toFileURL(pluginURL).getPath());
                String[] pluginFiles = pluginFolder.list();
                    
                for (int j = 0; j < pluginFiles.length; j++) {
                    //YAML スキーマファイル作成処理を実行
                    createSchemaFile(
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
    private static boolean createSchemaFile(IFolder projectFolder,
                                               File pluginFolder,
                                               String pluginFileName)
                                  throws IOException, CoreException {
    
        File pluginFile = new File(pluginFolder, pluginFileName);
        FileReader fileReader  = new FileReader(pluginFile);
        BufferedReader bufReader = new BufferedReader(fileReader);
        String fileLine;
        StringBuffer buf = new StringBuffer();
        
        while ((fileLine = bufReader.readLine()) != null) {
            buf.append(fileLine + "\r\n");
        }
        bufReader.close();
        fileReader.close();
        
        //プロジェクトへファイル作成
        IFile projectFile = projectFolder.getFile(pluginFile.getName());
        projectFile.create(new ByteArrayInputStream(
                                  buf.toString().getBytes()), true, null);
        
        return true;
    }
  
    /**
     * YAML スキーマフォルダから YAML スキーマファイル一覧を取得する.  
     * 
     * @param project プロジェクト
     * @return YAML スキーマファイル一覧
     */
    public static IFile[] getSchemaFiles(IProject project) {
        
        IFolder folder = project.getFolder(SCHEMA_FOLDER);
        
        ArrayList<IFile> schemaFileList = new ArrayList<IFile>();
        try {
            IResource[] resources = folder.members();
            for (int i = 0; i < resources.length; i++) {
                if (resources[i] instanceof IFile) {
                    IFile file = (IFile) resources[i];
                    
                    if (file.getFileExtension().equalsIgnoreCase("yaml")) {
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
     * @param project プロジェクト
     * @param yamlFile YAML ファイル
     * @return YAML スキーマファイル
     */
    public static IFile getSchemaFileForYAML(IProject project, 
                                               IFile yamlFile) {
        
        return null;
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
        
        return true;
    }
}
