package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface SsrGridStyleImpl {

    Consumer<SsrComponentImpl> getIt(String title, int fieldWidth);

    Consumer<SsrComponentImpl> getOpera(String title, String field, int fieldWidth, String url);

    Consumer<SsrComponentImpl> getString(String title, String field, int fieldWidth);

    Consumer<SsrComponentImpl> getBoolean(String title, String field, int fieldWidth, String labelText);

    Consumer<SsrComponentImpl> getOption(String title, String field, int fieldWidth, Map<String, String> map);

    List<String> items();
}
