package cn.cerc.ui.ssr.source;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class VuiRequestDataSetTest extends VuiRequestDataSet {

    // 模拟 Request 请求参数
    HashMap<String, String[]> request = new HashMap<>();

    public VuiRequestDataSetTest() {
        request.put("value", new String[] { "A1,B1,C1", "A2,B2,C2", "A3,B3,C3" });
        request.put("value2", new String[] { "D1", "D2", "D3" });
        // 配置内容
        config(DataRow.of("value", "field1,field2,field3", "value2", "field4"));
    }

    @Test
    public void test() {
        initDataSet();
        String result = """
                {"body":[["field1","field2","field3","field4"],["A1","B1","C1","D1"],["A2","B2","C2","D2"],["A3","B3","C3","D3"]]}""";
        assertEquals(result, dataSet().json());
    }

    @Override
    protected String[] getValues(String name) {
        return request.get(name);
    }

}
