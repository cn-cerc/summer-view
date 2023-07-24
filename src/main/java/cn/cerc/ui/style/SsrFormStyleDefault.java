package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SsrFormStyleDefault implements SsrFormStyleImpl {

    private List<String> items = new ArrayList<>();

    @Override
    public Consumer<SsrComponentImpl> getTextBox(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    %s: <input type="text" name="%s" value="${%s}" />
                    """, title, field, field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getCheckBox(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    <input type="checkbox" name="%s" value="${%s}" ${if %s}checked${endif} />%s
                    """, field, field, field, title));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getListBox(String title, String field, List<String> list, String selected) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title, String.format("""
                    <select name="%s">
                    ${list.begin}
                    <option value="${list.item}" ${if list.item==selected}selected${endif}>${list.item}</option>
                    ${list.end}
                    </select>
                    """, field));
            for (var item : list)
                ssr.toList(item);
            ssr.toMap("selected", selected);
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getDate(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    %s: <input type="text" name="%s" value="${%s}" />
                    """, title, field, field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getDatetime(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    %s: <input type="text" name="%s" value="${%s}" />
                    """, title, field, field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getDateRange(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    %s: <input type="text" name="%s" value="${%s}" />
                    """, title, field, field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getRadioButton(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    %s: <input type="text" name="%s" value="${%s}" />
                    """, title, field, field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getTabs(String title, String field) {
        items.add(title);
        return form -> {
            form.addTemplate(title, String.format("""
                    %s: <input type="text" name="%s" value="${%s}" />
                    """, title, field, field));
        };
    }

    @Override
    public List<String> items() {
        return items;
    }

}
