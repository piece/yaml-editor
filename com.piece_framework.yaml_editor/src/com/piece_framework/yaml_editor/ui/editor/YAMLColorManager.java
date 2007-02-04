package com.piece_framework.yaml_editor.ui.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * YAML カラーマネージャー.
 * YAML Editor で使用する Color オブジェクトを管理する。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public final class YAMLColorManager {
    
    // カラーテーブル
    private Map< RGB, Color > fColorTable = 
                new HashMap< RGB, Color >();
    
    private static YAMLColorManager fManager;
    
    /**
     * コンストラクタ.
     */
    private YAMLColorManager() {
    }
    
    /**
     * YAML カラーマネージャーのインスタンスを返す.
     * 
     * @return YAML カラーマネージャー
     */
    public static YAMLColorManager getColorManager() {
        if (fManager == null) {
            fManager = new YAMLColorManager();
        }
        return fManager;
    }
    
    /**
     * 指定された RGB オブジェクトに対応する Color オブジェクトを返す.
     * 
     * @param rgb RGB オブジェクト
     * @return Color オブジェクト
     */
    public Color getColor(RGB rgb) {
        Color color = (Color) fColorTable.get(rgb);
        if (color == null) {
            color = new Color(Display.getCurrent(), rgb);
            fColorTable.put(rgb, color);
        }
        return color;
    }
    
    /**
     * 保持している Color オブジェクトをすべてに対して、終了処理を行う.
     */
    public void dispose() {
        Iterator e = fColorTable.values().iterator();
        while (e.hasNext()) {
            ((Color) e.next()).dispose();
        }
    }
    
}
