package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface ICourierService {

    public abstract void save(Courier courier);

    // 快递员信息分页查询
    public abstract Page<Courier> findPageData(Pageable pageable, Specification<Courier> specification);

    // 根据快递员id批量更新是否作废状态
    public abstract void updateDelTag(String[] idArray);

    // 查询未关联的定区的快递员
    public abstract List<Courier> findNoAssociation();
}
