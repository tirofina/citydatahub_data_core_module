package kr.re.keti.sc.ingestinterface.acl.rule.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.re.keti.sc.ingestinterface.common.code.Constants;
import kr.re.keti.sc.ingestinterface.common.code.IngestInterfaceCode;
import kr.re.keti.sc.ingestinterface.common.vo.PageRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Access control policy rule VO class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AclRuleVO extends PageRequest {

    private String id;
    private String userId;
    private String clientId;
    private String resourceId;
    private IngestInterfaceCode.AclRuleResourceType resourceType;
    private IngestInterfaceCode.AclRuleCondition condition;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = Constants.CONTENT_DATE_FORMAT)
    private Date createDatetime;
    private String creatorId;
    @JsonFormat(pattern = Constants.CONTENT_DATE_FORMAT)
    @JsonProperty("modifiedAt")
    private Date modifyDatetime;
    private String modifierId;


    private String provisioningRequestId;
    private Date provisioningEventTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public IngestInterfaceCode.AclRuleResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(IngestInterfaceCode.AclRuleResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public IngestInterfaceCode.AclRuleCondition getCondition() {
        return condition;
    }

    public void setCondition(IngestInterfaceCode.AclRuleCondition condition) {
        this.condition = condition;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getModifyDatetime() {
        return modifyDatetime;
    }

    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }


    public String getProvisioningRequestId() {
        return provisioningRequestId;
    }

    public void setProvisioningRequestId(String provisioningRequestId) {
        this.provisioningRequestId = provisioningRequestId;
    }

    public Date getProvisioningEventTime() {
        return provisioningEventTime;
    }

    public void setProvisioningEventTime(Date provisioningEventTime) {
        this.provisioningEventTime = provisioningEventTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}

