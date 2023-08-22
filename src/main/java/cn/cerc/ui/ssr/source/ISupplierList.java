package cn.cerc.ui.ssr.source;

import java.util.List;
import java.util.Optional;

import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.page.ISupportCanvas;

public interface ISupplierList extends ISupportCanvas, ISupportXls {

    List<String> items();

    boolean addAll();

    default Optional<String> selected() {
        return Optional.empty();
    }

}
