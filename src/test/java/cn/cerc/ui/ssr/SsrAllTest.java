package cn.cerc.ui.ssr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ SsrBlockTest.class, SsrCallbackTest.class, SsrComboTest.class, SsrDataRowTest.class,
        SsrDatasetTest.class, SsrIfNodeTest.class, SsrTextNodeTest.class, UISsrBlockTest.class, SsrTemplateTest.class,
        UISsrGridTest.class, SsrDefaultGridStyleTest.class, SsrMapNodeTest.class, SsrListNodeTest.class,
        UISsrFormTest.class, SsrUtilsTest.class, SsrDefaultFormStyleTest.class })

public class SsrAllTest {

}
