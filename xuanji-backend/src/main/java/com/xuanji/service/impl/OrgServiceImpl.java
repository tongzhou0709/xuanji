package com.xuanji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuanji.entity.Org;
import com.xuanji.mapper.OrgMapper;
import com.xuanji.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrgServiceImpl implements OrgService {

    private final OrgMapper orgMapper;

    @Override
    public List<Org> list() {
        return orgMapper.selectList(new LambdaQueryWrapper<Org>().orderByAsc(Org::getSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrg(Org org) {
        calculateLevel(org);
        orgMapper.insert(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrg(Org org) {
        calculateLevel(org);
        orgMapper.updateById(org);
    }

    private void calculateLevel(Org org) {
        if (org.getParentId() == null || org.getParentId() == 0L) {
            org.setOrgLevel(1);
        } else {
            Org parentOrg = orgMapper.selectById(org.getParentId());
            if (parentOrg != null && parentOrg.getOrgLevel() != null) {
                org.setOrgLevel(parentOrg.getOrgLevel() + 1);
            } else {
                // 如果找不到父机构或父机构没级别，默认根据父机构是否存在来设置
                org.setOrgLevel(parentOrg == null ? 1 : 2);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrg(Long id) {
        orgMapper.deleteById(id);
    }
}
