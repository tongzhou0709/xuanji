package com.xuanji.service;

import com.xuanji.entity.Org;
import java.util.List;

public interface OrgService {
    List<Org> list();
    void saveOrg(Org org);
    void updateOrg(Org org);
    void deleteOrg(Long id);
}
