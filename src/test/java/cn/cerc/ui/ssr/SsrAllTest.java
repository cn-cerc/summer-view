package cn.cerc.ui.ssr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cn.cerc.ui.ssr.core.SsrBlockTest;
import cn.cerc.ui.ssr.core.SsrCallbackTest;
import cn.cerc.ui.ssr.core.SsrComboTest;
import cn.cerc.ui.ssr.core.SsrDataRowTest;
import cn.cerc.ui.ssr.core.SsrDatasetTest;
import cn.cerc.ui.ssr.core.SsrDefaultFormStyleTest;
import cn.cerc.ui.ssr.core.SsrDefaultGridStyleTest;
import cn.cerc.ui.ssr.core.SsrFormStyleDefaultTest;
import cn.cerc.ui.ssr.core.SsrIfNodeTest;
import cn.cerc.ui.ssr.core.SsrListNodeTest;
import cn.cerc.ui.ssr.core.SsrMapNodeTest;
import cn.cerc.ui.ssr.core.SsrTemplateTest;
import cn.cerc.ui.ssr.core.SsrTextNodeTest;
import cn.cerc.ui.ssr.core.SsrUtilsTest;
import cn.cerc.ui.ssr.form.VuiFormTest;
import cn.cerc.ui.ssr.grid.VuiGridTest;

@RunWith(Suite.class)

@SuiteClasses({ SsrBlockTest.class, SsrCallbackTest.class, SsrComboTest.class, SsrDataRowTest.class,
        SsrDatasetTest.class, SsrIfNodeTest.class, SsrTextNodeTest.class, UISsrBlockTest.class, SsrTemplateTest.class,
        VuiGridTest.class, SsrDefaultGridStyleTest.class, SsrMapNodeTest.class, SsrListNodeTest.class,
        VuiFormTest.class, SsrUtilsTest.class, SsrDefaultFormStyleTest.class, UISsrChunkTest.class,
        SsrFormStyleDefaultTest.class })

public class SsrAllTest {

}
