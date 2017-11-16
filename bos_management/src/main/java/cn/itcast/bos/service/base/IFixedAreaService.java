package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface IFixedAreaService {

    // 保存定区
    public abstract void save(FixedArea fixedArea);

    // 有条件的分页查询
    public abstract Page<FixedArea> pageQuery(Specification<FixedArea> specification, Pageable pageable);

    // 将快递员与收派时间和定区关联
    public abstract void associationCourierToFixedArea(String fixedAreaId, Integer courierId, Integer takeTimeId);
}
