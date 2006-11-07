package com.piece_framework.piece_ide.yamleditor.editors;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kwalify.Util;
import kwalify.ValidationException;
import kwalify.Validator;
import kwalify.YamlParser;


/**
 * YAML バリデーター.
 * YAMLファイルのバリデーションを実行する。
 * 
 * @author Seiichi Sugimoto
 * @version 0.2.0
 * @since 0.2.0
 * 
 */
public final class YAMLValidater {

    /**
     * 
     */
    private YAMLValidater() {
    }
    
    /**
     * バリデーションを実行する.
     * 
     * @param yamlSchemaStr スキーマ
     * @param yamlStr YAMLテキスト
     * @throws Exception 共通例外
     * @throws IOException 
     * @throws SyntaxException 
     * @see org.eclipse.ui.editors.text.StorageDocumentProvider
     *          #createDocument(java.lang.Object)
     */
      @SuppressWarnings("unchecked")
    public static void validation(String yamlSchemaStr, 
                                      String yamlStr) throws Exception {

        //YAMLスキーマ設定
        Object schema = new YamlParser(Util.untabify(yamlSchemaStr)).parse();

        //YAMLファイルの読み込み
        YamlParser parser = new YamlParser(Util.untabify(yamlStr));
        Object document = parser.parse();

        //バリデーションの実行
        Validator validator = new Validator(schema);
        List errors = validator.validate(document);

        //エラー出力
        if (errors != null && errors.size() > 0) {
            parser.setErrorsLineNumber(errors);
            Collections.sort(errors);
            for (Iterator it = errors.iterator(); it.hasNext();) {
                ValidationException error = (ValidationException) it.next();
                int linenum = error.getLineNumber();
                String path = error.getPath();
                String mesg = error.getMessage();
                System.out.println("- " + linenum + ": [" + path + "] " + mesg);
            }
        }
    }
}