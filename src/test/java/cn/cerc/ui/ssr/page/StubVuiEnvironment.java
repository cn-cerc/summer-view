package cn.cerc.ui.ssr.page;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.ssr.base.VuiPanel;
import cn.cerc.ui.ssr.form.FormFastDateField;
import cn.cerc.ui.ssr.form.FormStringField;
import cn.cerc.ui.ssr.form.FormSubmitButton;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.grid.GridBooleanField;
import cn.cerc.ui.ssr.grid.GridCheckBoxField;
import cn.cerc.ui.ssr.grid.GridItField;
import cn.cerc.ui.ssr.grid.GridStringField;
import cn.cerc.ui.ssr.grid.GridUrlField;
import cn.cerc.ui.ssr.grid.VuiGrid;
import cn.cerc.ui.ssr.source.VuiDataRow;
import cn.cerc.ui.ssr.source.VuiDataService;
import cn.cerc.ui.ssr.source.VuiDataSet;
import cn.cerc.ui.ssr.source.VuiMapService;
import cn.cerc.ui.ssr.source.VuiRequestRow;

public class StubVuiEnvironment extends VuiEnvironment {

    @Override
    protected IPage getRuntimePage() {
        return null;
    }

    @Override
    protected IPage getDesignPage() {
        return null;
    }

