
package cn.cerc.ui.ssr.form;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISupplierBlock;

public class SsrFormStyleDefault implements SsrFormStyleImpl {

    private List<String> items = new ArrayList<>();

    private ImageConfigImpl imageConfig;

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    @Override
    public ISupplierBlock getSubmitButton() {
        return new FormSubmitButton();
    }

    @Override
    public FormStringField getString(String title, String field) {
        return new FormStringField(title, field);
    }

    @Override
    public FormBooleanField getBoolean(String title, String field) {
        items.add(title);
        return new FormBooleanField(title, field);
    }

    @Override
    public FormMapField getMap(String title, String field) {
        items.add(title);
        return new FormMapField(title, field);
    }

    @Override
    public FormCodeNameField getCodeName(String title, String field, String... dialogFunc) {
        items.add(title);
        return new FormCodeNameField(title, field, dialogFunc);
    }

    protected String getDialogText(String field, String... dialogFunc) {
        var sb = new StringBuffer();
        sb.append(dialogFunc[0]);
        sb.append("('").append(field).append("'");
        if (dialogFunc.length > 1) {
            for (var i = 1; i < dialogFunc.length; i++)
                sb.append(",'").append(dialogFunc[i]).append("'");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public FormFastDateField getDate(String title, String field) {
        items.add(title);
        return new FormFastDateField(title, field);
    }

    @Override
    public FormDatetimeField getDatetime(String title, String field) {
        items.add(title);
        return new FormDatetimeField(title, field);
    }

    @Override
    public FormDateRangeField getDateRange(String title, String beginDate, String endDate) {
        items.add(title);
        return new FormDateRangeField(title, beginDate, endDate);
    }

    @Override
    public FormTextareaField getTextarea(String title, String field) {
        items.add(title);
        return new FormTextareaField(title, field);
    }

    @Override
    public List<String> items() {
        return items;
    }

}
