package cn.cerc.ui.page;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import cn.cerc.mis.core.IForm;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.core.HtmlWriter;

public class HtmlPage implements IPage {
    private IForm origin;
    private HtmlWriter content = new HtmlWriter();

    public HtmlPage(IForm form) {
        super();
        this.setOrigin(form);
    }

    @Override
    public IForm getForm() {
        return origin;
    }

    @Override
    public HtmlPage setOrigin(Object form) {
        this.origin = (IForm) form;
        return this;
    }
    
    @Override
    public IForm getOrigin() {
        return this.origin;
    }

    @Override
    public String execute() throws ServletException, IOException {
        PrintWriter out = origin.getResponse().getWriter();
        out.print(content.toString());
        return null;
    }

    public HtmlWriter getContent() {
        return content;
    }

}
