package kr.re.keti.sc.dataservicebroker.TestModel3;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.BindException;
import java.util.HashMap;
// import kr.re.keti.sc.dataservicebroker.common.exception.ngsild.NgsiLdBadRequestException;
// import kr.re.keti.sc.dataservicebroker.common.exception.ngsild.NgsiLdResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.*;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class TestModel3CRUDTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void testCreateCR_BV_01() throws Exception {
    /*
     201 Created TDD
*/

    String inputVO =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.14,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.9,0.8]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.12},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.13},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputVO)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputVO.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCR_BV_02() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCR_BV_03() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCR_BI_01() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":[false,true],\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]}},\"testDoubleArray\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"objects\":{\"type\":\"Property\",\"value\":{\"interger\":5,\"boolean\":true,\"string\":\"test\",\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1}},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]},\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testString\":{\"type\":\"Property\",\"value\":\"test\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isBadRequest())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testCreateCR_BI_02() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":\"invalid-string\",\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":\"invalid-string\"},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]}},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"objects\":{\"type\":\"Property\",\"value\":{\"interger\":5,\"boolean\":true,\"string\":\"test\",\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1}},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]},\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testString\":{\"type\":\"Property\",\"value\":\"test\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isBadRequest())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testCreateCR_BI_03() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":[false,true],\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":\"invalid-string\"},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]}},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"objects\":{\"type\":\"Property\",\"value\":{\"interger\":5,\"boolean\":true,\"string\":\"test\",\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1}},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]},\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testString\":{\"type\":\"Property\",\"value\":\"test\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isBadRequest())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testCreateCA_BV_01() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    /*
     204 No Content
    */
    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":[false,true]}}";
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .post(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCA_BV_02() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    /*
     204 No Content
    */
    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":[false,true]}}";
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .post(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCA_BV_03() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    /*
     204 No Content
    */
    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":[false,true]}}";
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .post(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCA_BI_01() throws Exception {
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    /*
     201 Created TDD
*/
    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":\"invalid-string\",\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]}},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"objects\":{\"type\":\"Property\",\"value\":{\"interger\":5,\"boolean\":true,\"string\":\"test\",\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1}},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-5]},\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testString\":{\"type\":\"Property\",\"value\":\"test\"},\"type\":\"TestModel3\"}";

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .post(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isBadRequest())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCA_BI_02() throws Exception {
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    /*
     201 Created TDD
*/
    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":\"invalid-string\",\"observedAt\":\"2023-06-18T15:00:00.000Z\"}}";

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .post(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isBadRequest())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCA_BI_03() throws Exception {
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":\"invalid-string\"},\"type\":\"TestModel3\"}";

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .post(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isBadRequest())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCU_BV_01() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":[true]},\"type\":\"TestModel3\"}";
    /*
       204 No Content
    */
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .patch(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCU_BV_02() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-6]}},\"type\":\"TestModel3\"}";
    /*
       204 No Content
    */
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .patch(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCU_BV_03() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[-12,-6]}},\"type\":\"TestModel3\"}";
    /*
       204 No Content
    */
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .patch(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCU_BI_01() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":\"invalid-String\"},\"type\":\"TestModel3\"}";
    /*
       204 No Content
    */
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .patch(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isBadRequest())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCU_BI_02() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"testArrayBoolean\":{\"type\":\"Property\",\"value\":\"invalid-Value\",\"observedAt\":\"2023-06-18T15:00:00.000Z\"},\"type\":\"TestModel3\"}";
    /*
       204 No Content
    */
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .patch(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isBadRequest())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void testCreateCU_BI_03() throws Exception {
    /*
     201 Created TDD
*/
    String inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"id\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e\",\"location\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"objects\":{\"type\":\"Property\",\"value\":{\"boolean\":true,\"date\":\"2023-06-18T15:00:00.000Z\",\"double\":0.1,\"interger\":5,\"string\":\"test\"}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testArrayBoolean\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"Property\",\"value\":[false,true]},\"testArrayDouble\":{\"type\":\"Property\",\"value\":[0.0,1.1]},\"testArrayInteger\":{\"type\":\"Property\",\"value\":[1,2]},\"testArrayObject\":{\"type\":\"Property\",\"value\":[{\"testArrObjString\":\"string\"},{\"testArrObjInteger\":10},{\"testArrObjDouble\":0.1},{\"testArrObjBoolean\":true},{\"testArrObjDate\":\"2023-11-17T20:48:09,290+09:00\"}]},\"testArrayString\":{\"type\":\"Property\",\"value\":[\"test1\",\"test2\"]},\"testBoolean\":{\"type\":\"Property\",\"value\":true},\"testDate\":{\"type\":\"Property\",\"value\":\"2023-11-17T20:48:09,290+09:00\"},\"testDouble\":{\"type\":\"Property\",\"value\":0.1},\"testGeoJson\":{\"observedAt\":\"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[-12,-5],\"type\":\"Point\"}},\"testInteger\":{\"type\":\"Property\",\"value\":10},\"testRelationshipString\":{\"object\":\"urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e1\",\"type\":\"Relationship\"},\"testString\":{\"type\":\"Property\",\"value\":\"valuestring\"},\"type\":\"TestModel3\"}";

    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          .content(inputData)
          .contentType("application/ld+json")
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(inputData.length()))
      )
      .andExpect(status().isCreated())
      .andDo(print());

    inputData =
      "{\"@context\":[\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld\"],\"type\":\"TestModel3\",\"datasetId\":\"TestModel3\",\"location\":{\"observedAt\": \"2023-06-18T15:00:00.000Z\",\"type\":\"GeoProperty\",\"value\":\"invalid-String\"},\"type\":\"TestModel3\"}";
    /*
       204 No Content
    */
    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .patch(
              "/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e/attrs"
            )
            .content(inputData)
            .contentType("application/ld+json")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Content-Length", String.valueOf(inputData.length()))
        )
        .andExpect(status().isBadRequest())
        .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

    resultActions =
      mvc
        .perform(
          MockMvcRequestBuilders
            .delete("/entities/urn:datahub:TestModel3:70-b3-d5-67-60-00-5c-1e")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}
