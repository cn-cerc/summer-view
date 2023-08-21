
package cn.cerc.ui.ssr.form;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISupplierBlock;

public class SsrFormStyleDefault {

    private ImageConfigImpl imageConfig;

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    public ISupplierBlock getSubmitButton() {
        return new FormSubmitButton();
    }

    public FormStringField getString(String title, String field) {
        return new FormStringField(title, field);
    }

    public FormBooleanField getBoolean(String title, String field) {
        return new FormBooleanField(title, field);
    }

    @Deprecated
    public FormMapField getMap(String title, String field) {
        return new FormMapField(title, field);
    }

    public FormCodeNameField getCodeName(String title, String field, String... dialogFunc) {
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

    public FormFastDateField getDate(String title, String field) {
        return new FormFastDateField(title, field);
    }

    public FormDatetimeField getDatetime(String title, String field) {
        return new FormDatetimeField(title, field);
    }

    public FormDateRangeField getDateRange(String title, String beginDate, String endDate) {
        return new FormDateRangeField(title, beginDate, endDate);
    }

    public FormTextareaField getTextarea(String title, String field) {
        return new FormTextareaField(title, field);
    }

}
