package cn.cerc.ui.other;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import cn.cerc.mis.config.ApplicationConfig;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class StrongItem extends UIComponent {
    private String name;
    private Double value;
    private String unit;

    public StrongItem(UIComponent owner) {
        super(owner);
    }

    public String getName() {
        return name;
    }

    public StrongItem setName(String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public StrongItem setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public StrongItem setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        DecimalFormat df = new DecimalFormat(ApplicationConfig.getPattern());
        html.print("%s：", this.getName());
        html.print("<strong");
        if (this.getId() != null) {
            html.print(" id=\"%s\"", this.getId());
        }
        html.print(">");
        html.print(df.format(new BigDecimal(this.value.toString())));
        if (this.getUnit() != null) {
            html.print(this.unit);
        }
        html.print("</strong>");
    }
}
