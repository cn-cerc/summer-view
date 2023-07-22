package cn.cerc.ui.style;

import java.util.List;
import java.util.function.Consumer;

public interface SsrFormStyleImpl {

    Consumer<SsrComponentImpl> getTextBox(String title, String field);

    Consumer<SsrComponentImpl> getCheckBox(String title, String field);

    Consumer<SsrComponentImpl> getDate(String title, String field);

    Consumer<SsrComponentImpl> getDatetime(String title, String field);

    Consumer<SsrComponentImpl> getDateRange(String title, String field);

    Consumer<SsrComponentImpl> getRadioButton(String title, String field);

    Consumer<SsrComponentImpl> getTabButton(String title, String field);

    Consumer<SsrComponentImpl> getListBox(String title, String field, List<String> list, String selected);

}
