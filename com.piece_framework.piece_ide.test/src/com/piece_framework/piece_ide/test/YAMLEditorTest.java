package com.piece_framework.piece_ide.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import junit.framework.TestCase;

/**
 * YAMLエディターテストクラス.
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 */
public class YAMLEditorTest extends TestCase {
    
    private TestProject testProject;

    /**
     * ユニットテスト初期化処理.
     * 
     * @throws Exception 一般例外 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        testProject = new TestProject();
        
    }

    /**
     * ユニットテスト終了処理.
     * 
     * @throws Exception 一般例外
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        testProject.dispose();
    }
    
    /**
     * YAMLエディター起動テスト.
     * 
     * @throws CoreException ランタイムコア例外
     */
    public void testOpenYAMLFile() throws CoreException {
         
        IFile file = null;
        try {
            IFolder folder = testProject.createFolder("config");
            file = testProject.createFile(folder, "test.yaml", "--- YAML%1.1");
            
        } catch (CoreException e) {
            e.printStackTrace();
            fail();
        }
        
        IWorkbenchPage page = getPage();
        IEditorPart part = IDE.openEditor(page, file);
        
        assertTrue(part.toString().indexOf("YAMLEditor") > 0);
    }
    
    /**
     * アクティブページを返す.
     * 
     * @return アクティブページ
     */
    private IWorkbenchPage getPage() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        
        return window.getActivePage();
    }
    
}
