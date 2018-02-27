package com.boco.eoms.dutyConfig.dao;

import com.boco.eoms.dutyConfig.model.DutyFlightConfig;
import java.math.BigDecimal;
import java.util.List;

public interface DutyFlightConfigMapper {
    public static final String DATA_SOURCE_NAME = "null";

    int deleteByPrimaryKey(int dutyFlightId);
    
    int deleteByDutyId(int dutyConfigId);

    int insert(DutyFlightConfig record);

    int insertSelective(DutyFlightConfig record);

    DutyFlightConfig selectByPrimaryKey(int dutyFlightId);

    int updateByPrimaryKeySelective(DutyFlightConfig record);

    int updateByPrimaryKey(DutyFlightConfig record);

    int insertBatch(List<DutyFlightConfig> records);
    
    List selectByDutyConfigId(int duty_config_id);
    
    Integer selectMaxId(int dutyConfigId);
    
}