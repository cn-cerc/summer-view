package cn.cerc.ui.other;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.page.StaticFile;

public class UIUploadImage extends UIComponent {

    private String uploadFile;
    private String uploadText;
    private String imageSrc;
    private Map<String, String> params = new LinkedHashMap<String, String>();
    private boolean readOnly = false;
    private boolean preview = true;
    private String callBack;

    public UIUploadImage(UIComponent owner, String uploadFile) {
        super(owner);
        this.setUploadFile(uploadFile);
    }

    public UIUploadImage(UIComponent owner, String uploadFile, String uploadText) {
        this(owner, uploadFile);
        this.setUploadText(uploadText);
    }

    public UIUploadImage(UIComponent owner, String uploadFile, String uploadText, String imageSrc) {
        this(owner, uploadFile);
        this.setUploadText(uploadText);
        this.setImageSrc(imageSrc);
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<div class='uploadImage%s'>", Utils.isEmpty(this.getCssClass()) ? "" : " " + this.getCssClass());
        String param = "";
        if (this.params.size() > 0) {
            param = "{";
            for (Map.Entry<String, String> keyValue : params.entrySet()) {
                param += String.format("%s: %s,", keyValue.getKey(), keyValue.getValue());
            }
            param += "}";
        }
        String callBackFunction = Utils.isEmpty(callBack) ? "" : ", " + callBack;
        String clickEvent = Utils.isEmpty(param)
                ? "uploadImage(this, \"" + uploadFile + "\", \"\", " + preview + callBackFunction + ")"
                : "uploadImage(this, \"" + uploadFile + "\", " + param + ", " + preview + callBackFunction + ")";
        html.println("<div class='uploadImg' %s>", readOnly
                ? String.format("data-event='uploadImage' data-file='%s' data-param='%s'", uploadFile, param)
                : String.format("onclick='%s'", clickEvent));
        if (Utils.isEmpty(imageSrc)) {
            html.println("<img src='%s' class='uploadIcon'/>", StaticFile.getImage("images/icon/photograph.png"));
            if (!Utils.isEmpty(uploadText))
                html.println("<span>%s</span>", uploadText);
        } else
            html.println("<img src='%s' />", imageSrc);
        html.println("</div>");
        html.println("<div class='uploadInput'>");
        html.println("<input type='file' accept='image/*' onChange='%s' />", Utils.isEmpty(param)
                ? "uploadInputChange(this, \"" + uploadFile + "\", \"\", " + preview + callBackFunction + ")"
                : "uploadInputChange(this, \"" + uploadFile + "\", " + param + ", " + preview + callBackFunction + ")");
        html.println("</div>");
        html.println("</div>");
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadText() {
        return uploadText;
    }

    public void setUploadText(String uploadText) {
        this.uploadText = uploadText;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }
}
