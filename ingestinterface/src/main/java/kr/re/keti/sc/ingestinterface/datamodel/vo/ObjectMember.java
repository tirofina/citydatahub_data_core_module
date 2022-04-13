package kr.re.keti.sc.ingestinterface.datamodel.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.re.keti.sc.ingestinterface.common.code.IngestInterfaceCode.AttributeValueType;

/**
 * Data model ObjectMember VO class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectMember {
    private String name;
    private String description;
    private Boolean isRequired;
    private Double greaterThan;
    private Double greaterThanOrEqualTo;
    private Double lessThanOrEqualTo;
    private Double lessThan;
    private String maxLength;
    private String minLength;
    private AttributeValueType valueType;
    private List<Object> valueEnum;
    private List<ObjectMember> objectMembers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Double getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(Double greaterThan) {
        this.greaterThan = greaterThan;
    }

    public Double getGreaterThanOrEqualTo() {
        return greaterThanOrEqualTo;
    }

    public void setGreaterThanOrEqualTo(Double greaterThanOrEqualTo) {
        this.greaterThanOrEqualTo = greaterThanOrEqualTo;
    }

    public Double getLessThanOrEqualTo() {
        return lessThanOrEqualTo;
    }

    public void setLessThanOrEqualTo(Double lessThanOrEqualTo) {
        this.lessThanOrEqualTo = lessThanOrEqualTo;
    }

    public Double getLessThan() {
        return lessThan;
    }

    public void setLessThan(Double lessThan) {
        this.lessThan = lessThan;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public AttributeValueType getValueType() {
        return valueType;
    }

    public void setValueType(AttributeValueType valueType) {
        this.valueType = valueType;
    }

    public List<Object> getValueEnum() {
        return valueEnum;
    }

    public void setValueEnum(List<Object> valueEnum) {
        this.valueEnum = valueEnum;
    }

    public List<ObjectMember> getObjectMembers() {
        return objectMembers;
    }

    public void setObjectMembers(List<ObjectMember> objectMembers) {
        this.objectMembers = objectMembers;
    }
}
