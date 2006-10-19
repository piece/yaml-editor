package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * YAML コンフィギュレーション.
 * YAML エディターの動作をカスタマイズする。
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.source.SourceViewerConfiguration
 * 
 */
public class YAMLConfiguration extends SourceViewerConfiguration {
    private YAMLDoubleClickStrategy doubleClickStrategy;
    private YAMLTagScanner tagScanner;
    private YAMLScanner scanner;
    private ColorManager colorManager;

    /**
     * コンストラクタ.
     * 
     * @param cm カラーマネージャー
     */
    public YAMLConfiguration(ColorManager cm) {
        this.colorManager = cm;
    }
    
    /**
     * YAML エディターがサポートするパーティーションタイプを返す.
     * 
     * @param sourceViewer ソースビュー
     * @return パーティションタイプ
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration
     *          #getConfiguredContentTypes(
     *              org.eclipse.jface.text.source.ISourceViewer)
     */
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[] {
            IDocument.DEFAULT_CONTENT_TYPE,
            YAMLPartitionScanner.YAML_COMMENT,
            YAMLPartitionScanner.YAML_MAPPING_KEY,
            YAMLPartitionScanner.YAML_MAPPING_VAL };
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
     * YAML スキャナーを取得する.
     * 
     * @return YAML スキャナー
     */
    protected YAMLScanner getYAMLScanner() {
        if (scanner == null) {
            scanner = new YAMLScanner(colorManager);
            scanner.setDefaultReturnToken(
                new Token(
                    new TextAttribute(
                        colorManager.getColor(IYAMLColorConstants.DEFAULT))));
        }
        return scanner;
    }
    
    /**
     * YAML タグスキャナーを取得する.
     * 
     * @return YAML タグスキャナー
     */
    protected YAMLTagScanner getYAMLTagScanner() {
        if (tagScanner == null) {
            tagScanner = new YAMLTagScanner(colorManager);
            // TODO:タグスキャナー？
            /*
            tagScanner.setDefaultReturnToken(
                new Token(
                    new TextAttribute(
                        colorManager.getColor(IYAMLColorConstants.TAG))));
            */
        }
        return tagScanner;
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
        
        // TODO:パーティーション単位のダメージャとリペアラの設定
        /*
        DefaultDamagerRepairer dr =
            new DefaultDamagerRepairer(getXMLTagScanner());
        reconciler.setDamager(dr, YAMLPartitionScanner.YAML_MAPPING_KEY);
        reconciler.setRepairer(dr, YAMLPartitionScanner.YAML_MAPPING_KEY);
        
        dr = new DefaultDamagerRepairer(getXMLScanner());
        reconciler.setDamager(dr, YAMLPartitionScanner.YAML_MAPPING_VAL);
        reconciler.setRepairer(dr, YAMLPartitionScanner.YAML_MAPPING_VAL);
        */
        DefaultDamagerRepairer dr;
        dr = new DefaultDamagerRepairer(getYAMLScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        NonRuleBasedDamagerRepairer ndr =
            new NonRuleBasedDamagerRepairer(
                new TextAttribute(
                    colorManager.getColor(IYAMLColorConstants.YAML_COMMENT)));
        reconciler.setDamager(ndr, YAMLPartitionScanner.YAML_COMMENT);
        reconciler.setRepairer(ndr, YAMLPartitionScanner.YAML_COMMENT);
        
        ndr = new NonRuleBasedDamagerRepairer(
                new TextAttribute(
                    colorManager.getColor(
                        IYAMLColorConstants.YAML_MAPPING_KEY)));
        reconciler.setDamager(ndr, YAMLPartitionScanner.YAML_MAPPING_KEY);
        reconciler.setRepairer(ndr, YAMLPartitionScanner.YAML_MAPPING_KEY);
        
        ndr = new NonRuleBasedDamagerRepairer(
                new TextAttribute(
                    colorManager.getColor(
                        IYAMLColorConstants.YAML_MAPPING_VAL)));
        reconciler.setDamager(ndr, YAMLPartitionScanner.YAML_MAPPING_VAL);
        reconciler.setRepairer(ndr, YAMLPartitionScanner.YAML_MAPPING_VAL);
        
        return reconciler;
    }

}