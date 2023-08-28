package cn.cerc.ui.ssr.editor;

public class SsrMessage {
    /** 初始化 Request msgData: HttpServletRequest */
    public static final int InitRequest = 1;
    /** 初始化 Handle msgData: IHandle */
    public static final int InitHandle = 2;
    /** 初始化 IPage msgData: IPage */
    public static final int InitPage = 3;
    /** 初始化 Binder 对象 */
    public static final int InitBinder = 4;
    /** 初始化 UIHeader msgData: UIHeader（IHeader） */
    public static final int InitHeader = 5;
    /** 初始化 UIFooter msgData: UIFooter（IFooter） */
    public static final int InitFooter = 6;

    /** 在读取完所有配置后广播 */
    public static final int InitProperties = 100;
    /** 请求准备数据 */
    public static final int InitContent = 101;

    /** 以下为 UISsrForm 事件 */
    /** 在 readAll 返回为真时发出 */
    public static final int AfterSubmit = 200;
    /** 在 appendComponent 时发出 */
    public static final int appendComponent = 201;
    /** 在 removeComponent 时发出 */
    public static final int removeComponent = 202;

    /** 在 MapSource 数据加载完成后发送 */
    public static final int InitMapSourceDone = 300;
    /** 在 ListSource 数据加载完成后发送 */
    public static final int InitListSourceDone = 301;

    public static final int RenameFieldCode = 400;
    public static final int UpdateFieldCode = 500;

    /** 在 SsrDataService 页面初始化调用服务后发送 */
    public static final int RefreshProperties = 600;
    /** 服务执行成功消息 */
    public static final int SuccessOnService = 700;
    /** 服务执行失败消息 */
    public static final int FailOnService = 701;
    /** 传递 xls 变量 */
    public static final int InitSheet = 800;
    /** Sheet行数递增 msgData 为增加的数量 */
    public static final int SheetNextRow = 801;
}
