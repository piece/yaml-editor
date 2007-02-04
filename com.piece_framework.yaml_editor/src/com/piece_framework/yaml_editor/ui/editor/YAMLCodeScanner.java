// $Id$
package com.piece_framework.yaml_editor.ui.editor;

import java.util.Vector;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.RGB;

/**
 * YAML コードスキャナー.
 * YAML のコードのルールを定義・設定する。<br>
 * <br>
 * ・マッピング(ハッシュ)キー<br>
 * ・引用符で囲まれた固定文字列<br>
 * ・YAMLバージョン指定<br>
 * ・コメント<br>
 * ・シーケンス(配列)の"-"<br>
 * ・ドキュメント区切りの"---"<br>
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.jface.text.rules.RuleBasedScanner
 */
public final class YAMLCodeScanner extends RuleBasedScanner {
    
    /**
     * ワード認識用.
     * 
     * @author Hideharu Matsufuji
     * @version 0.1.0
     * @since 0.1.0
     * @see org.eclipse.jface.text.rules.IWordDetector
     */
    private static class YAMLWordDetector implements IWordDetector {
        
        /**
         * 指定されたキャラクタをワードの一部として認識するかを返す.
         * 
         * @param c チェック対象キャラクタ
         * @return 判定結果
         * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
         */
        public boolean isWordPart(char c) {
            return Character.isLetter(c) || c == '-';
        }

        /**
         * 指定されたキャラクタをワードの先頭として認識するかを返す.
         * 
         * @param c チェック対象キャラクタ
         * @return 判定結果
         * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
         */
        public boolean isWordStart(char c) {
            return Character.isLetter(c) || c == '-';
        }
    }
    
    /**
     * 空白認識用.
     * 
     * @author Hideharu Matsufuji
     * @version 0.1.0
     * @since 0.1.0
     * @see org.eclipse.jface.text.rules.IWhitespaceDetector
     */
    private static class YAMLWhitespaceDetector implements IWhitespaceDetector {
        
        /**
         * 指定されたキャラクタを空白として認識するかを返す.
         * 
         * @param c チェック対象キャラクタ
         * @return 判定結果
         * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
         */
        public boolean isWhitespace(char c) {
            return Character.isWhitespace(c);
        }
    }
    
    /**
     * マッピング(ハッシュ)キールール.
     * マッピング(ハッシュ)のキーは以下のルールで判断する。<br>
     * ・アルファベット、引用符(')、二重引用符(")のいずれかで始まる。<br>
     * ・ワードの直後がコロン(:)である。<br>
     * ・コロン(:)の直後が半角スペース又は改行である。<br>
     * 
     * @author Hideharu Matsufuji
     * @version 0.1.0
     * @since 0.1.0
     * @see org.eclipse.jface.text.rules.IRule
     */
    private static class YAMLMappingKeyRule implements IRule {
        
        private IToken fToken;
        
        /**
         * コンストラクタ.
         * 
         * @param t ルールに合致したときに適用するトークン。
         */
        public YAMLMappingKeyRule(IToken t) {
            this.fToken = t;
        }
        
        /**
         * マッピング(ハッシュ)キールールに合致するか判定する.
         * マッピング(ハッシュ)キールールに合致した場合は指定されたトークンを返す。
         * 合致しない場合はToken.UNDEFINEDを返す。
         * 
         * @param scanner 判定対象のキャラクタを取り出すスキャナ
         * @return ルールに適用されるトークン。
         * @see org.eclipse.jface.text.rules.IRule
         *          #evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
         */
        public IToken evaluate(ICharacterScanner scanner) {
            IToken retToken = Token.UNDEFINED;
            
            char c = (char) scanner.read();
            
            if (Character.isLetter(c) || c == '\"' || c == '\'') {
                char bc = '\0';
                char quatation = '\0';
                int count = 1;
                
                // 引用符で始まる場合はそれを取得しておく
                if (c == '\"' || c == '\'') {
                    quatation = c;
                }
                do {
                    bc = c;
                    c = (char) scanner.read();
                    count++;
                    
                    // 引用符が閉じた場合はリセット
                    if (c == quatation) {
                        quatation = '\0';
                    }
                    // 引用符で囲まれた中で半角スペースの場合は
                    // 文字を置換して、終了しないようにする
                    if (quatation != '\0' && c == ' ') {
                        c = '_';
                    }
                } while (Character.isDefined(c) && c != ' ' && c != '\n');
                
                // ':'のあとがスペースか改行なら条件に合致
                if (bc == ':') {
                    retToken = fToken;
                } else {
                    for (int i = 0; i < count; i++) {
                        scanner.unread();
                    }
                }
            } else {
                scanner.unread();
            }
            
            return retToken;
        }
        
    }
    
    /**
     * YAMLバージョン指定ルール.
     * YAMLバージョン指定は以下のルールで判断する。<br>
     * ・"--- %YAML"で始まる。<br>
     * ・"--- %YAML"直後にひとつ半角スペースをはさんで、次の半角スペース<br>
     * 　又は改行までをバージョンとする。<br>
     * ・"--- %YAML"直後に半角スペースがない場合は、次の半角スペースまで<br>
     * 　を対象とする。<br>
     * <br>
     * 例：<br>
     * 　"--- %YAML 1.1  # YAMLバージョン指定"<br>
     * 　　"--- %YAML 1.1"がYAMLバージョン指定ルールとなる。<br>
     * <br>
     * 　"--- %YAML2.0 # YAML Version"<br>
     * 　　"--- %YAML2.0"がYAMLバージョン指定ルールとなる。<br>
     * 
     * @author Hideharu Matsufuji
     * @version 0.1.0
     * @since 0.1.0
     * @see org.eclipse.jface.text.rules.IRule
     */
    private static class YAMLVersionRule implements IRule {
            
