package cn.cerc.ui.ssr.core;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ServiceException;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.ado.EmptyEntity;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.CustomEntityService;
import cn.cerc.ui.grid.MutiPage;
import cn.cerc.ui.ssr.core.SvrEntityServiceList.BodyOutEntity;
import cn.cerc.ui.ssr.core.SvrEntityServiceList.HeadInEntity;

@Component
public class SvrEntityServiceList extends CustomEntityService<HeadInEntity, EmptyEntity, EmptyEntity, BodyOutEntity> {

    public static class HeadInEntity extends CustomEntity {
        @Column(name = "服务代码")
        String service_;
        @Column(name = "服务描述")
        String desc_;
        @Column(name = "载入笔数")
        int MaxRecord_;
    }

    public static class BodyOutEntity extends CustomEntity {
        @Column(name = "服务代码")
        String service_;
        @Column(name = "服务描述")
        String desc_;
    }

    @Override
    protected DataSet process(IHandle handle, HeadInEntity headIn, List<EmptyEntity> bodyIn) throws ServiceException {
        @SuppressWarnings("rawtypes")
        Map<String, CustomEntityService> map = Application.getContext().getBeansOfType(CustomEntityService.class);
        int maxRecord = headIn.MaxRecord_ == 0 ? MutiPage.PAGE_SIZE : headIn.MaxRecord_;
        DataSet dataOut = new DataSet();
        for (String beanId : map.keySet()) {
            CustomEntityService<?, ?, ?, ?> svr = map.get(beanId);
            if (this.getClass() == svr.getClass())
                continue;
            String service = svr.getClass().getSimpleName();
            if (!Utils.isEmpty(headIn.service_) && !service.contains(headIn.service_))
                continue;
            Description description = svr.getClass().getAnnotation(Description.class);
            String desc = "";
            if (description != null)
                desc = description.value();
            if (!Utils.isEmpty(headIn.desc_) && !desc.contains(headIn.desc_))
                continue;
            dataOut.append().setValue("service_", service).setValue("desc_", desc);
            if (maxRecord != -1 && dataOut.size() >= maxRecord)
                break;
        }
        return dataOut.setOk();
    }

}
