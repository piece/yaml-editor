package com.piece_framework.piece_ide.yamleditor.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * カラーマネージャー.
 * タグルールを定義する。
 * TODO:タグルールの見直し(そもそもタグでない？)
 * 
 * @author Hideharu Matsufuji
 * @version 0.2.0
 * @since 0.2.0
 * 
 */
public class ColorManager {
    
    // カラーテーブルの初期値
    private static final int INIT_MAP = 10;
    // カラーテーブル
    private Map< RGB, Color > fColorTable = 
                new HashMap< RGB, Color >(INIT_MAP);
    
    
    /**
     * 終了処理を行う.
     * カラーテーブルのすべてのカラーの終了処理を呼出す。
     */
    public void dispose() {
        Iterator e = fColorTable.values().iterator();
        while (e.hasNext()) {
            ((Color) e.next()).dispose();
        }
    }
    
    /**
     * カラーオブジェクトを取得する.
     * 指定されたRGBのカラーオブジェクトがない場合は新たに
     * 生成する。
     * 
     * @param rgb 取得するRGB
     * @return カラーオブジェクト
     */
    public Color getColor(RGB rgb) {
        Color color = (Color) fColorTable.get(rgb);
        if (color == null) {
            color = new Color(Display.getCurrent(), rgb);
            fColorTable.put(rgb, color);
        }
        return color;
    }
}
