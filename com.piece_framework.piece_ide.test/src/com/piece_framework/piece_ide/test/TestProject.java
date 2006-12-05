package com.piece_framework.piece_ide.test;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;

/**
 * ユニットテスト用プロジェクト作成クラス.
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 */
public class TestProject {
    private IProject project;
    
    /**
     * コンストラクタ.
     * テストプロジェクトを作成する。
     * 
     * @throws CoreException ランタイムコア例外
     */
    public TestProject() throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject("TestProject");
        project.create(null);
        project.open(null);
        
    }
    
    /**
     * プロジェクトを返す.
     * 
     * @return プロジェクト
     */
    public IProject getProject() {
        return project;
    }
    
    /**
     * フォルダを作成する.
     * 
     * @param folderName フォルダ名
     * @return 作成したFolderオブジェクト
     * @throws CoreException ランタイムコア例外
     */
    public IFolder createFolder(String folderName) throws CoreException { 
        IFolder folder = project.getFolder("src");
        folder.create(false, true, null);
        
        return folder;
    }
    
    /**
     * ファイルを作成する.
     * 
     * @param folder ファイルを作成するフォルダ
     * @param fileName ファイル名
     * @param source ファイル内容
     * @return 作成したFileオブジェクト
     * @throws CoreException ランタイムコア例外
     */
    public IFile createFile(IFolder folder, 
                             String fileName, 
                             String source) throws CoreException {
        
        IFile file = folder.getFile(new Path(fileName));
        if (!file.exists()) {
            InputStream is = new ByteArrayInputStream(source.getBytes());
            file.create(is, false, null);
        }
        
        return file;
    }

    /**
     * 終了処理を行う.
     * 作成したテスト用プロジェクトを削除する。
     * 
     * @throws CoreException ランタイムコア例外
     */
    public void dispose() throws CoreException {
        waitForIndexer();
        project.delete(true, true, null);
    }
    
    
    /**
     * インデックス処理の終了を待機する.
     * Java検索は正確で効率的に行うためにインデックスを使用している。
     * プロジェクト削除に伴うインデックスの干渉を避けるため、インデッ
     * クスの割り振りが終わってからプロジェクトの削除を行う。
     * 
     * @throws JavaModelException Javaモデル例外
     */
    private void waitForIndexer() throws JavaModelException {
        
        new SearchEngine().searchAllTypeNames(
            null, 
            null, 
            SearchPattern.R_EXACT_MATCH, 
            IJavaSearchConstants.CLASS, 
            SearchEngine.createJavaSearchScope(new IJavaElement[0]), 
            new TypeNameRequestor() {
                public void acceptType(
                            int modifiers, 
                            char[] packageName, 
                            char[] simpleTypeName, 
                            char[][] enclosingTypeNames, 
                            String path) {
                }
            }, 
            IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, 
            null);
    }
}
