package cn.cerc.ui.page;

import java.util.Optional;

public interface StaticFileVersionImpl {

    /**
     * 
     * @param group
     * @return sample: 220101.1
     */
    Optional<Integer> getVersion(String fileName);
}
