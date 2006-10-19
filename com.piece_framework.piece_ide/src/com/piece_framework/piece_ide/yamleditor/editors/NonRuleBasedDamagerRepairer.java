package com.piece_framework.piece_ide.yamleditor.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.custom.StyleRange;

/**
 * ルールなし基本ダメージャ／リペアラ.
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * @see org.eclipse.jface.text.rules.MultiLineRule
 * 
 */
public class NonRuleBasedDamagerRepairer
    implements IPresentationDamager, IPresentationRepairer {

    // 対象ドキュメント
    private IDocument fDocument;
    // デフォルトのテキスト属性
    private TextAttribute fDefaultTextAttribute;
    
    /**
     * コンストラクタ.
     * 
     * @param defaultTextAttribute デフォルトのテキスト属性
     */
    public NonRuleBasedDamagerRepairer(TextAttribute defaultTextAttribute) {
        Assert.isNotNull(defaultTextAttribute);
    
        fDefaultTextAttribute = defaultTextAttribute;
    }

    /**
     * ドキュメント設定.
     * 
     * @param document ドキュメント
     * @see org.eclipse.jface.text.presentation.IPresentationRepairer
     *          #setDocument(IDocument)
     */
    public void setDocument(IDocument document) {
        fDocument = document;
    }
    
    /**
     * オフセットからの行の終了位置を返す.
     *
     * @param offset オフセット
     * @return オフセットからの行の終了位置
     * @exception BadLocationException カレントドキュメントの位置が不正
     */
    protected int endOfLineOf(int offset) throws BadLocationException {
        
        IRegion info = fDocument.getLineInformationOfOffset(offset);
        if (offset <= info.getOffset() + info.getLength()) {
            return info.getOffset() + info.getLength();
        }
        
        int line = fDocument.getLineOfOffset(offset);
        try {
            info = fDocument.getLineInformation(line + 1);
            return info.getOffset() + info.getLength();
        } catch (BadLocationException x) {
            return fDocument.getLength();
        }
    }

    /**
     * ダメージリペアラを取得する.
     * 
     * @param partition パーティーション
     * @param event イベント
     * @param documentPartitioningChanged パーティーションで変更があったか
     * @return ダメージリペアラ
     * @see org.eclipse.jface.text.presentation.IPresentationDamager
     *          #getDamageRegion(ITypedRegion, DocumentEvent, boolean)
     */
    public IRegion getDamageRegion(
        ITypedRegion partition,
        DocumentEvent event,
        boolean documentPartitioningChanged) {
        if (!documentPartitioningChanged) {
            try {
            
                IRegion info =
                    fDocument.getLineInformationOfOffset(event.getOffset());
                int start = Math.max(partition.getOffset(), info.getOffset());
            
                int end = event.getOffset();
                if (event.getText() == null) {
                    end += event.getLength();
                } else {
                    end += event.getText().length();
                }
                
                if (info.getOffset() <= end
                    && end <= info.getOffset() + info.getLength()) {
                    // optimize the case of the same line
                    end = info.getOffset() + info.getLength();
                } else {
                    end = endOfLineOf(end);
                }
                
                end =
                    Math.min(
                        partition.getOffset() + partition.getLength(),
                        end);
                return new Region(start, end - start);
            
            } catch (BadLocationException x) {
            }
        }
        
        return partition;
    }

    /**
     * プレゼンテーションを生成する.
     * 
     * @param presentation テキストプレゼンテーション
     * @param region リージョン
     * @see org.eclipse.jface.text.presentation.IPresentationRepairer
     *          #createPresentation(TextPresentation, ITypedRegion)
     */
    public void createPresentation(
        TextPresentation presentation,
        ITypedRegion region) {
        addRange(
            presentation,
            region.getOffset(),
            region.getLength(),
            fDefaultTextAttribute);
    }

    /**
     * 指定されたテキストプレゼンテーションにスタイルを追加する.
     * 
     * @param presentation テキストプレゼンテーション
     * @param offset オフセット
     * @param length スタイル長
     * @param attr テキスト属性
     */
    protected void addRange(
        TextPresentation presentation,
        int offset,
        int length,
        TextAttribute attr) {
        if (attr != null) {
            presentation.addStyleRange(
                new StyleRange(
                    offset,
                    length,
                    attr.getForeground(),
                    attr.getBackground(),
                    attr.getStyle()));
        }
    }
}