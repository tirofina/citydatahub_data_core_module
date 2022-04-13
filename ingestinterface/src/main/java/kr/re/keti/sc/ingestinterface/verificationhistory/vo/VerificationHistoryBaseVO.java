package kr.re.keti.sc.ingestinterface.verificationhistory.vo;

import com.fasterxml.jackson.annotation.*;
import kr.re.keti.sc.ingestinterface.common.vo.PageRequest;

import java.util.Date;

/**
 * Verification history DB VO class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationHistoryBaseVO extends PageRequest {

    Long seq;
    Date testTime;
    String startTime;
    String endTime;

    String datasetId;
    String dataModelId;
    String entityId;

    @JsonProperty(value = "verified")
    Boolean isVerified; //변수명이 isVerified 경우, verified 로 리턴, JsonProperty도 미적용됨
    String errorCode;
    String errorCause;
    String data;

    String smartSearchValue;
    Integer successCount;
    Integer failureCount;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDataModelId() {
		return dataModelId;
	}

	public void setDataModelId(String dataModelId) {
		this.dataModelId = dataModelId;
	}

	public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSmartSearchValue() {
        return smartSearchValue;
    }

    public void setSmartSearchValue(String smartSearchValue) {
        this.smartSearchValue = smartSearchValue;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }
}
