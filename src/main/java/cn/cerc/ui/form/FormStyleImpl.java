package cn.cerc.ui.form;

import java.util.List;

public interface FormStyleImpl {
    String getHtml(int width);

    int getWidth();

    List<String> getCodes();
}