        private IToken fToken;
        private static final String VERSION = "--- %YAML"; //$NON-NLS-1$
        
        /**
         * コンストラクタ.
         * 
         * @param t ルールに合致したときに適用するトークン。
         */
        public YAMLVersionRule(IToken t) {
            this.fToken = t;
        }

        /**
         * YAMLバージョン指定ルールに合致するか判定する.
         * YAMLバージョン指定ルールに合致した場合は指定されたトークンを返す。
         * 合致しない場合はToken.UNDEFINEDを返す。
         * 
         * @param scanner 判定対象のキャラクタを取り出すスキャナ
         * @return ルールに適用されるトークン。
         * @see org.eclipse.jface.text.rules.IRule
         *          #evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
         */
        public IToken evaluate(ICharacterScanner scanner) {
            
            char[] version = VERSION.toCharArray();
            
            IToken retToken = Token.UNDEFINED;
            
            char c = (char) scanner.read();
            boolean check = true;
            int count = 1;
            
            for (int i = 0; i < version.length; i++) {
                if (version[i] != c) {
                    check = false;
                    break;
                }
                c = (char) scanner.read();
                count++;
            }
            if (check) {
                // バージョン番号までを対象とする
                if (c == ' ') {
                    c = (char) scanner.read();
                }
                do {
                    c = (char) scanner.read();
                } while (Character.isDefined(c) && c != ' ' && c != '\n');
                
                retToken = fToken;
                scanner.unread();
                
            } else {
                for (int i = 0; i < count; i++) {
                    scanner.unread();
                }
            }
            
            return retToken;
        }
        
        
    }

    /** YAML マッピング(キー)色. */
    private static final RGB YAML_MAPPING_KEY_COLOR = new RGB(10, 200, 10);
    /** YAML 固定文字列色. */
    private static final RGB YAML_STRING_COLOR = new RGB(0, 0, 255);
    /** YAML バージョン指定色. */
    private static final RGB YAML_VERSION_COLOR = new RGB(0, 150, 150);
    /** YAML コメント色. */
    private static final RGB YAML_COMMENT_COLOR = new RGB(0, 100, 200);
    /** YAML シーケンス(配列)色. */
    private static final RGB YAML_SEQUENCE_COLOR = new RGB(0, 0, 255);
    /** YAML ドキュメント区切り色. */
    private static final RGB YAML_DOC_SEPARATOR_COLOR = new RGB(255, 0, 0);
    
    /** デフォルト色. */
    private static final RGB YAML_DEFAULT_COLOR = new RGB(0, 0, 0);
    
    private static YAMLCodeScanner fCodeScanner;
    
    /**
     * コンストラクタ.
     * ルール定義を行う。
     */
    private YAMLCodeScanner() {
        
        Vector<IRule> rules = new Vector<IRule>();
        YAMLColorManager manager = YAMLColorManager.getColorManager();
        
        IToken defaultToken = new Token(new TextAttribute(
                manager.getColor(YAML_DEFAULT_COLOR)));
        
        IToken keyToken = new Token(
                new TextAttribute(manager.getColor(YAML_MAPPING_KEY_COLOR)));
        IToken stringToken = new Token(
                new TextAttribute(manager.getColor(YAML_STRING_COLOR)));
        IToken versionToken = new Token(
                new TextAttribute(manager.getColor(YAML_VERSION_COLOR)));
        IToken commentToken = new Token(
                new TextAttribute(manager.getColor(YAML_COMMENT_COLOR)));
        IToken sequenceToken = new Token(
                new TextAttribute(manager.getColor(YAML_SEQUENCE_COLOR)));
        IToken docSeparatorToekn = new Token(
                new TextAttribute(manager.getColor(YAML_DOC_SEPARATOR_COLOR)));
        
        WordRule wordRule = new WordRule(new YAMLWordDetector(), defaultToken);
        
        // マッピング(キー)
            // ":"より前
        rules.add(new YAMLMappingKeyRule(keyToken));
        // 固定文字列
        rules.add(new SingleLineRule(
                "\"", "\"", stringToken)); //$NON-NLS-1$ //$NON-NLS-2$
        rules.add(new SingleLineRule(
                "\'", "\'", stringToken)); //$NON-NLS-1$ //$NON-NLS-2$
        // バージョン指定
            // "--- %YAML"以降の1行
        rules.add(new YAMLVersionRule(versionToken));
        // コメント
            // "#"以降の1行
        rules.add(new EndOfLineRule("#", commentToken)); //$NON-NLS-1$
        // シーケンス(配列)
            // "-"のみ
        wordRule.addWord("-", sequenceToken); //$NON-NLS-1$
        // ドキュメント区切り
            // "---"のみ
        wordRule.addWord("---", docSeparatorToekn); //$NON-NLS-1$
        rules.add(wordRule);
        
        // 空白ルールを設定
        rules.add(new WhitespaceRule(new YAMLWhitespaceDetector()));
        
        IRule[] r = new IRule[1];
        setRules(rules.toArray(r));
    }
    
    /**
     * YAML コードスキャナーのインスタンスを返す.
     * 
     * @return YAML コードスキャナー.
     */
    public static YAMLCodeScanner getScanner() {
        if (fCodeScanner == null) {
            fCodeScanner = new YAMLCodeScanner();
        }
        return fCodeScanner;
    }
    
}

