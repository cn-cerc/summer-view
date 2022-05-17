package cn.cerc.ui.core;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;

@Component
public class RequestReader {

    private HttpServletRequest request;

    RequestReader(HttpServletRequest request) {
        this.request = request;
    }

    public boolean hasValue(INameOwner owner) {
        String value = request.getParameter(owner.getName());
        return !Utils.isEmpty(value);
    }

    public String getString(INameOwner owner, String defaultValue) {
        String result = request.getParameter(owner.getName());
        return result != null ? result : defaultValue;
    }

    public int getInt(INameOwner owner, int defaultValue) {
        return Integer.parseInt(getString(owner, String.valueOf(defaultValue)));
    }

}
