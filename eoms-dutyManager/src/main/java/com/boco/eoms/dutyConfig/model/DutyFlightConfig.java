package com.boco.eoms.dutyConfig.model;

import java.io.Serializable;

public class DutyFlightConfig implements Serializable {
    private int dutyFlightId;

    private String dutyFlightName;

    private String dutyFlightBegintime;

    private String dutyFlightEndtime;

    private int dutyConfigId;

    private int lastDutyFlightId;

    private String createTime;

    private int lastDutyConfigId;

    private static final long serialVersionUID = 1L;

    private int deleteIf;

    public int getDutyFlightId() {
		return dutyFlightId;
	}

	public void setDutyFlightId(int dutyFlightId) {
		this.dutyFlightId = dutyFlightId;
	}

	public int getDutyConfigId() {
		return dutyConfigId;
	}

	public void setDutyConfigId(int dutyConfigId) {
		this.dutyConfigId = dutyConfigId;
	}

	public int getLastDutyFlightId() {
		return lastDutyFlightId;
	}

	public void setLastDutyFlightId(int lastDutyFlightId) {
		this.lastDutyFlightId = lastDutyFlightId;
	}

	public int getLastDutyConfigId() {
		return lastDutyConfigId;
	}

	public void setLastDutyConfigId(int lastDutyConfigId) {
		this.lastDutyConfigId = lastDutyConfigId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDutyFlightName() {
        return dutyFlightName;
    }

    public void setDutyFlightName(String dutyFlightName) {
        this.dutyFlightName = dutyFlightName;
    }

    public String getDutyFlightBegintime() {
        return dutyFlightBegintime;
    }

    public void setDutyFlightBegintime(String dutyFlightBegintime) {
        this.dutyFlightBegintime = dutyFlightBegintime;
    }

    public String getDutyFlightEndtime() {
        return dutyFlightEndtime;
    }

    public void setDutyFlightEndtime(String dutyFlightEndtime) {
        this.dutyFlightEndtime = dutyFlightEndtime;
    }

    

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    

    private DutyFlightConfig(Builder b) {
        dutyFlightId = b.dutyFlightId;
        dutyFlightName = b.dutyFlightName;
        dutyFlightBegintime = b.dutyFlightBegintime;
        dutyFlightEndtime = b.dutyFlightEndtime;
        dutyConfigId = b.dutyConfigId;
        lastDutyFlightId = b.lastDutyFlightId;
        createTime = b.createTime;
        lastDutyConfigId = b.lastDutyConfigId;
    }

    public DutyFlightConfig() {
        super();
    }

    public static class Builder {
        private int dutyFlightId;

        private String dutyFlightName;

        private String dutyFlightBegintime;

        private String dutyFlightEndtime;

        private int dutyConfigId;

        private int lastDutyFlightId;

        private String createTime;

        private int lastDutyConfigId;
        
        

        public Builder dutyFlightId(int dutyFlightId) {
            this.dutyFlightId = dutyFlightId;
            return this;
        }

        public Builder dutyFlightName(String dutyFlightName) {
            this.dutyFlightName = dutyFlightName;
            return this;
        }

        public Builder dutyFlightBegintime(String dutyFlightBegintime) {
            this.dutyFlightBegintime = dutyFlightBegintime;
            return this;
        }

        public Builder dutyFlightEndtime(String dutyFlightEndtime) {
            this.dutyFlightEndtime = dutyFlightEndtime;
            return this;
        }

        public Builder dutyConfigId(int dutyConfigId) {
            this.dutyConfigId = dutyConfigId;
            return this;
        }

        public Builder lastDutyFlightId(int lastDutyFlightId) {
            this.lastDutyFlightId = lastDutyFlightId;
            return this;
        }

        public Builder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder lastDutyConfigId(int lastDutyConfigId) {
            this.lastDutyConfigId = lastDutyConfigId;
            return this;
        }

        public DutyFlightConfig build() {
            return new DutyFlightConfig(this);
        }
    }
}