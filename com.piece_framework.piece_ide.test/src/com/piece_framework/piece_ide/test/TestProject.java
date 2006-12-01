package com.piece_framework.piece_ide.test;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * ユニットテスト用プロジェクト作成クラス.
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 */
public class TestProject {
    private IProject project;
    private IJavaProject javaProject;
    private IPackageFragmentRoot sourceFolder;
    
    public TestProject() throws CoreException{
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject("TestProject");
        project.create(null);
        project.open(null);
        javaProject = JavaCore.create(project);
        IFolder binFolder = createBinFolder();
        setJavaNature();
        javaProject.setRawClasspath(new IClasspathEntry[0], null);
        createOutputFolder(binFolder);
        addSystemLibraries();
    }
    
    public IProject getProject(){
        return project;
    }
    
    public IJavaProject getJavaProject(){
        return javaProject;
    }
    
    public void addJar(String plugin, String jar) 
                throws MalformedURLException,
                       IOException,
                       JavaModelException {
        
        Path result = findFileInPlugin(plugin, jar);
        IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
        IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length+1];
        System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
        newEntries[oldEntries.length] = JavaCore.newLibraryEntry(result, null, null);
        javaProject.setRawClasspath(newEntries, null);
    }
    
    public IPackageFragment createPackage(String name) throws CoreException{
        if(sourceFolder == null ){
            sourceFolder = createSourceFolder();
        }
        return sourceFolder.createPackageFragment(name, false, null);
    }
    
    public IType createType(IPackageFragment pack, 
                            String cuName,
                            String source) throws JavaModelException{
        StringBuffer buf = new StringBuffer();
        buf.append("package " + pack.getElementName() + ";\n" );
        buf.append("\n");
        buf.append(source);
        ICompilationUnit cu = pack.createCompilationUnit(
                                cuName,
                                buf.toString(),
                                false,
                                null);
        return cu.getTypes()[0];
    }
    
    public void dispose() throws CoreException{
        waitForIndexer();
        project.delete(true, true, null);
    }
    
    private IFolder createBinFolder() throws CoreException{
        IFolder binFolder = project.getFolder("bin");
        binFolder.create(false, true, null);
        return binFolder;
        
    }
    
    private void setJavaNature() throws CoreException{
        IProjectDescription description = project.getDescription();
        description.setNatureIds(new String[]{JavaCore.NATURE_ID});
        project.setDescription(description, null);
    }
    
    private void createOutputFolder(IFolder binFolder)
                    throws JavaModelException{
        IPath outputLocation = binFolder.getFullPath();
        javaProject.setOutputLocation(outputLocation, null);
    }
    
    private IPackageFragmentRoot createSourceFolder() 
                    throws CoreException{ 
        IFolder folder = project.getFolder("src");
        folder.create(false, true, null);
        IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);
        IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
        IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length+1];
        System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
        newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
        javaProject.setRawClasspath(newEntries, null);
        return root;
    }
    
    private void addSystemLibraries() throws JavaModelException {
        IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
        IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length+1];
        System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
        newEntries[oldEntries.length] = JavaRuntime.getDefaultJREContainerEntry();
        javaProject.setRawClasspath(newEntries, null);
    }
    
    private Path findFileInPlugin(String plugin, String file) 
                    throws MalformedURLException, IOException{
        
        URL pluginURL = Platform.getBundle(plugin).getEntry("/");
        URL jarURL = new URL(pluginURL, file);
        URL localJarURL = FileLocator.toFileURL(jarURL);
        return new Path(localJarURL.getPath());
    }
    
    private void waitForIndexer() throws JavaModelException{
        
        new SearchEngine().searchAllTypeNames(
            null, 
            null, 
            SearchPattern.R_EXACT_MATCH, 
            IJavaSearchConstants.CLASS, 
            SearchEngine.createJavaSearchScope(new IJavaElement[0]), 
            new TypeNameRequestor(){
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
