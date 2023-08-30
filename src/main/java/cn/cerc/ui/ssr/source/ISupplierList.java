package cn.cerc.ui.ssr.source;

import java.util.List;
import java.util.Optional;

public interface ISupplierList {

    List<String> items();

    boolean addAll();

    default Optional<String> selected() {
        return Optional.empty();
    }

}
