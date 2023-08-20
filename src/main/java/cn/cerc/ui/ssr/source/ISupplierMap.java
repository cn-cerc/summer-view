package cn.cerc.ui.ssr.source;

import java.util.Map;
import java.util.Optional;

public interface ISupplierMap {

    Map<String, String> items();

    default Optional<String> selected() {
        return Optional.empty();
    }

}
