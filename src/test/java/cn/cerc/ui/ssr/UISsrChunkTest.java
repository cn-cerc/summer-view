package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.block.UISsrBlock101;
import cn.cerc.ui.ssr.block.UISsrBlock102;
import cn.cerc.ui.ssr.block.UISsrBlock103;
import cn.cerc.ui.ssr.block.UISsrBlock104;

public class UISsrChunkTest {

    @Test
    public void test_cpu101() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock101 uiSsrBlock101 = new UISsrBlock101(chunk);
        uiSsrBlock101.slot0(style.getString("姓名", "name_").url(() -> "www?name=" + ds.getString("name_")));
        uiSsrBlock101.slot1(style.getOpera("name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <span role="gridIt">1</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=zhangsan'>zhangsan</a> </div> <div role='opera'>
                        <a href='zhangsan'>内容</a>
                        </div></li></ul><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <span role="gridIt">2</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=lisi'>lisi</a> </div> <div role='opera'>
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
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock101 uiSsrBlock101 = new UISsrBlock101(chunk);
        uiSsrBlock101.slot0(style.getString("姓名", "name_").url(() -> "www?name=" + ds.getString("name_")));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <span role="gridIt">1</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=zhangsan'>zhangsan</a> </div> </li></ul><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <span role="gridIt">2</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www?name=lisi'>lisi</a> </div> </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu103() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock101 uiSsrBlock101 = new UISsrBlock101(chunk);
        uiSsrBlock101.slot0(style.getString("姓名", "name_"));
        uiSsrBlock101.slot1(style.getOpera(() -> ds.getString("name_")));
        chunk.dataSet(ds);
        assertEquals("""
                <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                <div>
                <span role="gridIt">1</span>
                </div> <div>
                <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div> <div role='opera'>
                <a href='zhangsan'>内容</a>
                </div></li></ul><ul role='chunkBoxItem'><li role='title'>
                <div>
                <span role="gridIt">2</span>
                </div> <div>
                <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div> <div role='opera'>
                <a href='lisi'>内容</a>
                </div></li></ul></div>""", chunk.toString());
    }

    @Test
    public void test_cpu104() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock101 uiSsrBlock101 = new UISsrBlock101(chunk);
        uiSsrBlock101.slot0(style.getString("姓名", "name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <span role="gridIt">1</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div> </li></ul><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <span role="gridIt">2</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div> </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu105() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("opera_", "内容");
        ds.append().setValue("name_", "lisi").setValue("opera_", "内容");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock102 board = new UISsrBlock102(chunk, "checkbox_name_", "name_");
        board.slot0(style.getString("姓名", "name_").url(() -> "www"));
        board.slot1(style.getString("内容", "opera_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <input type="checkbox" name="checkbox_name_" value="zhangsan"/>
                        <span role="gridIt">1</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>zhangsan</a> </div> <div>
                        <label for='opera_'>内容</label> <span id='opera_'>内容</span> </div></li></ul><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <input type="checkbox" name="checkbox_name_" value="lisi"/>
                        <span role="gridIt">2</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>lisi</a> </div> <div>
                        <label for='opera_'>内容</label> <span id='opera_'>内容</span> </div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu106() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock102 board = new UISsrBlock102(chunk, "checkBoxName", "name_");
        board.slot0(style.getString("姓名", "name_").url(() -> "www"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <input type="checkbox" name="checkBoxName" value="zhangsan"/>
                        <span role="gridIt">1</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>zhangsan</a> </div> </li></ul><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <input type="checkbox" name="checkBoxName" value="lisi"/>
                        <span role="gridIt">2</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <a id='name_' href='www'>lisi</a> </div> </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu107() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock102 board = new UISsrBlock102(chunk, "checkBoxName", "name_");
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getOpera(() -> "www"));
        chunk.dataSet(ds);
        assertEquals("""
                <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                <div>
                <input type="checkbox" name="checkBoxName" value="zhangsan"/>
                <span role="gridIt">1</span>
                </div> <div>
                <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div> <div role='opera'>
                <a href='www'>内容</a>
                </div></li></ul><ul role='chunkBoxItem'><li role='title'>
                <div>
                <input type="checkbox" name="checkBoxName" value="lisi"/>
                <span role="gridIt">2</span>
                </div> <div>
                <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div> <div role='opera'>
                <a href='www'>内容</a>
                </div></li></ul></div>""", chunk.toString());
    }

    @Test
    public void test_cpu108() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock102 board = new UISsrBlock102(chunk, "checkBoxName", "name_");
        board.slot0(style.getString("姓名", "name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <input type="checkbox" name="checkBoxName" value="zhangsan"/>
                        <span role="gridIt">1</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div> </li></ul><ul role='chunkBoxItem'><li role='title'>
                        <div>
                        <input type="checkbox" name="checkBoxName" value="lisi"/>
                        <span role="gridIt">2</span>
                        </div> <div>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div> </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu109() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan");
        ds.append().setValue("name_", "lisi");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock103 board = new UISsrBlock103(chunk);
        board.slot0(style.getString("姓名", "name_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock103'> <div>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div>  </li></ul><ul role='chunkBoxItem'><li role='UISsrBlock103'> <div>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div>  </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu110() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15");
        ds.append().setValue("name_", "lisi").setValue("age_", "18");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock103 board = new UISsrBlock103(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock103'> <div>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div> <div>
                        <label for='age_'>年龄</label> <span id='age_'>15</span> </div> </li></ul><ul role='chunkBoxItem'><li role='UISsrBlock103'> <div>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div> <div>
                        <label for='age_'>年龄</label> <span id='age_'>18</span> </div> </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu111() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "男");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "女");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock103 board = new UISsrBlock103(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getString("性别", "sex_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock103'> <div>
                        <label for='name_'>姓名</label> <span id='name_'>zhangsan</span> </div> <div>
                        <label for='age_'>年龄</label> <span id='age_'>15</span> </div> <div>
                        <label for='sex_'>性别</label> <span id='sex_'>男</span> </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock103'> <div>
                        <label for='name_'>姓名</label> <span id='name_'>lisi</span> </div> <div>
                        <label for='age_'>年龄</label> <span id='age_'>18</span> </div> <div>
                        <label for='sex_'>性别</label> <span id='sex_'>女</span> </div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu112() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15");
        ds.append().setValue("name_", "lisi").setValue("age_", "18");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock104 board = new UISsrBlock104(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock104'> <div>
                        <label for='name_'>姓名：</label> <span id='name_'>zhangsan</span> </div> <div>
                        <label for='age_'>年龄：</label> <span id='age_'>15</span> </div> </li></ul><ul role='chunkBoxItem'><li role='UISsrBlock104'> <div>
                        <label for='name_'>姓名：</label> <span id='name_'>lisi</span> </div> <div>
                        <label for='age_'>年龄：</label> <span id='age_'>18</span> </div> </li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu113() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "男");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "女");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock104 board = new UISsrBlock104(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getString("性别", "sex_"));
        chunk.dataSet(ds);
        assertEquals(
                """
                        <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock104'> <div>
                        <label for='name_'>姓名：</label> <span id='name_'>zhangsan</span> </div> <div>
                        <label for='age_'>年龄：</label> <span id='age_'>15</span> </div> <div>
                        <label for='sex_'>性别：</label> <span id='sex_'>男</span> </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock104'> <div>
                        <label for='name_'>姓名：</label> <span id='name_'>lisi</span> </div> <div>
                        <label for='age_'>年龄：</label> <span id='age_'>18</span> </div> <div>
                        <label for='sex_'>性别：</label> <span id='sex_'>女</span> </div></li></ul></div>""",
                chunk.toString());
    }

    @Test
    public void test_cpu114() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "zhangsan").setValue("age_", "15").setValue("sex_", "1");
        ds.append().setValue("name_", "lisi").setValue("age_", "18").setValue("sex_", "0");

        UISsrChunk chunk = new UISsrChunk(null);
        SsrBlockStyleDefault style = chunk.createDefaultStyle();

        UISsrBlock104 board = new UISsrBlock104(chunk);
        board.slot0(style.getString("姓名", "name_"));
        board.slot1(style.getString("年龄", "age_"));
        board.slot2(style.getOption("性别", "sex_", Map.of("0", "女", "1", "男")));
        chunk.dataSet(ds);
        assertEquals("""
                <div role='chunkBox'><ul role='chunkBoxItem'><li role='UISsrBlock104'> <div>
                <label for='name_'>姓名：</label> <span id='name_'>zhangsan</span> </div> <div>
                <label for='age_'>年龄：</label> <span id='age_'>15</span> </div> <div>
                <label for='sex_'>性别</label>
                <span id='sex_'>男</span>
                </div></li></ul><ul role='chunkBoxItem'><li role='UISsrBlock104'> <div>
                <label for='name_'>姓名：</label> <span id='name_'>lisi</span> </div> <div>
                <label for='age_'>年龄：</label> <span id='age_'>18</span> </div> <div>
                <label for='sex_'>性别</label>
                <span id='sex_'>女</span>
                </div></li></ul></div>""", chunk.toString());
    }
}
