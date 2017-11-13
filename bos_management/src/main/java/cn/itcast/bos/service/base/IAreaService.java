package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

public interface IAreaService {

    public abstract void saveBatch(List<Area> areas);

    // 区域分页查询
    public abstract Page<Area> pageQuery(Specification<Area> specification, Pageable pageable);
}
