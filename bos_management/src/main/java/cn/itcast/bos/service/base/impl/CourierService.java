package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.ICourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.ICourierService;

@Service("courierService")
@Transactional
public class CourierService implements ICourierService {

    @Autowired
    private ICourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Override
    public Page<Courier> findPageData(Pageable pageable, Specification<Courier> specification) {
        return courierRepository.findAll(specification, pageable);
    }

    @Override
    public void updateDelTag(String[] idArray) {
        for (int i = 0; i < idArray.length; i++) {
            Integer id = Integer.parseInt(idArray[i]);
            courierRepository.updateDelTag(id);
        }
    }

    @Override
    public List<Courier> findNoAssociation() {
        // 封装查询条件
        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 查询条件，判断列表size为空
                Predicate predicate = cb.isEmpty(root.get("fixedAreas").as(Set.class));
                return predicate;
            }
        };
        return courierRepository.findAll(specification);
    }

}
