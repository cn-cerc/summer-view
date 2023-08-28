package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.block.SsrBlockStyleDefault;
import cn.cerc.ui.ssr.block.UISsrBlock1101;
import cn.cerc.ui.ssr.block.UISsrBlock1201;
import cn.cerc.ui.ssr.block.UISsrBlock2101;
import cn.cerc.ui.ssr.block.UISsrBlock2201;
import cn.cerc.ui.ssr.block.UISsrBlock3101;
import cn.cerc.ui.ssr.block.UISsrBlock310101;
import cn.cerc.ui.ssr.block.UISsrBlock3201;
import cn.cerc.ui.ssr.block.UISsrChunk;

public class UISsrChunkTest {

    @Test
    public void test_cpu101() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock2101 block = new UISsrBlock2101(chunk);
        block.slot0(style.getString("姓名", "name_").url(() -> "www?name=" + ds.getString("name_")));
        block.slot1(style.getOpera("name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=zhangsan'>zhangsan</a></div> <div role='opera'>
                        <a href='zhangsan'>内容</a>
                        </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=lisi'>lisi</a></div> <div role='opera'>
                        <a href='lisi'>内容</a>
                        </div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu102() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock1101 block = new UISsrBlock1101(chunk);
        block.slot0(style.getString("姓名", "name_").url(() -> "www?name=" + ds.getString("name_")));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock1101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=zhangsan'>zhangsan</a></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock1101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=lisi'>lisi</a></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu103() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock310101 block = new UISsrBlock310101(chunk);
        block.slot0(style.getIt());
        block.slot1(style.getString("姓名", "name_"));
        block.slot2(style.getOpera(() -> ds.getString("name_")));
        chunk.dataSet(ds);
        assertEquals("""
                <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock310101'> <div role='gridIt'>
                <span>1</span>
                </div> <div style='flex: 1;'>
                <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div role='opera'>
                <a href='zhangsan'>内容</a>
                </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock310101'> <div role='gridIt'>
                <span>2</span>
                </div> <div style='flex: 1;'>
                <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div role='opera'>
                <a href='lisi'>内容</a>
                </div></li></ul></div>""", chunk.toString());
    }

    @Test
    public void test_cpu104() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", 18).setValue("weight_", 160);
        ds.append().setValue("name_", "lisi").setValue("age_", 16).setValue("weight_", 150);

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock3201 block = new UISsrBlock3201(chunk);
        block.slot0(style.getString("姓名", "name_"));
        block.slot1(style.getString("年龄", "age_"));
        block.slot2(style.getString("体重", "weight_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock3201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div> <div style='flex: 1;'>
                        <label for='weight_'>体重</label> <span id='weight_'>160</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>16</span></div> <div style='flex: 1;'>
                        <label for='weight_'>体重</label> <span id='weight_'>150</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu105() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("opera_", "内容");
        ds.append().setValue("name_", "lisi").setValue("opera_", "内容");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock310101 board = new UISsrBlock310101(chunk);
        board.slot0(style.getCheckboxIt("checkbox_name_", "name_"));
        board.slot1(style.getString("姓名", "name_").url(() -> "www"));
        board.slot2(style.getString("内容", "opera_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock310101'> <div role='checkboxIt'>
                        <input type="checkbox" id="checkbox_name_" name="checkbox_name_" value="zhangsan"/>
                        <span>1</span>
                        </div> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>zhangsan</a></div> <div style='flex: 1;'>
                        <label for='opera_'>内容</label> <span id='opera_'>内容</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock310101'> <div role='checkboxIt'>
                        <input type="checkbox" id="checkbox_name_" name="checkbox_name_" value="lisi"/>
                        <span>2</span>
                        </div> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>lisi</a></div> <div style='flex: 1;'>
                        <label for='opera_'>内容</label> <span id='opera_'>内容</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu106() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock2101 board = new UISsrBlock2101(chunk);
        board.slot0(style.getCheckbox("checkBoxName", "name_"));
        board.slot1(style.getString("姓名", "name_").url(() -> "www"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div role='checkbox'>
                        <input type="checkbox" name="checkBoxName" value="zhangsan"/>
                        </div> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>zhangsan</a></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div role='checkbox'>
                        <input type="checkbox" name="checkBoxName" value="lisi"/>
                        </div> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>lisi</a></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu107() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock310101 board = new UISsrBlock310101(chunk);
        board.slot0(style.getCheckbox("checkBoxName", "name_"));
        board.slot1(style.getString("姓名", "name_"));
        board.slot2(style.getOpera(() -> "www"));
        chunk.dataSet(ds);
        assertEquals("""
                <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock310101'> <div role='checkbox'>
                <input type="checkbox" name="checkBoxName" value="zhangsan"/>
                </div> <div style='flex: 1;'>
                <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div role='opera'>
                <a href='www'>内容</a>
                </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock310101'> <div role='checkbox'>
                <input type="checkbox" name="checkBoxName" value="lisi"/>
                </div> <div style='flex: 1;'>
                <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div role='opera'>
                <a href='www'>内容</a>
                </div></li></ul></div>""", chunk.toString());
    }

    @Test
    public void test_cpu108() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock2101 board = new UISsrBlock2101(chunk);
        board.slot0(style.getCheckboxIt("checkBoxName", "name_"));
        board.slot1(style.getString("姓名", "name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div role='checkboxIt'>
                        <input type="checkbox" id="checkBoxName" name="checkBoxName" value="zhangsan"/>
                        <span>1</span>
                        </div> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div role='checkboxIt'>
                        <input type="checkbox" id="checkBoxName" name="checkBoxName" value="lisi"/>
                        <span>2</span>
                        </div> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu109() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock1201 board = new UISsrBlock1201(chunk);
        board.slot0(style.getString("姓名", "name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock1201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock1201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu110() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15");
        ds.append().setValue("name_", "lisi").setValue("age_", "18");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock2201 board = new UISsrBlock2201(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock2201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>15</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock2201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu111() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "男");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "女");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock3201 board = new UISsrBlock3201(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getString("性别", "sex_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock3201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>15</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>男</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3201'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>女</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu112() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15");
        ds.append().setValue("name_", "lisi").setValue("age_", "18");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock2101 board = new UISsrBlock2101(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>15</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock2101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu113() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "男");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "女");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock3101 board = new UISsrBlock3101(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getString("性别", "sex_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>15</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>男</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>女</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu114() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "1");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "0");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock3101 board = new UISsrBlock3101(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getNumber("性别", "sex_").toList(List.of("女", "男")));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>15</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>男</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 1;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>女</span></div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu115() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("adult_", "false");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("adult_", "true");
        ds.append().setValue("name_", "wangwu").setValue("age_", "0");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock3101 board = new UISsrBlock3101(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getBoolean("是否成年", "adult_"));
        board.ratio(2, 1, 1);
        chunk.dataSet(ds);
        assertEquals("""
                <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 2;'>
                <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                <label for='age_'>年龄</label> <span id='age_'>15</span></div> <div style='flex: 1;'>
                <label for='adult_'>是否成年</label>
                <span id='adult_'>否</span>
                </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 2;'>
                <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                <label for='age_'>年龄</label> <span id='age_'>18</span></div> <div style='flex: 1;'>
                <label for='adult_'>是否成年</label>
                <span id='adult_'>是</span>
                </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 2;'>
                <label for='name_'>姓名</label> <span id='name_'>wangwu</span></div> <div style='flex: 1;'>
                <label for='age_'>年龄</label> <span id='age_'>0</span></div> <div style='flex: 1;'>
                <label for='adult_'>是否成年</label>
                <span id='adult_'>否</span>
                </div></li></ul></div>""", chunk.toString());
    }

    @Test
    public void test_cpu116() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "0");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "1");
        ds.append().setValue("name_", "wangwu").setValue("age_", "0").setValue("sex_", "0");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.defaultStyle();

        UISsrBlock3101 board = new UISsrBlock3101(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getNumber("性别", "sex_").toList(List.of("男", "女")));
        board.ratio(2, 1, 1);
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 2;'>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>15</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>男</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 2;'>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>18</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>女</span></div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock3101'> <div style='flex: 2;'>
                        <label for='name_'>姓名</label> <span id='name_'>wangwu</span></div> <div style='flex: 1;'>
                        <label for='age_'>年龄</label> <span id='age_'>0</span></div> <div style='flex: 1;'>
                        <label for='sex_'>性别</label> <span id='sex_'>男</span></div></li></ul></div>""",
                chunk.toString());
    }
}
