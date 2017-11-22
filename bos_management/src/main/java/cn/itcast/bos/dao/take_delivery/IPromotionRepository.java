package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.Promotion;

public interface IPromotionRepository extends JpaRepository<Promotion, Integer> {

}
