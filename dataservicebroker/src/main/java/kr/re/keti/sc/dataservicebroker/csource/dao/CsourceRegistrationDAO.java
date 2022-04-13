package kr.re.keti.sc.dataservicebroker.csource.dao;

import java.sql.SQLException;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.mybatis.spring.SqlSessionTemplate;
import org.postgis.LineString;
import org.postgis.MultiLineString;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.re.keti.sc.dataservicebroker.common.code.Constants;
import kr.re.keti.sc.dataservicebroker.common.code.DataServiceBrokerCode;
import kr.re.keti.sc.dataservicebroker.common.code.DataServiceBrokerCode.ErrorCode;
import kr.re.keti.sc.dataservicebroker.common.exception.ngsild.NgsiLdBadRequestException;
import kr.re.keti.sc.dataservicebroker.common.vo.DbConditionVO;
import kr.re.keti.sc.dataservicebroker.common.vo.QueryVO;
import kr.re.keti.sc.dataservicebroker.csource.vo.CsourceRegistrationBaseDaoVO;
import kr.re.keti.sc.dataservicebroker.csource.vo.CsourceRegistrationEntityDaoVO;
import kr.re.keti.sc.dataservicebroker.csource.vo.CsourceRegistrationInfoDaoVO;
import kr.re.keti.sc.dataservicebroker.datamodel.vo.Attribute;
import kr.re.keti.sc.dataservicebroker.datamodel.vo.DataModelCacheVO;
import kr.re.keti.sc.dataservicebroker.util.ConvertTimeParamUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CsourceRegistrationDAO implements CsourceRegistrationDAOInterface {

    @Autowired
    private SqlSessionTemplate sqlSession;
    @Autowired
    @Qualifier("retrieveSqlSession")
    private SqlSessionTemplate retrieveSqlSession;
    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${entity.retrieve.default.limit:1000}")
    private Integer defaultLimit;

    private String defaultLocationAttrName = "location";

    //Geo-query default EPSG 세팅
    @Value("${geometry.default.EPSG:4326}")
    private String defaultEPSG;



    @Override
    public Integer createCsourceRegistrationBase(CsourceRegistrationBaseDaoVO csourceRegistrationBaseDaoVO) {
        return sqlSession.update("dataservicebroker.csource.registration.createCsourceRegistrationBase", csourceRegistrationBaseDaoVO);
    }

    @Override
    public Integer createCsourceRegistrationInfo(List<CsourceRegistrationInfoDaoVO> csourceRegistrationInfoDaoVOs) {

        Map<String, Object> csourceRegistrationInfoMap = new HashMap<String, Object>();
        csourceRegistrationInfoMap.put("list", csourceRegistrationInfoDaoVOs);
        return sqlSession.update("dataservicebroker.csource.registration.createCsourceRegistrationInfo", csourceRegistrationInfoMap);
    }


    @Override
    public Integer createCsourceRegistrationEntity(List<CsourceRegistrationEntityDaoVO> csourceRegistrationEntityDaoVOs) {

        Map<String, Object> csourceRegistrationEntityMap = new HashMap<String, Object>();
        csourceRegistrationEntityMap.put("list", csourceRegistrationEntityDaoVOs);
        return sqlSession.update("dataservicebroker.csource.registration.createCsourceRegistrationEntity", csourceRegistrationEntityMap);
    }


    /**
     * contet source 개별 조회
     *
     * @param registrationId
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public List<CsourceRegistrationBaseDaoVO> retrieveCsourceRegistration(String registrationId) {

        List<CsourceRegistrationBaseDaoVO> csourceRegistrationBaseDaoVOs = retrieveSqlSession.selectList("dataservicebroker.csource.registration.selectCsourceRegistrationByRegistrationId", registrationId);
        return csourceRegistrationBaseDaoVOs;
    }


    /**
     * contet source 전체 조회
     *
     * @param queryVO
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public List<CsourceRegistrationBaseDaoVO> queryCsourceRegistration(QueryVO queryVO) {

        DbConditionVO dbConditionVO = setQueryCondition(queryVO);
        List<CsourceRegistrationBaseDaoVO> csourceRegistrationBaseDaoVOs = retrieveSqlSession.selectList("dataservicebroker.csource.registration.selectCsourceRegistration", dbConditionVO);
        return csourceRegistrationBaseDaoVOs;
    }


    /**
     * contet source 전체 조회 (Count)
     *
     * @param queryVO
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Integer queryCsourceRegistrationCount(QueryVO queryVO) {

        DbConditionVO dbConditionVO = setQueryCondition(queryVO);
        Integer totalCount = retrieveSqlSession.selectOne("dataservicebroker.csource.registration.selectCsourceRegistrationCount", dbConditionVO);
        return totalCount;
    }

    /**
     * contet source 전체 조회
     *
     * @param entityId
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public List<CsourceRegistrationBaseDaoVO> queryCsourceRegistrationByEntityId(String entityId) {

        List<CsourceRegistrationBaseDaoVO> csourceRegistrationBaseDaoVOs = retrieveSqlSession.selectList("dataservicebroker.csource.registration.selectCsourceRegistrationByEntityId", entityId);
        return csourceRegistrationBaseDaoVOs;
    }

    @Override
    public Integer deleteCsourceRegistrationBase(String id) {
        return sqlSession.update("dataservicebroker.csource.registration.deleteCsourceRegistrationBase", id);
    }

    @Override
    public Integer deleteCsourceRegistrationInfo(String csourceRegistrationBaseId) {
        return sqlSession.update("dataservicebroker.csource.registration.deleteCsourceRegistrationInfo", csourceRegistrationBaseId);
    }

    @Override
    public Integer deleteCsourceRegistrationEntity(String csourceRegistrationBaseId) {
        return sqlSession.update("dataservicebroker.csource.registration.deleteCsourceRegistrationEntity", csourceRegistrationBaseId);
    }


    @Override
    public Integer updateCsourceRegistrationBase(CsourceRegistrationBaseDaoVO csourceRegistrationBaseDaoVO) {
        return sqlSession.update("dataservicebroker.csource.registration.updateCsourceRegistrationBase", csourceRegistrationBaseDaoVO);
    }
//
//    @Override
//    public Integer updateCsourceRegistrationInfo(List<CsourceRegistrationInfoDaoVO> csourceRegistrationInfoDaoVOs) {
//
//        Map<String, Object> csourceRegistrationInfoMap = new HashMap<String, Object>();
//        csourceRegistrationInfoMap.put("list", csourceRegistrationInfoDaoVOs);
//        return sqlSession.update("dataservicebroker.csource.registration.createCsourceRegistrationInfo", csourceRegistrationInfoMap);
//    }
//
//
//    @Override
//    public Integer updateCsourceRegistrationEntity(List<CsourceRegistrationEntityDaoVO> csourceRegistrationEntityDaoVOs) {
//
//        Map<String, Object> csourceRegistrationEntityMap = new HashMap<String, Object>();
//        csourceRegistrationEntityMap.put("list", csourceRegistrationEntityDaoVOs);
//        return sqlSession.update("dataservicebroker.csource.registration.createCsourceRegistrationEntity", csourceRegistrationEntityMap);
//    }


    /**
     * DB 조회 조건 세팅
     *
     * @param queryVO
     * @return
     */
    private DbConditionVO setQueryCondition(QueryVO queryVO) {

         /*

• A list (one or more) of Attribute names (optional).
• An id pattern as a regular expression (optional).
• An NGSI-LD temporal query (optional) as per clause 4.11.
• An NGSI-LD context source query (optional) as per clause 4.9.
• A limit to the number of Context Source Registrations to be retrieved. See clause 5.5.9. At least one of (a) list of Entity Types or (b) list of Attribute names shall be present.

         */

        DbConditionVO dbConditionVO = new DbConditionVO();

        // id  조건 넣기
        // • A list (one or more) of Entity identifiers (optional).
        if (queryVO.getId() != null) {

            dbConditionVO.setId(queryVO.getId());
        }

        //A reference to a JSON-LD @context (optional).
        if (queryVO.getContext() != null) {
            dbConditionVO.setContextList(queryVO.getContext());
        }

        //• A list (one or more) of Entity types of the matching entities (optional).

        //1. 조회 대상 컬럼 세팅
        //• A list (one or more) of Attribute names (optional).
        dbConditionVO.setWatchAttributeList(queryVO.getAttrs());


//        // id pattern 조건 넣기
        if (queryVO.getIdPattern() != null) {
        	try {
        		Pattern.compile(queryVO.getIdPattern());
        	} catch(PatternSyntaxException e) {
        		log.warn("invalid RegEx expression. idPattern={}", queryVO.getIdPattern());
        		throw new NgsiLdBadRequestException(ErrorCode.INVALID_PARAMETER, "invalid RegEx expression");
        	}
            dbConditionVO.setIdPattern(queryVO.getIdPattern());
        }

        //2. geo-query param 처리
        // • An NGSI-LD geo-query (optional) as per clause 4.10.
        if (queryVO.getGeorel() != null) {
            dbConditionVO.setGeoCondition(this.convertGeoQuery(generateGeoQuery(queryVO)));
        }

        if (queryVO.getLimit() == null) {
            dbConditionVO.setLimit(defaultLimit);
        } else {
            dbConditionVO.setLimit(queryVO.getLimit());
        }
        dbConditionVO.setOffset(queryVO.getOffset());
//
//
//        //4. 시간 조건(time rel) param 처리
//        if (isTemproal == true) {
//
//            //4. timerel param 처리, 이력 데이터 조회시에만 적용
//            if (queryVO.getTimerel() != null) {
//                queryVO = convertTimerel(queryVO);
//                dbConditionVO.setQueryCondition(queryVO.getTimeQuery());
//            }
//        }
        //4. timerel param 처리, 이력 데이터 조회시에만 적용
        if (queryVO.getTimerel() != null) {
            queryVO = convertCsourceRegistrationTimerel(queryVO);
            dbConditionVO.setQueryCondition(queryVO.getTimeQuery());
        }
        //• An NGSI-LD query (optional) as per clause 4.9.

        return dbConditionVO;
    }


    public String convertGeoQuery(QueryVO queryVO) {

        String geoRel = queryVO.getGeorel();

        String query = null;
        StringBuilder stringBuilder = new StringBuilder();

        if (geoRel.equalsIgnoreCase(DataServiceBrokerCode.GeometryType.NEAR_REL.getCode())) {

            if (queryVO.getMinDistance() != null) {

                stringBuilder.append(" ST_DWithin(")
                        .append(queryVO.getLocationCol())
                        .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')")
                        .append(", " + queryVO.getMinDistance())
                        .append(") is false");


            } else if (queryVO.getMaxDistance() != null) {

                stringBuilder.append(" ST_DWithin(")
                        .append(queryVO.getLocationCol())
                        .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')")
                        .append(", " + queryVO.getMaxDistance())
                        .append(")");


            }

        } else if (geoRel.equalsIgnoreCase("within")) {

            stringBuilder.append("ST_Within(")
                    .append(queryVO.getLocationCol())
                    .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')::geometry")
                    .append(")");

        } else if (geoRel.equalsIgnoreCase("contains")) {

            stringBuilder.append("ST_Contains(")
                    .append(queryVO.getLocationCol())
                    .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')::geometry")
                    .append(")");

        } else if (geoRel.equalsIgnoreCase("overlaps")) {

            stringBuilder.append("ST_Overlaps(")
                    .append(queryVO.getLocationCol())
                    .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')::geometry")
                    .append(")");

        } else if (geoRel.equalsIgnoreCase("intersects")) {

            stringBuilder.append("ST_Intersects(")
                    .append(queryVO.getLocationCol())
                    .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')::geometry")
                    .append(")");

        } else if (geoRel.equalsIgnoreCase("equals")) {

            stringBuilder.append("ST_Equals(")
                    .append(queryVO.getLocationCol())
                    .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')::geometry")
                    .append(")");
        } else if (geoRel.equalsIgnoreCase("disjoint")) {

            stringBuilder.append("ST_Disjoint(")
                    .append(queryVO.getLocationCol())
                    .append(", ST_GeogFromText('").append(queryVO.getGeometryValue()).append("')::geometry")
                    .append(")");

        }

        query = stringBuilder.toString();

        return query;
    }

    /**
     * location 정보 -> postgis geometry Value(text)로 변환
     *
     * @param queryVO
     * @return
     */
    private QueryVO generateGeoQuery(QueryVO queryVO) {
        /*
            georel = nearRel / withinRel / containsRel / overlapsRel / intersectsRel / equalsRel / disjointRel
            nearRel = nearOp andOp distance equal PositiveNumber distance = "maxDistance" / "minDistance"
            nearOp = "near"
            withinRel = "within"
            containsRel = "contains"
            intersectsRel = "intersects"
            equalsRel = "equals"
            disjointRel = "disjoint"
            overlapsRel = "overlaps"
            ; near;max(min)Distance==x (in meters)
         */

        if (queryVO.getGeorel() != null && queryVO.getCoordinates() != null && queryVO.getGeometry() != null) {

            try {
                String georelFullTxt = queryVO.getGeorel();
                String georelName = georelFullTxt.split(";")[0];

                if (georelFullTxt.startsWith(DataServiceBrokerCode.GeometryType.NEAR_REL.getCode())) {
                    queryVO.setGeorel(georelName);
                    String distanceText = georelFullTxt.split(";")[1];
                    String distanceColName = distanceText.split("==")[0];
                    int distance = Integer.parseInt(distanceText.split("==")[1]);

                    if (distanceColName.equals(DataServiceBrokerCode.GeometryType.MIN_DISTANCE.getCode())) {
                        queryVO.setMinDistance(distance);
                    } else if (distanceColName.equals(DataServiceBrokerCode.GeometryType.MAX_DISTANCE.getCode())) {
                        queryVO.setMaxDistance(distance);
                    } else {

                        log.warn("invalid geo-query parameter");
                        throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid geo-query parameter");
                    }

                }

                DataServiceBrokerCode.GeometryType geometryType = DataServiceBrokerCode.GeometryType.parseType(georelName);
                if (geometryType == null) {
                    log.warn("invalid geo-query parameter");
                    throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid geo-query parameter");
                }
            } catch (Exception e) {
                throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid geo-query parameter", e);
            }


            if (log.isDebugEnabled()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("queryVO =").append(queryVO.toString());
                log.debug(stringBuilder.toString());
            }


            int srid = Integer.parseInt(defaultEPSG);
            String locationCol;

            if (queryVO.getGeoproperty() != null) {

                locationCol = queryVO.getGeoproperty();
//                locationCol = locationCol.replace(".", "_") + "_" + srid;

            } else {
                locationCol = defaultLocationAttrName;
            }

            PGgeometry pGgeometry = makePostgisType(queryVO.getGeometry(), queryVO.getCoordinates(), srid);

            String pGgeometryValue = pGgeometry.getValue();

            queryVO.setGeometryValue(pGgeometryValue);
            queryVO.setLocationCol(locationCol);

            if (log.isDebugEnabled()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("geometryValue : ")
                        .append(pGgeometryValue)
                        .append(" locationCol : ")
                        .append(locationCol);
                log.debug(stringBuilder.toString());
            }

            return queryVO;

        } else {
            log.warn("invalid geo-query parameter");
            throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid geo-query parameter");
        }
    }

    /**
     * Geo-Query시, 기준이 되는 Geo Column명을 가져옴
     * <p>
     * 세팅 조건
     * 우선 순위 1. application-properties내 정보와 일치하는 컬럼이 있을 경우
     * 2. 1번의 케이스가 x 이고, type내 RootAttribute 중 GEO_PROPERTY 중 첫번째로 검색되는 컬럼
     *
     * @param dataModelCacheVO
     * @return
     */
    private String getDefaultLocationColName(DataModelCacheVO dataModelCacheVO) {

        String locationColName = null;

        Attribute locationAttr = dataModelCacheVO.getRootAttribute(defaultLocationAttrName);

        if (locationAttr != null) {
        	locationColName = locationAttr.getName() + Constants.GEO_PREFIX_4326;

        } else {
            for (Attribute rootAttribute : dataModelCacheVO.getDataModelVO().getAttributes()) {
                if (rootAttribute.getAttributeType() == DataServiceBrokerCode.AttributeType.GEO_PROPERTY) {
                    locationColName = rootAttribute.getName() + Constants.GEO_PREFIX_4326;
                    break;
                }
            }
        }
        return locationColName;
    }


    /**
     * geo-query 관련 정보를 postGiS type으로 변환 ( Point, Polygon, LineString, MultiLineString, MultiPolygon)
     *
     * @param geometry
     * @param coordinates
     * @param srid
     * @return
     */
    private PGgeometry makePostgisType(String geometry, String coordinates, int srid) {

        log.debug("geometry : '{}', coordinates : {}, srid : {}", geometry, coordinates, srid);

        try {
            String convertedCoordinates = convertCoordinatesToPGisStr(coordinates);

            PGgeometry pGgeometry = new PGgeometry();

            if (geometry.equalsIgnoreCase("Point")) {

//             {"type": "Point", "coordinates": [100.0, 0.0]}

                Point point = new Point(convertedCoordinates);
                point.setSrid(srid);
                pGgeometry.setGeometry(point);

            } else if (geometry.equalsIgnoreCase("Polygon")) {

            /*
            	"geometry": {
            		"type": "Polygon",
            		"coordinates": [
            			[
            				[100.0, 0.0],
            			 	[101.0, 0.0],
            			  	[101.0, 1.0],
            			   	[100.0, 1.0],
            			    [100.0, 0.0]
            			 ]
            		]
            	}
            */

                Polygon polygon = new Polygon(convertedCoordinates);
                polygon.setSrid(srid);
                pGgeometry.setGeometry(polygon);

            } else if (geometry.equalsIgnoreCase("LineString")) {

            /*
                "type": "Feature",
                "geometry": {
                   "type": "LineString",
                   "coordinates": [
                       [102.0, 0.0],
                       [103.0, 1.0],
                       [104.0, 0.0],
                       [105.0, 1.0]
                   ]
                }
            */

                LineString lineString = new LineString(convertedCoordinates);
                lineString.setSrid(srid);
                pGgeometry.setGeometry(lineString);


            } else if (geometry.equalsIgnoreCase("MultiLineString")) {

            /*
                {
                   "type": "MultiLineString",
                   "coordinates": [
                       [
                           [170.0, 45.0], [180.0, 45.0]
                       ], [
                           [-180.0, 45.0], [-170.0, 45.0]
                       ]
                   ]
                }
            */

                MultiLineString multiLineString = new MultiLineString(convertedCoordinates);
                multiLineString.setSrid(srid);
                pGgeometry.setGeometry(multiLineString);

            } else if (geometry.equalsIgnoreCase("MultiPolygon")) {

            /*
                {
                   "type": "MultiPolygon",
                   "coordinates": [
                       [
                           [
                               [180.0, 40.0], [180.0, 50.0], [170.0, 50.0],
                               [170.0, 40.0], [180.0, 40.0]
                           ]
                       ],
                       [
                           [
                               [-170.0, 40.0], [-170.0, 50.0], [-180.0, 50.0],
                               [-180.0, 40.0], [-170.0, 40.0]
                           ]
                       ]
                   ]
               }
            */

                MultiPolygon multiPolygon = new MultiPolygon(convertedCoordinates);
                multiPolygon.setSrid(srid);
                pGgeometry.setGeometry(multiPolygon);

            }

            log.debug("pGgeometry : '{}'", pGgeometry.toString());
            return pGgeometry;

        } catch (EmptyStackException et) {
            log.warn("invalid coordinates : " + et.getMessage());
            throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid coordinates", et);
        } catch (SQLException se) {
            log.warn("invalid coordinates : " + se.getMessage());
            throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid coordinates", se);
        }
    }


    /**
     * coordinates (IETF RFC7946[8] -> postgis)
     *
     * @param coordinates
     * @return
     */
    private String convertCoordinatesToPGisStr(String coordinates) {

        /*
            coordinates = [[[180.0, 40.0], [180.0, 50.0]]]  , IETF RFC7946[8]
            coordinates = (((180.0 40.0]) (180.0, 50.0)))   , postgis
        */
        try {
            coordinates = coordinates.replace(" ", "");
            coordinates = coordinates.replace("[", "(");
            coordinates = coordinates.replace("]", ")");

            char[] arr = coordinates.toCharArray();
            char[] changeArr = new char[arr.length];

            for (int j = 0; j < arr.length; j++) {
                char comma = ',';
                char ch = arr[j];
                if (j > 1) {
                    if (ch == comma && !(arr[j - 1] == '(' || arr[j - 1] == ')')) {
                        changeArr[j] = ' ';
                    } else {
                        changeArr[j] = arr[j];
                    }
                } else {
                    changeArr[j] = arr[j];
                }
            }

            String changedCoordinates = new String(changeArr);
            log.debug("changed coordinates : '{}'", changedCoordinates);

            return changedCoordinates;

        } catch (Exception e) {
            throw new NgsiLdBadRequestException(DataServiceBrokerCode.ErrorCode.INVALID_PARAMETER, "invalid coordinates", e);
        }
    }


    /**
     * timerel 요청에 대해 query 생성
     *
     * @param queryVO
     * @return
     */
    public QueryVO convertCsourceRegistrationTimerel(QueryVO queryVO) {


        String timerel = queryVO.getTimerel();
        String time = null;
        if (queryVO.getTimeAt() != null) {
            time = ConvertTimeParamUtil.dateTimeToLocalDateTime(queryVO.getTimeAt());
        }

        String endTime = null;
        if (queryVO.getEndtimeAt() != null) {
            endTime = ConvertTimeParamUtil.dateTimeToLocalDateTime(queryVO.getEndtimeAt());
        }

        ConvertTimeParamUtil.checkTimeRelParams(timerel, time, endTime);

        String colName = timerelToColName(queryVO.getTimerel());


        if (timerel.equalsIgnoreCase(DataServiceBrokerCode.TemporalOperator.BETWEEN_REL.getCode())) {
            queryVO.setTimeQuery(makeCsourceRegistrationFragmentBetweenTimeQuery(timerel, colName, time, endTime));
        } else {
            queryVO.setTimeQuery(makeCsourceRegistrationFragmentTimeQuery(timerel, colName, time));
        }

        return queryVO;
    }


    /**
     * timerel (AFTER, BEFORE ) 조건일 경우, query 생성
     *
     * @param timerel
     * @param colName
     * @param timeStr
     * @return
     */
    private String makeCsourceRegistrationFragmentTimeQuery(String timerel, String colName, String timeStr) {

        StringBuilder timeQuery = new StringBuilder();
        if (timerel.equalsIgnoreCase(DataServiceBrokerCode.TemporalOperator.AFTER_REL.getCode())) {
            timeQuery.append(colName + "_START");
            timeQuery.append(" ");
            timeQuery.append(" > ");
            timeQuery.append("'" + timeStr + "'");
        } else if (timerel.equalsIgnoreCase(DataServiceBrokerCode.TemporalOperator.BEFORE_REL.getCode())) {
            timeQuery.append(colName + "_START");
            timeQuery.append(" ");
            timeQuery.append(" < ");
            timeQuery.append("'" + timeStr + "'");
        }

        log.debug("timeQuery : '{}'", timeQuery);

        return timeQuery.toString();
    }

    /**
     * timerel (Between) 조건일 경우, query 생성
     *
     * @param timerel
     * @param colName
     * @param timeStr
     * @param endTimeStr
     * @return
     */
    private String makeCsourceRegistrationFragmentBetweenTimeQuery(String timerel, String colName, String timeStr, String endTimeStr) {
        StringBuilder timeQuery = new StringBuilder();
        timeQuery.append("(");
        timeQuery.append(colName + "_START");
        timeQuery.append(" > ");
        timeQuery.append("'" + timeStr + "'");
        timeQuery.append(" AND ");

        timeQuery.append(colName + "_END");
        timeQuery.append(" < ");
        timeQuery.append("'" + endTimeStr + "'");
        timeQuery.append(")");


        return timeQuery.toString();

    }


    private String timerelToColName(String timerel) {

        if (timerel == null) {
            return "OBSERVATION_INTERVAL";
        }

        if (timerel.equalsIgnoreCase("managementInterval")) {
            return "MANAGEMENT_INTERVAL";
        } else {
            return "OBSERVATION_INTERVAL";
        }
    }


    private String locationToColName(String georel) {
        if (georel == null) {
            return "LOCATION";
        }

        if (georel.equalsIgnoreCase("observationSpace")) {
            return "OBSERVATION_SPACE";
        } else if (georel.equalsIgnoreCase("operationSpace")) {
            return "OPERATION_SPACE";
        } else {
            return "LOCATION";
        }
    }
}
