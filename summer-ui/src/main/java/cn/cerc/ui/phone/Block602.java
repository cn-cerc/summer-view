package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIImage;

/**
 * @author 张弓
 */
public class Block602 extends UICustomPhone {
    private List<UIImage> items = new ArrayList<>();

    /**
     * 多图片显示，上下陈列
     *
     * @param owner 内容显示区
     */
    public Block602(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.println("<div class=\"block602\">");
        for (UIImage button : items) {
            button.output(html);
        }
        html.println("</div>");
    }

    public UIImage addImage(String imgUrl) {
        UIImage image = new UIImage();
        image.setSrc(imgUrl);
        items.add(image);
        return image;
    }

    /**
     * 针对图片进行阿里云OSS压缩
     * 
     * @param imgUrl 图片地址
     * @param width  图片宽度
     * @return 压缩后的图片
     */
    public UIImage addImage(String imgUrl, int width) {
        UIImage image = new UIImage();
        image.setSrc(imgUrl, width);
        items.add(image);
        return image;
    }
}
