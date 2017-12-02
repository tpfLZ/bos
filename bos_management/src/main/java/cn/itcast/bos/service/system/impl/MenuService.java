package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.IMenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.system.IMenuService;

@Service("menuService")
@Transactional
public class MenuService implements IMenuService {

    @Autowired
    private IMenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public void save(Menu menu) {
        menuRepository.save(menu);
    }

}
