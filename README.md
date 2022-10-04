# summer-view

对应MVC的页面显示层，具体说明和使用方式详见以下模组

借鉴mfc与vcl模型，使用后台渲染生成web html5页面

前端程序员与后端程序员分离，后端程序负责数据处理以及生成html，前端程序员负责js与css。
避免了前端程序员与后端程序员大量的数据接口协调工作，也就节约了开发成本。

对于个别需要有特殊要求的页面，这些页面仍能继续使用jquery、vue、React等处理，兼顾易用性。


#主要组件简介
UIGridView：用于在pc浏览器上显示表格
UIBlockView：用于显示各类块状排版
UIPhoneView：用于在phone浏览器上显示，继承于UIBlockView

#使用范例
```
DataSet dataSet = svr.dataOut();
// 定义要参与显示的栏位内容
var style = new UIDataStyle().setDataSet(svr.dataOut());
style.addField("Name_").setName("部门名称");
style.addField("ParentName").setName("一级部门");
style.addField("Depute_").setName("外发否").dataType().setClass(Boolean.class);
style.addField("Disable_").setName("停用否").dataType().setClass(Boolean.class);

// 定义pc环境下的表格显示（在phone环境将不会工作）
var view1 = new UIGridView(jspPage.getContent()).setDataStyle(style);
// 定义phone环境下的显示（在pc环境将不会工作）
var view2 = new UIPhoneView(jspPage.getContent()).setDataStyle(style);
// 混合显示在一起
view2.addLine().addCell("Name_", "ParentName");
// 分成4栏，按1：2：1：2的宽度比例显示
view2.addGrid(1, 2, 1, 2).addCell("Depute_", "Disable_");
```

#UIDataStyle
UIDataStyle(): 等同于 new UIDataStyle(false)，使用显示样式，不可编辑内容
UIDataStyle(true): 使用编辑样式，各字段会显示输入框, 可编辑内容
addField(): 增加一个字段，返回 FieldDataStyle

#FieldDataStyle： 字段的样式定义数据
onGetText(OnGetText onGetText)：定义在执行getText时的处理器
setName(String fieldName): 设置字段名称，其作用与dataSet.fields(fileCode).setName相同

#UIPhoneView 定义在phone环境下的显示，此组件在非phone环境下不会输出，但可以使用setActive(true)设置为任何环境均会输出
addLine(): 增加一行，并返回UIPhoneLine对象
addGrid(int...displyWidth): 增加一行，并同时定义各列的显示宽度比例，并返回UIPhoneGridLine对象

#UIPhoneGridLine 配合UIPhoneView，控制一行数据的显示，其内容将以表格形式体现，因此可以设置宽度
addCell(String...fileList)：增加多列

#UIBlockLine 配合UIBlockView与UIPhoneView，控制一行数据的显示，其内容将以<span>分开
addCell(String...fileList)：增加多列

#第2代写法：创建搜索面板
```
UISearchPanel search1 = new UISearchPanel(page.getContent());
new StringColumn(search1, "员工工号", "code_").setPlaceholder("请输入员工工号");
new StringColumn(search1, "查询条件", "searchText_").setPlaceholder("请输入查询条件");
search1.readAll();
```
#第3代写法：创建搜索面板
```
var search2 = new UIDataStyle(true).setDataRow(new DataRow());
search2.addField("code_").setName("员工工号").setPlaceholder("请输入员工工号");
search2.addField("searchText_").setName("查询条件").setPlaceholder("请输入查询条件");
if (UIForm.build(page.getContent(), search2).gatherRequest() > 0)
    System.out.println(search2.current());
```