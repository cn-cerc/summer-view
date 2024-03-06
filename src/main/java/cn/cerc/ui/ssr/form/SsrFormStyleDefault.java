
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

    public FormSearchTextButton getSearchTextButton() {
        return new FormSearchTextButton();
    }

    public FormStringField getString(String title, String field) {
        return new FormStringField(title, field);
    }

    public FormNumberField getNumber(String title, String field) {
        return new FormNumberField(title, field);
    }

    public FormBooleanField getBoolean(String title, String field) {
        return new FormBooleanField(title, field);
    }

    /**
     * 如果是value值为enum的下标的枚举请改为getNumber+toList方法，toList需要跟在getNumber之后
     * addBlock(style.getNumber('title', 'field').toList(enum.values()))
     * 如果value为非enum的下标的枚举请改为getString+toMap方法，toMap需要跟在getString之后
     * addBlock(style.getString('title', 'field').toMap(Map.of('', '')))
     */
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

    public FormDatetimeField getDate(String title, String field) {
        return new FormDatetimeField(title, field).setKind(DatetimeKindEnum.OnlyDate);
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

    public FormHiddenField getHiddenField(String title, String field) {
        return new FormHiddenField(title, field);
    }
}
