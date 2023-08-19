package cn.cerc.ui.ssr.page;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.ssr.form.UISsrForm;
import cn.cerc.ui.ssr.grid.UISsrGrid;
import cn.cerc.ui.ssr.source.SsrDataService;

//@Configuration
//@ComponentScan(basePackages = { "cn.cerc.*" })
public class VisualContainerTest {
    public static final String Text = """
                        {
                "readme": null,
                "title": null,
                "class": "VisualContainer",
                "components": [
                    {
                        "action": "FrmDept",
                        "class": "UISsrForm",
                        "id": "form1",
                        "components": [
                            {
                                "class": "FormStringField",
                                "title": "查询条件",
                                "field": "code_",
                                "mark": "",
                                "placeholder": "",
                                "dialog": "",
                                "patten": "",
                                "readonly": "false",
                                "required": "false",
                                "autofocus": "false"
                            },
                            {
                                "class": "FormStringField",
                                "title": "载入笔数",
                                "field": "MaxRecord_",
                                "mark": "",
                                "placeholder": "",
                                "dialog": "",
                                "patten": "",
                                "readonly": "false",
                                "required": "false",
                                "autofocus": "false"
                            }
                        ]
                    },
                    {
                        "class": "SsrDataSource",
                        "id": "dataSource1"
                    },
                    {
                        "class": "UISsrGrid",
                        "id": "grid1",
                        "components": [
                            {
                                "class": "GridStringField",
                                "title": "代码",
                                "field": "code_",
                                "fieldWidth": "4",
                                "align": "left"
                            },
                            {
                                "class": "GridStringField",
                                "title": "姓名",
                                "field": "name_",
                                "fieldWidth": "4",
                                "align": "left"
                            }
                        ]
                    }
                ]
            }

            """.trim();

    @Before
    public void setup() {
        Application.init(VisualContainerTest.class);
    }

    @Test
    public void test_writer() {
        var vc = new VisualContainer(null);
        vc.setId("FrmDept");

        var form1 = new UISsrForm(vc);
        form1.setId("form1");
        form1.action("FrmDept");
        var style = form1.defaultStyle();
        form1.addBlock(style.getString("查询条件", "code_"));
        form1.addBlock(style.getString("载入笔数", "MaxRecord_"));

        var dataSource1 = new SsrDataService();
        dataSource1.setOwner(vc);
        dataSource1.setId("dataSource1");
        dataSource1.service("SvrDept.download");
        dataSource1.headIn().targetId(form1.getId());

        var grid1 = new UISsrGrid(vc);
        grid1.setId("grid1");
        grid1.dataSourceBindId(dataSource1.getId());
        var style2 = grid1.defaultStyle();
        grid1.addBlock(style2.getString("代码", "code_", 4));
        grid1.addBlock(style2.getString("姓名", "name_", 4));

        assertEquals(
                """
                        {"title":null,"readme":null,"class":"VisualContainer","id":"FrmDept","components":[{"action":"FrmDept","dataRow":"","class":"UISsrForm","id":"form1","components":[{"class":"FormStringField","id":"查询条件","title":"查询条件","field":"code_","mark":"","placeholder":"","dialog":"","patten":"","readonly":"false","required":"false","autofocus":"false"},{"class":"FormStringField","id":"载入笔数","title":"载入笔数","field":"MaxRecord_","mark":"","placeholder":"","dialog":"","patten":"","readonly":"false","required":"false","autofocus":"false"}]},{"class":"SsrDataService","id":"dataSource1","bind":"form1","service":"SvrDept.download"},{"class":"UISsrGrid","id":"grid1","components":[{"class":"GridStringField","title":"代码","field":"code_","fieldWidth":"4","align":"left"},{"class":"GridStringField","title":"姓名","field":"name_","fieldWidth":"4","align":"left"}],"dataSet":"dataSource1"}]}                        """
                        .trim(),
                vc.getProperties());
    }
//
//    @Bean
//    public ISystemTable getIystemTable() {
//        return null;
//    }
//
//    @Bean
//    public HttpServletRequest getHttpServletRequest() {
//        return null;
//    }
//
//    @Bean
//    public ImageConfigImpl getImageConfigImpl() {
//        return null;
//    }

    @Test
    public void test_reader() {
        var vc = new VisualContainer(null);
        vc.setProperties(Text);
        assertEquals(
                """
                        {"readme":"null","title":"null","editor":null,"class":"VisualContainer","components":[{"action":"","class":"UISsrForm","id":"form1","components":[{"class":"FormStringField","title":"查询条件","field":"code_","mark":"","placeholder":"","dialog":"","patten":"","readonly":"false","required":"false","autofocus":"false"},{"class":"FormStringField","title":"载入笔数","field":"MaxRecord_","mark":"","placeholder":"","dialog":"","patten":"","readonly":"false","required":"false","autofocus":"false"}]},{"class":"SsrDataSource","id":"dataSource1"},{"class":"UISsrGrid","id":"grid1","components":[{"class":"GridStringField","title":"代码","field":"code_","fieldWidth":"4","align":"left"},{"class":"GridStringField","title":"姓名","field":"name_","fieldWidth":"4","align":"left"}]}]}
                        """
                        .trim(),
                vc.getProperties());
    }
}
