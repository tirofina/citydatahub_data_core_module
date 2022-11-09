package kr.re.keti.sc.dataservicebroker.entities.controller.http;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.BindException;
import java.util.HashMap;
import kr.re.keti.sc.dataservicebroker.common.exception.ngsild.NgsiLdBadRequestException;
import kr.re.keti.sc.dataservicebroker.common.exception.ngsild.NgsiLdResourceNotFoundException;
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
public class EntityControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void testCreate() throws Exception {
    /*
     201 Created TDD
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entities")
          //.content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
        //.header("Content-Length", String.valueOf(csourceVO.length()))
      )
      .andExpect(status().isCreated())
      .andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetEntityCount() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/entitycount")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetEntity() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/entities")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetEntityById() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/entities/urn:datahub:test4:70-b3-d5-67-60-00-5c-1e_test201018")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetEntityTypes() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders.get("/types").accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetAttribute() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/attributes")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetEntityType() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders.get("/types").accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testGetAttributeInformation() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/attributes/urn:ngsi-ld:AttributeList:58208329")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testAppendEntityAttributes() throws Exception {
    String csourceVO =
      "{\"id\":\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343\",\"location\":{\"test\":\"0101000020E6100000B459F5B9DA7C5E40B6DB2E34D7B14240\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[127.11132,37.393653]}}}";
    /*
     204 No Content
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post(
            "/entities/urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343/attrs"
          )
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
          .header("Link", "<http://211.253.243.121:38081/Test3.jsonld>")
          .header("Content-Type", "application/json")
      )
      .andExpect(status().isNoContent())
      //.andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testUpdateEntityAttributes() throws Exception {
    String csourceVO =
      "{\"id\":\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343\",\"location\":{\"test\":\"0101000020E6100000B459F5B9DA7C5E40B6DB2E34D7B14240\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[127.11132,37.393653]}}}";
    /*
   204 No Content
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post(
            "/entities/urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343/attrs"
          )
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
          .header("Link", "<http://211.253.243.121:38081/Test3.jsonld>")
          .header("Content-Type", "application/json")
      )
      .andExpect(status().isNoContent())
      //.andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testPartialUpdateWithAttrId() throws Exception {
    String csourceVO =
      "{\"id\":\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343\",\"location\":{\"test\":\"0101000020E6100000B459F5B9DA7C5E40B6DB2E34D7B14240\",\"type\":\"GeoProperty\",\"value\":{\"type\":\"Point\",\"coordinates\":[127.11132,37.393653]}}}";
    /*
   204 No Content
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .patch(
            "/entities/urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343/attrs/location"
          )
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
          .header("Link", "<http://211.253.243.121:38081/Test3.jsonld>")
          .header("Content-Type", "application/json")
      )
      .andExpect(status().isNoContent())
      //.andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testDeleteAttr() {}

  @Test
  void testDelete() throws Exception {
    /*
 204 No Content
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .delete(
            "/entities/urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test23431"
          )
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
      )
      .andExpect(status().isOk())
      //.andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testBatchEntityCreation() throws Exception {
    String csourceVO =
      "[{\"id\":\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\",\"type\":\"http://211.253.243.121/context/Test3#Test3\",\"datasetId\":\"Test3\",\"location\":{\"test\":\"0101000020E6100000B459F5B9DA7C5E40B6DB2E34D7B14240\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[127.11132,37.393653],\"type\":\"Point\"}}}]";
    /*
   204 No Content
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entityOperations/create")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
          .header("Link", "<http://211.253.243.121:38081/Test3.jsonld>")
          .header("Content-Type", "application/json")
      )
      .andExpect(status().isCreated())
      .andExpect(
        content()
          .string("[\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\"]")
      )
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testBatchEntityUpdate() throws Exception {
    String csourceVO =
      "[{\"id\":\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\",\"type\":\"http://211.253.243.121/context/Test3#Test3\",\"datasetId\":\"Test3\",\"location\":{\"test\":\"0101000020E6100000B459F5B9DA7C5E40B6DB2E34D7B14240\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[127.11132,37.393653],\"type\":\"Point\"}}}]";
    /*
 204 No Content
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entityOperations/update")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
          .header("Link", "<http://211.253.243.121:38081/Test3.jsonld>")
          .header("Content-Type", "application/json")
      )
      .andExpect(status().isNoContent())
      //.andExpect(content().string("[\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\"]"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testBatchEntityUpsert() throws Exception {
    String csourceVO =
      "[{\"id\":\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\",\"type\":\"http://211.253.243.121/context/Test3#Test3\",\"datasetId\":\"Test3\",\"location\":{\"test\":\"0101000020E6100000B459F5B9DA7C5E40B6DB2E34D7B14240\",\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[127.11132,37.393653],\"type\":\"Point\"}}}]";
    /*
    204 No Content
  */
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entityOperations/upsert")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
          .header("Link", "<http://211.253.243.121:38081/Test3.jsonld>")
          .header("Content-Type", "application/json")
      )
      .andExpect(status().isNoContent())
      //.andExpect(content().string("[\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\"]"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testBatchEntityDelete() throws Exception {
    String body = "[\"urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_testDongjin\"]";
    /*
    204 No Content
   */
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/entityOperations/delete")
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(body.length()))
      )
      .andExpect(status().isNoContent())
      //.andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }
}
