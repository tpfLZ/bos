package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.Standard;

public interface IStandardRepository extends JpaRepository<Standard, Integer> {

}
