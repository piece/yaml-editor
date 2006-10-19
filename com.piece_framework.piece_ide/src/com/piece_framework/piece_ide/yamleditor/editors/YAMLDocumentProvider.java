package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * YAML ドキュメントプロバイダー.
 * ドキュメントの生成・管理を行う。
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.ui.editors.text.FileDocumentProvider
 * 
 */
public class YAMLDocumentProvider extends FileDocumentProvider {

    /**
     * ドキュメントを生成する.
     * 
     * @param element ドキュメントの元になる要素
     * @return ドキュメント
     * @throws CoreException 共通例外
     * @see org.eclipse.ui.editors.text.StorageDocumentProvider
     *          #createDocument(java.lang.Object)
     */
    protected IDocument createDocument(Object element) throws CoreException {
        IDocument document = super.createDocument(element);
        
        if (document != null) {
            // ドキュメントを意味のある区域に分割する
            IDocumentPartitioner partitioner =
                new FastPartitioner(
                    new YAMLPartitionScanner(),
                    new String[] {
                        YAMLPartitionScanner.YAML_COMMENT,
                        YAMLPartitionScanner.YAML_MAPPING_KEY,
                        YAMLPartitionScanner.YAML_MAPPING_VAL}
                    );
            
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
        return document;
    }
}