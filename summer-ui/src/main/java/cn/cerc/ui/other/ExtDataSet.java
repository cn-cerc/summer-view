package cn.cerc.ui.other;

import cn.cerc.core.DataSet;
import cn.cerc.core.DataSetGson;
import cn.cerc.core.Utils;

public class ExtDataSet extends DataSet {
    private static final long serialVersionUID = -8061177880589068767L;

    public ExtDataSet(String json) {
        super();
        fromJson(json);
    }

    @Override
    public String toJson() {
        return new DataSetGson<ExtDataSet>(this).encode();
    }

    @Override
    public ExtDataSet fromJson(String json) {
        this.close();
        if (!Utils.isEmpty(json))
            new DataSetGson<ExtDataSet>(this).decode(json);
        return this;
    }

}
