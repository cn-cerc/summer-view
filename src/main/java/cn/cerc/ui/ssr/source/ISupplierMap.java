package cn.cerc.ui.ssr.source;

import java.util.Map;
import java.util.Optional;

import cn.cerc.ui.ssr.page.ISupportCanvas;

public interface ISupplierMap extends ISupportCanvas {

    Map<String, String> items();

    default Optional<String> selected() {
        return Optional.empty();
    }

}
