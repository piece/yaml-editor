package com.piece_framework.piece_ide.yamleditor.editors;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * YAML スキーマファイル管理クラス.
 * YAML スキーマファイルの管理を行う。<br>
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.jface.text.rules.RuleBasedPartitionScanner
 * 
 */
public final class YAMLSchemaManager {
    
    // YAML スキーマフォルダ名
    private static final String SCHEMA_FOLDER = ".yaml_schema";
    
    /**
     * コンストラクタ.
     */
    private YAMLSchemaManager() {
    }
    
    /**
     * YAML スキーマファイルを保存するフォルダを作成する.
     * フォルダ作成後、 Piece Framework の YAML スキーマをコピーする。 
     * 
     * @param project プロジェクト
     * @return 処理結果
     */
    public static boolean createSchemaFolder(IProject project) {
        // TODO: 「定数が使用されていない」警告を表示させないための暫定処理
        return SCHEMA_FOLDER.equalsIgnoreCase(".yaml_schema");
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
        
        //IFile[] files = new IFile[0];
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
