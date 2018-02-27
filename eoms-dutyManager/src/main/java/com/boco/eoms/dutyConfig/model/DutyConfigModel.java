package com.boco.eoms.dutyConfig.model;

import java.io.Serializable;

public class DutyConfigModel implements Serializable {
    private int id;

    private String dutyConfigName;

    private String dutyHandoverType;

    private String dutyHandoverTime;

    private int lastDutyConfigId;

    private String createTime;

    private static final long serialVersionUID = 1L;

    private String deleteIf;

    public String getDeleteIf() {
		return deleteIf;
	}

	public void setDeleteIf(String deleteIf) {
		this.deleteIf = deleteIf;
	}

	public int getId() {
		return id;
	}

	public void setDutyConfigId(int id) {
		this.id = id;
	}

	public String getDutyConfigName() {
        return dutyConfigName;
    }

    public void setDutyConfigName(String dutyConfigName) {
        this.dutyConfigName = dutyConfigName;
    }

    public String getDutyHandoverType() {
        return dutyHandoverType;
    }

    public void setDutyHandoverType(String dutyHandoverType) {
        this.dutyHandoverType = dutyHandoverType;
    }

    public String getDutyHandoverTime() {
        return dutyHandoverTime;
    }

    public void setDutyHandoverTime(String dutyHandoverTime) {
        this.dutyHandoverTime = dutyHandoverTime;
    }

   

    public int getLastDutyConfigId() {
		return lastDutyConfigId;
	}

	public void setLastDutyConfigId(int lastDutyConfigId) {
		this.lastDutyConfigId = lastDutyConfigId;
	}

	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    private DutyConfigModel(Builder b) {
        id = b.id;
        dutyConfigName = b.dutyConfigName;
        dutyHandoverType = b.dutyHandoverType;
        dutyHandoverTime = b.dutyHandoverTime;
        lastDutyConfigId = b.lastDutyConfigId;
        createTime = b.createTime;
    }

    public DutyConfigModel() {
        super();
    }

    public static class Builder {
        private int id;

        private String dutyConfigName;

        private String dutyHandoverType;

        private String dutyHandoverTime;

        private int lastDutyConfigId;

        private String createTime;

        public Builder dutyConfigId(int dutyConfigId) {
            this.id = id;
            return this;
        }

        public Builder dutyConfigName(String dutyConfigName) {
            this.dutyConfigName = dutyConfigName;
            return this;
        }

        public Builder dutyHandoverType(String dutyHandoverType) {
            this.dutyHandoverType = dutyHandoverType;
            return this;
        }

        public Builder dutyHandoverTime(String dutyHandoverTime) {
            this.dutyHandoverTime = dutyHandoverTime;
            return this;
        }

        public Builder lastDutyConfigId(int lastDutyConfigId) {
            this.lastDutyConfigId = lastDutyConfigId;
            return this;
        }

        public Builder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public DutyConfigModel build() {
            return new DutyConfigModel(this);
        }
    }
}