package com.piece_framework.piece_ide;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * 
 * 実行クラス
 *
 */
public class PieceAction implements IWorkbenchWindowActionDelegate {


    /**
     * 終了処理
     *
     */
    public void dispose() {
        // TODO 自動生成されたメソッド・スタブ

    }

    /**
     * 初期処理
     * @param window the window that provides the context for this delegate
     */
    public void init(IWorkbenchWindow window) {
        // TODO 自動生成されたメソッド・スタブ

    }

    /**
     * 実行処理
     * @param action the action proxy that handles the presentation portion
     *      of the action
     */
    public void run(IAction action) {
        // メッセージダイアログ表示
        MessageDialog.openInformation(null, null, "ここからPiece-ideが始まります！");

    }

    /**
     * デリゲート時処理
     * @param action the action proxy that handles presentation portion of
     * 		the action
     * @param selection the current selection, or <code>null</code> if there
     * 		is no selection.
     */
    public void selectionChanged(IAction action, ISelection selection) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
