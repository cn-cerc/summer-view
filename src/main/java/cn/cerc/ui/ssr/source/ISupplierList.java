package cn.cerc.ui.ssr.source;

import java.util.List;
import java.util.Optional;

import cn.cerc.ui.ssr.page.ISupportCanvas;

public interface ISupplierList extends ISupportCanvas {

    List<String> items();

    boolean addAll();

    default Optional<String> selected() {
        return Optional.empty();
    }

}