    @Override
    public String loadProperties() {
        return """
                {
                    "class": "VisualContainer",
                    "id": "FrmDept",
                    "v_width": 50,
                    "v_height": 30,
                    "v_top": 10,
                    "v_left": 10,
                    "container": true,
                    "visual": true,
                    "title": "部门资料维护",
                    "readme": "",
                    "components": [
                        {
                            "class": "UISsrForm",
                            "id": "form1",
                            "v_width": 50,
                            "v_height": 30,
                            "v_top": 10,
                            "v_left": 10,
                            "container": true,
                            "visual": true,
                            "action": "",
                            "dataRow": "dataRow1",
                            "align": "None",
                            "components": [
                                {
                                    "class": "FormStringField",
                                    "id": "查询条件",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "查询条件",
                                    "field": "code_",
                                    "mark": "",
                                    "placeholder": "",
                                    "dialog": "",
                                    "patten": "",
                                    "readonly": false,
                                    "required": false,
                                    "autofocus": false
                                },
                                {
                                    "class": "FormStringField",
                                    "id": "载入笔数",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "载入笔数",
                                    "field": "MaxRecord_",
                                    "mark": "",
                                    "placeholder": "",
                                    "dialog": "",
                                    "patten": "",
                                    "readonly": false,
                                    "required": false,
                                    "autofocus": false
                                },
                                {
                                    "class": "FormStringField",
                                    "id": "field1",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "mark": "",
                                    "placeholder": "",
                                    "dialog": "",
                                    "patten": "",
                                    "readonly": false,
                                    "required": false,
                                    "autofocus": false
                                },
                                {
                                    "class": "FormSubmitButton",
                                    "id": "submitButton1",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "提交",
                                    "field": "submit",
                                    "searchButton": true
                                },
                                {
                                    "class": "FormFastDateField",
                                    "id": "field2",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "日期范围",
                                    "field": "TBDate_",
                                    "placeholder": "",
                                    "patten": "",
                                    "required": false,
                                    "readonly": false
                                },
                                {
                                    "class": "FormStringField",
                                    "id": "field3",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "mark": "",
                                    "placeholder": "",
                                    "dialog": "",
                                    "patten": "",
                                    "readonly": false,
                                    "required": false,
                                    "autofocus": false
                                },
                                {
                                    "class": "FormFastDateField",
                                    "id": "field4",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "placeholder": "",
                                    "patten": "",
                                    "required": false,
                                    "readonly": false
                                },
                                {
                                    "class": "FormFastDateField",
                                    "id": "field5",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "placeholder": "",
                                    "patten": "",
                                    "required": false,
                                    "readonly": false
                                }
                            ]
                        },
                        {
                            "class": "UISsrGrid",
                            "id": "grid1",
                            "v_width": 50,
                            "v_height": 30,
                            "v_top": 10,
                            "v_left": 10,
                            "container": true,
                            "visual": true,
                            "emptyText": "1",
                            "dataSet": "",
                            "components": [
                                {
                                    "class": "GridStringField",
                                    "id": "column1",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridStringField",
                                    "id": "column2",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridStringField",
                                    "id": "column3",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridStringField",
                                    "id": "column4",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridStringField",
                                    "id": "column5",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridStringField",
                                    "id": "column6",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridItField",
                                    "id": "column7",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "序",
                                    "field": "",
                                    "fieldWidth": 10
                                },
                                {
                                    "class": "GridItField",
                                    "id": "column8",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "序",
                                    "field": "",
                                    "fieldWidth": 10
                                }
                            ]
                        },
                        {
                            "class": "SsrDataRow",
                            "id": "dataRow1",
                            "v_top": 10,
                            "v_left": 10,
                            "container": false,
                            "visual": false,
                            "data": {
                                "code_": "*",
                                "name_": "",
                                "MaxRecord_": "100",
                                "line_2": "",
                                "Address_": "",
                                "TBDate_": ""
                            }
                        },
                        {
                            "class": "SsrDataService",
                            "id": "service1",
                            "v_top": 10,
                            "v_left": 10,
                            "container": false,
                            "visual": false,
                            "service": "SvrIndustry.search",
                            "success_message": "",
                            "headIn": "",
                            "callByInit": false
                        },
                        {
                            "class": "SsrMapSource",
                            "id": "mapSource1",
                            "v_top": 10,
                            "v_left": 10,
                            "container": false,
                            "visual": false,
                            "addAll": false,
                            "service": "",
                            "remoteService": false,
                            "key": "",
                            "value": ""
                        },
                        {
                            "class": "UISsrForm",
                            "id": "form2",
                            "v_width": 50,
                            "v_height": 30,
                            "v_top": 10,
                            "v_left": 10,
                            "container": true,
                            "visual": true,
                            "action": "",
                            "dataRow": "",
                            "align": "None"
                        },
                        {
                            "class": "UISsrGrid",
                            "id": "grid2",
                            "v_width": 50,
                            "v_height": 30,
                            "v_top": 10,
                            "v_left": 10,
                            "container": true,
                            "visual": true,
                            "emptyText": "",
                            "dataSet": "",
                            "components": [
                                {
                                    "class": "GridCheckBoxField",
                                    "id": "column9",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 5
                                },
                                {
                                    "class": "GridBooleanField",
                                    "id": "column10",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "trueText": "是",
                                    "falseText": "否",
                                    "fieldWidth": 5
                                },
                                {
                                    "class": "GridStringField",
                                    "id": "column11",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "",
                                    "field": "",
                                    "fieldWidth": 10,
                                    "align": ""
                                },
                                {
                                    "class": "GridUrlField",
                                    "id": "GridUrlField1",
                                    "v_top": 10,
                                    "v_left": 10,
                                    "container": false,
                                    "visual": false,
                                    "title": "操作",
                                    "context": "内容",
                                    "field": "opear",
                                    "fieldWidth": 5,
                                    "href": ""
                                }
                            ]
                        },
                        {
                            "class": "SsrRequestRow",
                            "id": "dataRow2",
                            "v_top": 10,
                            "v_left": 10,
                            "container": false,
                            "visual": false,
                            "data": {}
                        },
                        {
                            "class": "SsrDataSet",
                            "id": "dataSet1",
                            "v_top": 10,
                            "v_left": 10,
                            "container": false,
                            "visual": false
                        },
                        {
                            "class": "SsrDataRow",
                            "id": "dataRow3",
                            "v_top": 10,
                            "v_left": 10,
                            "container": false,
                            "visual": false,
                            "data": {}
                        },
                        {
                            "class": "SsrPanel",
                            "id": "panel1",
                            "v_width": 50,
                            "v_height": 30,
                            "v_top": 10,
                            "v_left": 10,
                            "container": true,
                            "visual": true,
                            "v_type": "div",
                            "v_role": "",
                            "v_class": "",
                            "align": "None"
                        },
                        {
                            "class": "UISsrForm",
                            "id": "form3",
                            "v_width": 50,
                            "v_height": 30,
                            "v_top": 10,
                            "v_left": 10,
                            "container": true,
                            "visual": true,
                            "action": "",
                            "dataRow": "",
                            "align": "None"
                        }
                    ]
                }
                """;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getBean(String beanId, Class<T> requiredType) {
        Object obj = null;
        if ("UISsrForm".equals(beanId))
            obj = new VuiForm();
        if ("UISsrGrid".equals(beanId))
            obj = new VuiGrid();
        if ("formStringField".equals(beanId))
            obj = new FormStringField();
        if ("formSubmitButton".equals(beanId))
            obj = new FormSubmitButton();
        if ("formFastDateField".equals(beanId))
            obj = new FormFastDateField();
        if ("vuiDataRow".equals(beanId))
            obj = new VuiDataRow();
        if ("vuiDataSet".equals(beanId))
            obj = new VuiDataSet();
        if ("gridStringField".equals(beanId))
            obj = new GridStringField();
        if ("gridItField".equals(beanId))
            obj = new GridItField();
        if ("vuiDataService".equals(beanId))
            obj = new VuiDataService();
        if ("vuiMapService".equals(beanId))
            obj = new VuiMapService();
        if ("gridCheckBoxField".equals(beanId))
            obj = new GridCheckBoxField();
        if ("gridBooleanField".equals(beanId))
            obj = new GridBooleanField();
        if ("gridUrlField".equals(beanId))
            obj = new GridUrlField();
        if ("vuiRequestRow".equals(beanId))
            obj = new VuiRequestRow();
        if ("vuiPanel".equals(beanId))
            obj = new VuiPanel();
        return (Optional<T>) Optional.ofNullable(obj);
    }

    @Test
    public void test() {
        StubVuiEnvironment env = new StubVuiEnvironment();
        var canvas = new VuiCanvas(env);
        canvas.ready();
        assertEquals(32, canvas.getMembers().size());
        //
        var html = new HtmlWriter();
        canvas.output(html);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><div> <span onclick="toggleSearch(this)">查询条件</span> <div class="searchFormButtonDiv">
                        <button name="submit" value="submit">提交</button> </div>
                        </div><ul><li>
                        <label for="code_"><em>查询条件</em></label>
                        <div>
                        <input type="text" name="code_" id="code_" value="*" autocomplete="off" placeholder="请输入查询条件"/>
                        <span role="suffix-icon"></span>
                        </div>
                        </li><li>
                        <label for="MaxRecord_"><em>载入笔数</em></label>
                        <div>
                        <input type="text" name="MaxRecord_" id="MaxRecord_" value="100" autocomplete="off" placeholder="请输入载入笔数"/>
                        <span role="suffix-icon"></span>
                        </div>
                        </li><li>
                        <label for="TBDate_"><em>日期范围</em></label>
                        <div>
                        <input type="text" name="TBDate_" id="TBDate_" value="" autocomplete="off" placeholder="请点击获取日期范围"/>
                        <span role="suffix-icon"><a href="javascript:showDateDialog('TBDate_')">
                        <img src="null" />
                        </a></span>
                        </div>
                        </li></ul></form><div>dataSet is null</div><div>dataRow is null</div><div>dataSet is null</div><div></div><div>dataRow is null</div>
                                                                                """
                        .trim(),
                html.toString());
    }

}
