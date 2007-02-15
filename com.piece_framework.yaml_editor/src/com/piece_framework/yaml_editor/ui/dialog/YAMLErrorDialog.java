// $Id$
package com.piece_framework.yaml_editor.ui.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;

import com.piece_framework.yaml_editor.plugin.Messages;
import com.piece_framework.yaml_editor.plugin.YAMLEditorPlugin;

/**
 * エラーダイアログ.
 * Exception発生時にエラーメッセージダイアログを表示させる。
 * 
 * @author Seiichi Sugimoto
 * @version 0.1.0
 * @since 0.1.0
 */
public final class YAMLErrorDialog  {

    /** エラーコード：入出力例外. */
    public static final int ERROR_NUM_IO = 1;

    /** エラーコード：その他例外. */
    public static final int ERROR_NUM_OTHER = 99;

    /**
     * コンストラクタ.
     */
    private YAMLErrorDialog() {
    }
   
    /**
     * エラーダイアログ表示.
     * エラーメッセージダイアログを表示させる。
     * 
     * @param errorLevel エラーレベル
     * @param errorNum エラー番号
     * @param exception 例外
     */
    public static void showDialog(int errorLevel,
                                    int errorNum,
                                    Throwable exception) {
        showDialog(new Status(errorLevel,
                              YAMLEditorPlugin.PLUGIN_ID,
                              errorNum,
                              Messages.getString(
                                "ErrorMessageDialog.MessageException"),
                              exception));
    }
    
    /**
     * エラーダイアログ表示.
     * エラーメッセージダイアログを表示させる。
     * 
     * @param status エラーステータス
     */
    public static void showDialog(IStatus status) {
        ErrorDialog.openError(
               Display.getCurrent().getActiveShell(),
               Messages.getString("ErrorMessageDialog.WindowTitle"),
               Messages.getString("ErrorMessageDialog.MessageTitle"),
               status);
    }
}
