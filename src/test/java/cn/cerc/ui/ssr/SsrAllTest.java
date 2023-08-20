package cn.cerc.ui.ssr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ SsrBlockTest.class, SsrCallbackTest.class, SsrComboTest.class, SsrDataRowTest.class,
        SsrDatasetTest.class, SsrIfNodeTest.class, SsrTextNodeTest.class, UISsrBlockTest.class, SsrTemplateTest.class,
        VuiGridTest.class, SsrDefaultGridStyleTest.class, SsrMapNodeTest.class, SsrListNodeTest.class,
        VuiFormTest.class, SsrUtilsTest.class, SsrDefaultFormStyleTest.class, UISsrChunkTest.class,
        SsrFormStyleDefaultTest.class })

public class SsrAllTest {

}
