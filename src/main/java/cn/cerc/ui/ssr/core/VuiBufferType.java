package cn.cerc.ui.ssr.core;

import cn.cerc.mis.core.IBufferKey;

public enum VuiBufferType implements IBufferKey {
    VuiForm;

    @Override
    public int getStartingPoint() {
        return 80;
    }

    @Override
    public int getMinimumNumber() {
        return 0;
    }

    @Override
    public int getMaximumNumber() {
        return 10;
    }

}
