package cn.cerc.ui.style;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ SsrTemplateTest.class, SsrCallbackTest.class, SsrComboTest.class, SsrDataRowTest.class,
        SsrDatasetTest.class, SsrIfNodeTest.class, SsrTextNodeTest.class, UITemplateBlockTest.class })

public class SsrAllTest {

}
