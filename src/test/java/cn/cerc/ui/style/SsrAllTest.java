package cn.cerc.ui.style;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ SsrTemplateTest.class, SsrCallbackTest.class, SsrComboTest.class, SsrDataRowTest.class,
        SsrDatasetTest.class, SsrIfNodeTest.class, SsrTextNodeTest.class, UITemplateBlockTest.class,
        SsrDefineTest.class, UITemplateGridTest.class, SsrDefaultGridStyleTest.class, SsrMapNodeTest.class,
        SsrListNodeTest.class})

public class SsrAllTest {

}
