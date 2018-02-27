package com.boco.eoms.dutyConfig.dao;

import com.boco.eoms.dutyConfig.model.DutyConfigModel;
import java.util.List;

public interface DutyConfigModelMapper {
    public static final String DATA_SOURCE_NAME = "null";

    int deleteByPrimaryKey(String dutyConfigId);

    int insert(DutyConfigModel record);

    int insertSelective(DutyConfigModel record);

    DutyConfigModel selectByPrimaryKey(int dutyConfigId);

    int updateByPrimaryKeySelective(DutyConfigModel record);

    int updateByPrimaryKey(DutyConfigModel record);
    List<DutyConfigModel> selectAll();
    int insertBatch(List<DutyConfigModel> records);
    Integer selectMaxId();
}