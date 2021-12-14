package cn.cerc.ui.other;

import cn.cerc.core.DataSet;
import cn.cerc.core.DataSetGson;
import cn.cerc.core.Utils;

@Deprecated
public class ExtDataSet extends DataSet {
    private static final long serialVersionUID = -8061177880589068767L;

    public ExtDataSet(String json) {
        super();
        setJson(json);
    }

    @Override
    public String json() {
        return new DataSetGson<>(this).encode();
    }

    @Override
    public ExtDataSet setJson(String json) {
        super.clear();
        if (!Utils.isEmpty(json))
            new DataSetGson<>(this).decode(json);
        return this;
    }

}
