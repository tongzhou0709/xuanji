package com.xuanji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuanji.entity.Menu;
import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> listTree();
    void saveMenu(Menu menu);
    void updateMenu(Menu menu);
    void deleteMenu(Long id);
}
