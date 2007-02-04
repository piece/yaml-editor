// $Id$
package test.com.piece_framework.yaml_editor.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.piece_framework.yaml_editor.util.YAMLValidator;

/**
 * YAMLバリデーターーテストクラス.
 * 
 * @author Seiichi Sugimoto
 * @version 0.1.0
 * @since 0.1.0
 */
public class YAMLValidatorTest extends TestCase {

    /**
     * ユニットテスト初期化処理.
     * 
     * @throws Exception 一般例外 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
    }

    /**
     * ユニットテスト終了処理.
     * 
     * @throws Exception 一般例外
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
    }

    /**
     * YAMLバリデーターテスト.
     * 
     */
    public void testValidator() {
         
        StringBuffer schama = 
            new StringBuffer("type:      seq\r\n");     //$NON-NLS-1$
        schama.append("required:  yes\r\n");            //$NON-NLS-1$
        schama.append("sequence:\r\n");                 //$NON-NLS-1$
        schama.append("   - type:      map\r\n");       //$NON-NLS-1$
        schama.append("     required:  yes\r\n");       //$NON-NLS-1$
        schama.append("     mapping:\r\n");             //$NON-NLS-1$
        schama.append("      \"name\":\r\n");           //$NON-NLS-1$
        schama.append("         type:      str\r\n");   //$NON-NLS-1$
        schama.append("         required:  yes\r\n");   //$NON-NLS-1$
        schama.append("         unique:    yes\r\n");   //$NON-NLS-1$
        ByteArrayInputStream schamaStream =
                      new ByteArrayInputStream(schama.toString().getBytes());

        StringBuffer yaml = new StringBuffer("- name:    Sumire"); //$NON-NLS-1$
        ByteArrayInputStream yamlStream =
                      new ByteArrayInputStream(yaml.toString().getBytes());

        List<Map> errorList = new ArrayList<Map>();
        try {
            errorList = YAMLValidator.validation(schamaStream, yamlStream);
            assertEquals(0, errorList.size());
        } catch (IOException e) {
        }
    }
}
