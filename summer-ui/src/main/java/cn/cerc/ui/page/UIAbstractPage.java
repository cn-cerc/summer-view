package cn.cerc.ui.page;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import cn.cerc.core.ClassConfig;
import cn.cerc.mis.SummerMIS;
import cn.cerc.mis.cdn.CDN;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.IClient;
import cn.cerc.mis.core.IForm;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public abstract class UIAbstractPage extends UIComponent implements IPage {
    protected static final ClassConfig config = new ClassConfig(UIAbstractPage.class, SummerMIS.ID);

    private List<String> cssFiles = new ArrayList<>();
    private List<String> jsFiles = new ArrayList<>();
    private UIComponent header; // 头部区域，剩下的均为下部区域
    private UIComponent aside; // 左部区域，剩下的均为右部区域
    @Deprecated
    private UIComponent menuPath; // （中间右边上方）菜单路径
    @Deprecated
    private UIComponent notice; // （中间右边上方）通知区域
    private UIComponent frontPanel; // （中间右边上方）控制区域（Web显示固定）
    private UIComponent content; // （中间右边）主内容区域
    private UIComponent footer; // （中间右边）尾部区部（Web显示固定）
    @Deprecated
    private UIComponent statusBar; // （中间右边下方）下方状态条（Web显示固定）
    private UIComponent address; // 底部区域
    private DefineContent defineHead;

    public UIAbstractPage(IForm form) {
        this.setOrigin(form);
    }

    @Override
    public UIAbstractPage setOrigin(Object origin) {
        super.setOrigin(origin);
        if (getForm() != null)
            this.initComponents(getForm().getClient());
        return this;
    }

    /**
     * 在此函数中，实现对header\aside\content\footer 的初始化
     * 
     * @param client 代表当前运行的客户端环境
     */
    public abstract void initComponents(IClient client);

    @Override
    public final String execute() throws ServletException, IOException {
        PrintWriter out = getResponse().getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println(String.format("<title>%s</title>", getForm().getName()));
        out.println("<meta name=\"referrer\" content=\"no-referrer\" />");
        out.println("<meta name=\"format-detection\" content=\"telephone=no\" />");
        out.println("<meta name=\"format-detection\" content=\"email=no\" />");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        out.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=9; IE=8; IE=7;\"/>");
        out.println(
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0\"/>");
        // 加入脚本文件
        String device = this.getForm().getClient().isPhone() ? "phone" : "pc";
        String staticPath = Application.getStaticPath();
        String version = config.getString(CDN.BROWSER_CACHE_VERSION, "1.0.0.0");
        for (String file : getJsFiles()) {
            String[] args = file.split("\\.");
            out.println(String.format("<script src=\"%s%s-%s.%s?v=%s\"></script>", staticPath, args[0], device, args[1],
                    version));
        }
        // 加入样式文件
        for (String file : cssFiles) {
            String[] args = file.split("\\.");
            out.println(String.format("<link href=\"%s%s-%s.%s?v=%s\" rel=\"stylesheet\">", staticPath, args[0], device,
                    args[1], version));
        }

        if (defineHead != null) {
            UIComponent content = new UIComponent();
            defineHead.execute(this, content);
            out.print(content.toString());
        }

        out.println("</head>");
        out.println("<body>");

        // 头部区域
        if (header != null) {
            out.println("<header>");
            out.println(header);
            out.println("</header>");
        }
        // 下部区域
        out.println("<main>");
        // 左部区域
        if (aside != null) {
            out.println("<aside>");
            out.println(aside);
            out.println("</aside>");
        }

        // 右侧区域
        out.println("<article>");
        // （中间右边上方）控制区域（Web显示固定）
        if (frontPanel != null) {
            out.print("<div class='frontPanel'>");
            out.print(frontPanel);
            out.println("</div>");
        }
        // （中间右边）主内容区域
        out.println("<content>");
        if (content != null)
            out.println(content);
        out.println("</content>");
        // （中间右边下方）下方状态条
        if (!isPhone()) {
            if (footer != null) {
                out.println("<footer>");
                out.println(footer);
                out.println("</footer>");
            }
        }
        out.println("</article>");
        out.println("</main>");

        if (address != null) {
            out.println("<address>");
            out.println(address);
            out.println("</address>");
        }
        if (isPhone()) {
            if (footer != null) {
                out.println("<footer>");
                out.println(footer);
                out.println("</footer>");
            }
        }
        // 底部区域
        out.println("</body>");
        out.println("</html>");
        return null;
    }

    public interface DefineContent {
        void execute(UIAbstractPage sender, UIComponent content);
    }

    public void DefineHead(DefineContent defineHead) {
        this.defineHead = defineHead;
    }

    @Override
    public void output(HtmlWriter html) {
        for (UIComponent component : this.getComponents()) {
            component.output(html);
        }
    }

    @Override
    public IForm getForm() {
        return (IForm) this.getOrigin();
    }

    public final List<String> getJsFiles() {
        return jsFiles;
    }

    public final void addScriptFile(String file) {
        jsFiles.add(file);
    }

    public final List<String> getCssFiles() {
        return cssFiles;
    }

    public final void addCssFile(String file) {
        cssFiles.add(file);
    }

    public UIComponent getHeader() {
        if (header == null)
            header = new UIComponent(this);
        return header;
    }

    public UIComponent getAside() {
        if (aside == null) {
            aside = new UIComponent(this);
        }
        return aside;
    }

    @Deprecated
    // 请改使用 getControls
    public UIComponent getMenuPath() {
        if (menuPath == null)
            menuPath = new UIComponent(this.getFrontPanel());
        return menuPath;
    }

    @Deprecated
    // 请改使用 getControls
    public UIComponent getNotice() {
        if (notice == null)
            notice = new UIComponent(this.getFrontPanel());
        return notice;
    }

    public UIComponent getContent() {
        if (content == null)
            content = new UIComponent(this);
        return content;
    }

    public UIComponent getFrontPanel() {
        if (frontPanel == null)
            frontPanel = new UIComponent(this);
        return frontPanel;
    }

    @Deprecated
    // 请改使用 getFooter
    public UIComponent getStatusBar() {
        if (statusBar == null)
            statusBar = new UIComponent(this);
        return statusBar;
    }

    public UIComponent getFooter() {
        if (footer == null)
            footer = new UIComponent(this);
        return footer;
    }

    public UIComponent getAddress() {
        if (address == null)
            address = new UIComponent(this);
        return address;
    }

}
