package kr.re.keti.sc.dataservicebroker.common.vo;


import kr.re.keti.sc.dataservicebroker.common.code.Constants;

import java.util.List;

public class DbConditionVO {


    private String selectCondition;
    private String tableName;
    private String histTableName;
    private String geoCondition;
    private String queryCondition;
    private String timerelCondition;
    private String id;
    private String type;
    private String datasetId;
    private List<String> searchIdList;
    private String idPattern;

    private List<String> contextList;
    private List<String> watchAttributeList;

    private Integer limit;
    private Integer offset;
    private String aclDatasetCondition;

    public String getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(String selectCondition) {
        this.selectCondition = selectCondition;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getHistTableName() {
        return Constants.SCHEMA_NAME + ".\"" + histTableName + "\"";
    }

    public void setHistTableName(String histTableName) {
        this.histTableName = histTableName;
    }

    public String getGeoCondition() {
        return geoCondition;
    }

    public void setGeoCondition(String geoCondition) {
        this.geoCondition = geoCondition;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public String getTimerelCondition() {
        return timerelCondition;
    }

    public void setTimerelCondition(String timerelCondition) {
        this.timerelCondition = timerelCondition;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public List<String> getSearchIdList() {
        return searchIdList;
    }

    public void setSearchIdList(List<String> searchIdList) {
        this.searchIdList = searchIdList;
    }


    public String getIdPattern() {
        return idPattern;
    }

    public void setIdPattern(String idPattern) {
        this.idPattern = idPattern;
    }

    public List<String> getContextList() {
        return contextList;
    }

    public void setContextList(List<String> contextList) {
        this.contextList = contextList;
    }

    public List<String> getWatchAttributeList() {
        return watchAttributeList;
    }

    public void setWatchAttributeList(List<String> watchAttributeList) {
        this.watchAttributeList = watchAttributeList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAclDatasetCondition() {
        return aclDatasetCondition;
    }

    public void setAclDatasetCondition(String aclDatasetCondition) {
        this.aclDatasetCondition = aclDatasetCondition;
    }

    @Override
    public String toString() {
        return "DbConditionVO{" +
                "selectCondition='" + selectCondition + '\'' +
                ", tableName='" + tableName + '\'' +
                ", histTableName='" + histTableName + '\'' +
                ", geoCondition='" + geoCondition + '\'' +
                ", queryCondition='" + queryCondition + '\'' +
                ", timerelCondition='" + timerelCondition + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", datasetId='" + datasetId + '\'' +
                ", searchIdList=" + searchIdList +
                ", idPattern='" + idPattern + '\'' +
                ", contextList=" + contextList + '\'' +
                ", watchAttributeList=" + watchAttributeList + '\'' +
                ", limit=" + limit + '\'' +
                ", offset=" + offset + '\'' +
                ", aclDatasetCondition=" + aclDatasetCondition +
                '}';
    }
}
