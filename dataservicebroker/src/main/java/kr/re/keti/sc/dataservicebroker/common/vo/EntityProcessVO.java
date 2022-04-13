package kr.re.keti.sc.dataservicebroker.common.vo;

import java.util.List;

import kr.re.keti.sc.dataservicebroker.common.code.DataServiceBrokerCode.Operation;
import kr.re.keti.sc.dataservicebroker.common.code.DataServiceBrokerCode.OperationOption;
import kr.re.keti.sc.dataservicebroker.datamodel.vo.DataModelCacheVO;

public class EntityProcessVO<T1 extends CommonEntityFullVO, T2 extends CommonEntityDaoVO> {
	/** 요청 reqeust id */
	private String reqeustId;
	/** 요청 e2e reqeust id */
	private String e2eReqeustId;
	/** 데이터셋아이디 */
	private String datasetId;
	/** 요청 수신 받은 content */
	private String content;
	/** Operation */
	private Operation operation;
	/** 수신받은 데이터 VO */
	private T1 entityFullVO;
	/** Operation 별 DB 처리 시 사용될 VO */
	private T2 entityDaoVO;
	/** Entity 파싱 시 사용될 DataModel 정보 */
	private DataModelCacheVO dataModelCacheVO;
	/** 처리결과 VO */
	private ProcessResultVO processResultVO = new ProcessResultVO();
	/** 처리Options */
	private List<OperationOption> operationOptions;

	public String getReqeustId() {
		return reqeustId;
	}
	public void setReqeustId(String reqeustId) {
		this.reqeustId = reqeustId;
	}
	public String getE2eReqeustId() {
		return e2eReqeustId;
	}
	public void setE2eReqeustId(String e2eReqeustId) {
		this.e2eReqeustId = e2eReqeustId;
	}
	public String getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public T1 getEntityFullVO() {
		return entityFullVO;
	}
	public void setEntityFullVO(T1 entityFullVO) {
		this.entityFullVO = entityFullVO;
	}
	public T2 getEntityDaoVO() {
		return entityDaoVO;
	}
	public void setEntityDaoVO(T2 entityDaoVO) {
		this.entityDaoVO = entityDaoVO;
	}
	public DataModelCacheVO getDataModelCacheVO() {
		return dataModelCacheVO;
	}
	public void setDataModelCacheVO(DataModelCacheVO dataModelCacheVO) {
		this.dataModelCacheVO = dataModelCacheVO;
	}
	public ProcessResultVO getProcessResultVO() {
		return processResultVO;
	}
	public void setProcessResultVO(ProcessResultVO processResultVO) {
		this.processResultVO = processResultVO;
	}
	public List<OperationOption> getOperationOptions() {
		return operationOptions;
	}
	public void setOperationOptions(List<OperationOption> operationOptions) {
		this.operationOptions = operationOptions;
	}
}
