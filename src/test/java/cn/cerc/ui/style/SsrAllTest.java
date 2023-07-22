package cn.cerc.ui.style;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ SsrTemplateTest.class, SsrCallbackTest.class, SsrComboTest.class, SsrDataRowTest.class,
        SsrDatasetTest.class, SsrIfNodeTest.class, SsrTextNodeTest.class, UISsrBlockTest.class, SsrDefineTest.class,
        UISsrGridTest.class, SsrDefaultGridStyleTest.class, SsrMapNodeTest.class, SsrListNodeTest.class,
        UISsrFormTest.class, SsrUtilsTest.class, SsrDefaultFormStyleTest.class })

public class SsrAllTest {

}
