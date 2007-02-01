package com.piece_framework.yaml_editor.yamleditor.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;

/**
 * YAML コンフィギュレーション.
 * YAML Editor の動作をカスタマイズする。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.jface.text.source.SourceViewerConfiguration
 * 
 */
public class YAMLConfiguration extends SourceViewerConfiguration {
    private YAMLDoubleClickStrategy doubleClickStrategy;
    
    /**
     * ルールを持たないスキャナクラス.
     * テキスト属性のみを設定可能なスキャナクラス。
     * 
     * @author Hideharu Matsufuji
     * @version 0.1.0
     * @since 0.1.0
     * @see org.eclipse.jface.text.rules.BufferedRuleBasedScanner
     */
    static class SingleTokenScanner extends BufferedRuleBasedScanner {
        
        /**
         * コンストラクタ.
         * テキスト属性を渡す。
         * 
         * @param attribute テキスト属性
         */
        public SingleTokenScanner(TextAttribute attribute) {
            setDefaultReturnToken(new Token(attribute));
        }
    }    
    
    /**
     * YAML Editor がサポートするパーティーションタイプを返す.
     * 
     * @param sourceViewer ソースビュー
     * @return パーティションタイプ
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration
     *          #getConfiguredContentTypes(
     *              org.eclipse.jface.text.source.ISourceViewer)
     */
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        int patternNum = YAMLPartitionScanner.YAML_PARTITION_TYPES.length + 1;
        String[] types = new String[patternNum];
        
        types[0] = IDocument.DEFAULT_CONTENT_TYPE;
        for (int i = 1; i < patternNum; i++) {
            types[i] = YAMLPartitionScanner.YAML_PARTITION_TYPES[i - 1];
        }
        
        return types;
    }
    
    /**
     * ダブルクリック時の動作を決定する実装クラスを返す.
     * 
     * @param sourceViewer ソースビュー
     * @param contentType コンテントタイプ
     * @return ダブルクリック時の動作を決定する実装クラス
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration
     *          #getDoubleClickStrategy(
     *              org.eclipse.jface.text.source.ISourceViewer, 
     *              java.lang.String)
     */
    public ITextDoubleClickStrategy getDoubleClickStrategy(
        ISourceViewer sourceViewer,
        String contentType) {
        
        if (doubleClickStrategy == null) {
            doubleClickStrategy = new YAMLDoubleClickStrategy();
        }
        return doubleClickStrategy;
    }
    
    /**
     * エディターのプレゼンテーションを設定・取得する.
     * 
     * @param sourceViewer ソースビュー
     * @return プレゼンテーションリコンセラー
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration
     *          #getPresentationReconciler(
     *              org.eclipse.jface.text.source.ISourceViewer)
     */
    public IPresentationReconciler getPresentationReconciler(
        ISourceViewer sourceViewer) {
        
        PresentationReconciler reconciler = new PresentationReconciler();
        
        DefaultDamagerRepairer dr;
        
        int partitionNum = YAMLPartitionScanner.YAML_PARTITION_TYPES.length;
        YAMLColorManager manager = YAMLColorManager.getColorManager();
        for (int i = 0; i < partitionNum; i++) {
            
            Color color = manager.getColor(
                            YAMLPartitionScanner.YAML_PARTITION_COLORS[i]);
            
            dr = new DefaultDamagerRepairer(
                    new SingleTokenScanner(new TextAttribute(color)));
            
            reconciler.setDamager(dr, 
                        YAMLPartitionScanner.YAML_PARTITION_TYPES[i]);
            reconciler.setRepairer(dr, 
                        YAMLPartitionScanner.YAML_PARTITION_TYPES[i]);
        }
        
        dr = new DefaultDamagerRepairer(YAMLCodeScanner.getScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        
        return reconciler;
    }

    
}