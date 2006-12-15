package com.piece_framework.piece_ide.yamleditor.editors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kwalify.SyntaxException;
import kwalify.Util;
import kwalify.ValidationException;
import kwalify.Validator;
import kwalify.YamlParser;

import org.eclipse.core.resources.IMarker;



/**
 * YAML バリデーター.
 * YAMLファイルのバリデーションを実行する。
 * 
 * @author Seiichi Sugimoto
 * @version 0.1.0
 * @since 0.1.0
 * 
 */
public class YAMLValidator {

    /**
     * Kwalifyライブラリを使用し、バリデーションを実行する.
     * 
     * @param schemaStream YAMLスキーマ定義ファイル（ストリーム）
     * @param docStream YAMLファイル（ストリーム）
     * @return エラーリスト
     * @throws IOException 入出力例外エラー
     */
    public static List<Map> validation(InputStream schemaStream,
                                            InputStream docStream)
                                            throws IOException {

        List<Map> errorList = new ArrayList<Map>();
     
        try {
            //YAMLスキーマ設定
            Object schema = new YamlParser(Util.untabify(
                         Util.readInputStream(schemaStream))).parse();
          
            //YAMLファイル設定
            YamlParser parser = new YamlParser(Util.untabify(Util
                               .readInputStream(docStream)));
            Object document = parser.parse();
        
            //バリデーションの実行
            Validator validator = new Validator(schema);
            List errors = validator.validate(document);

            //エラー内容をエラー出力用リストにセット
            if (errors != null && errors.size() > 0) {
                parser.setErrorsLineNumber(errors);
                Collections.sort(errors);
                for (Iterator it = errors.iterator(); it.hasNext();) {
                    ValidationException error = (ValidationException) it.next();

                    errorList.add(mapMarker(IMarker.SEVERITY_ERROR,
                                            error.getMessage(),
                                            error.getLineNumber()));
                }
            }
        } catch (SyntaxException e) {
            errorList.add(mapMarker(IMarker.SEVERITY_ERROR,
                                    e.getMessage(), e.getLineNumer()));
        }
        return errorList;
    }
    
    /**
     * マップにマーカーを設定する.
     * @param markType マーカーの種類
     * @param markMessage マークする内容
     * @param markNumber マークする行番号
     * @return マーカーが設定されたマップ
     */
    private static Map<String, Comparable> mapMarker(int markType,
                                                       String markMessage,
                                                       int markNumber) {
        Map<String, Comparable> attributeMap =
                                     new HashMap<String, Comparable>();
        attributeMap.put(IMarker.SEVERITY, markType);
        attributeMap.put(IMarker.MESSAGE, markMessage);
        attributeMap.put(IMarker.LINE_NUMBER, markNumber);
        return attributeMap;
    }
    
}