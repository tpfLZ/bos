package cn.itcast.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.User;

public interface IUserRepository extends JpaRepository<User, Integer> {

    public abstract User findByUsername(String username);
}
