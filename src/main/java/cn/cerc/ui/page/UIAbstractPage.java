package cn.cerc.ui.page;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.db.redis.RedisRecord;
import cn.cerc.mis.SummerMIS;
import cn.cerc.mis.cdn.CDN;
import cn.cerc.mis.core.AppClient;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.core.SupportScriptFile;
import cn.cerc.ui.core.UIComponent;

public abstract class UIAbstractPage extends UIComponent implements IPage, SupportScriptFile {
    protected static final ClassConfig config = new ClassConfig(UIAbstractPage.class, SummerMIS.ID);

    private List<StaticFile> cssFiles = new ArrayList<>();
    private List<StaticFile> jsFiles = new ArrayList<>();
    private UIComponent header; // 头部区域，剩下的均为下部区域
    private UIComponent aside; // 左部区域，剩下的均为右部区域
    private UIComponent frontPanel; // （中间右边上方）控制区域（Web显示固定）
    private UIComponent content; // （中间右边）主内容区域
    private UIComponent footer; // （中间右边）尾部区部（Web显示固定）
    private UIComponent address; // 底部区域
    private DefineContent defineHead;
    @Deprecated
    private UIComponent menuPath; // （中间右边上方）菜单路径
    @Deprecated
    private UIComponent notice; // （中间右边上方）通知区域
    @Deprecated
    private UIComponent statusBar; // （中间右边下方）下方状态条（Web显示固定）

    public UIAbstractPage(IForm form) {
        super(null);
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
    public abstract void initComponents(AppClient client);

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
        String version = config.getString(CDN.BROWSER_CACHE_VERSION, "1.0.0.0");
        for (StaticFile file : getJsFiles()) {
            out.println(String.format("<script src=\"%s\"></script>", file.getUrl(version)));
        }
        // 加入样式文件
        for (StaticFile file : cssFiles) {
            out.println(String.format("<link href=\"%s\" rel=\"stylesheet\">", file.getUrl(version)));
        }

        if (defineHead != null) {
            UIComponent content = new UIComponent(null);
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

    public final List<StaticFile> getJsFiles() {
        return jsFiles;
    }

    @Override
    public final void addScriptFile(String fileName) {
        if (fileName.toLowerCase().startsWith("http"))
            this.addScriptFile(fileName, "");
        else
            this.addScriptFile(fileName, this.isPhone() ? "phone" : "pc");
    }

    public final void addScriptFile(String fileName, String device) {
        for (StaticFile item : jsFiles) {
            if (fileName.equals(item.getFileName()) && device.equals(item.getDevice()))
                return;
        }
        jsFiles.add(new StaticFile(StaticFileType.jsFile, fileName).setDevice(device));
    }

    public final List<StaticFile> getCssFiles() {
        return cssFiles;
    }

    public final void addCssFile(String fileName) {
        this.addCssFile(fileName, this.isPhone() ? "phone" : "pc");
    }

    public final void addCssFile(String fileName, String device) {
        cssFiles.add(new StaticFile(StaticFileType.cssFile, fileName).setDevice(device));
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

    // 从请求或缓存读取数据
    public final String getValue(RedisRecord buff, String key) {
        String value = getRequest().getParameter(key);
        if (value != null) {
            value = value.trim();
            buff.setValue(key, value);
            return value;
        }

        value = buff.getString(key).replace("{}", "");
        if (Utils.isNumeric(value) && value.endsWith(".0"))
            value = value.substring(0, value.length() - 2);

        this.add(key, value);
        return value;
    }

    public void add(String key, String value) {
        getRequest().setAttribute(key, value);
    }

}
