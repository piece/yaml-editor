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
public class YAMLValidater {

    /**
     * Kwalifyライブラリを使用し、バリデーションを実行する.
     * 
     * @param schemaStream YAMLスキーマ定義ファイル（ストリーム）
     * @param docStream YAMLファイル（ストリーム）
     * @return エラーリスト
     * @throws IOException 入出力例外エラー
     * @see org.eclipse.ui.editors.text.StorageDocumentProvider
     *          #createDocument(java.lang.Object)
     */
    protected static List<Map> validation(InputStream schemaStream,
                                            InputStream docStream)
                                            throws IOException {

        List<Map> errorList = new ArrayList();
     
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

                    Map<String, Comparable> attributeMap = new HashMap();
                    attributeMap.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
                    attributeMap.put(IMarker.MESSAGE, error.getMessage());
                    attributeMap.put(IMarker.LINE_NUMBER,
                                                      error.getLineNumber());
                    errorList.add(attributeMap);
                }
            }
        } catch (SyntaxException e) {
            Map<String, Comparable> attributeMap = new HashMap();
            attributeMap.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            attributeMap.put(IMarker.MESSAGE, e.getMessage());
            attributeMap.put(IMarker.LINE_NUMBER, e.getLineNumer());
            errorList.add(attributeMap);
        }
        return errorList;
    }
}