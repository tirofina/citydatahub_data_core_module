package kr.re.keti.sc.dataservicebroker.entities.datalifecycle.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DataLifeCyleDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    public int deleteEntity(String tableName, String datasetId, Date lifeCyleDate) {

        Map<String, Object> param = new HashMap<>();
        param.put("tableName", tableName);
        param.put("datasetId", datasetId);
        param.put("lifeCyleDate", lifeCyleDate);

        return sqlSession.update("dataservicebroker.datalifecyle.deleteEntity", param);
    }

}