package kr.re.keti.sc.dataservicebroker.csource.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.BindException;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class CsourceRegistrationControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  String csourceVO =
    "{\"context\":\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"description\":\"For TTD\",\"endpoint\":\"http://my.csource.org:1026\",\"expires\":\"9999-11-17T20:48:09,290+09:00\",\"id\":\"TDD\",\"information\":[{\"entities\":[{\"id\":\"urn:datahub:OffStreetParking:yatap_01\",\"type\":\"http://kr.citydatahub.OffStreetParking:1.0\"}],\"properties\":[[\"address\",\"streedAddress\"]],\"relationships\":[[\"inAccident\"]]}],\"location\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[[127.11132,37.393653]],\"type\":\"MultiPolygon\"}},\"managementInterval\":{},\"name\":\"string\",\"observationInterval\":{\"start\":\"2020-11-17T20:48:09,290+09:00\"},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[[127.11132,37.393653]],\"type\":\"MultiPolygon\"}},\"operationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[[127.11132,37.393653]],\"type\":\"MultiPolygon\"}},\"type\":\"ContextSourceRegistration\"}";
  String csourceVO_update =
    "{\"context\":\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"description\":\"For TTD update\",\"endpoint\":\"http://my.csource.org:1026\",\"expires\":\"9999-11-17T20:48:09,290+09:00\",\"id\":\"TDD\",\"information\":[{\"entities\":[{\"id\":\"urn:datahub:OffStreetParking:yatap_01\",\"type\":\"http://kr.citydatahub.OffStreetParking:1.0\"}],\"properties\":[[\"address\",\"streedAddress\"]],\"relationships\":[[\"inAccident\"]]}],\"location\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[[127.11132,37.393653]],\"type\":\"MultiPolygon\"}},\"managementInterval\":{},\"name\":\"string\",\"observationInterval\":{\"start\":\"2020-11-17T20:48:09,290+09:00\"},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[[127.11132,37.393653]],\"type\":\"MultiPolygon\"}},\"operationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"coordinates\":[[127.11132,37.393653]],\"type\":\"MultiPolygon\"}},\"type\":\"ContextSourceRegistration\"}";
  String query_response_compare =
    "[{\"id\":\"TDD\",\"type\":\"ContextSourceRegistration\",\"name\":\"string\",\"description\":\"For TTD\",\"information\":[{\"entities\":[{\"id\":\"urn:datahub:OffStreetParking:yatap_01\",\"type\":\"http://kr.citydatahub.OffStreetParking:1.0\"}]}],\"location\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}},\"operationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}},\"endpoint\":\"http://my.csource.org:1026\"}]";
  String retrieve_response_compare =
    "{\"id\":\"TDD\",\"type\":\"ContextSourceRegistration\",\"name\":\"string\",\"description\":\"For TTD update\",\"information\":[{\"entities\":[{\"id\":\"urn:datahub:OffStreetParking:yatap_01\",\"type\":\"http://kr.citydatahub.OffStreetParking:1.0\"}]}],\"location\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}},\"observationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}},\"operationSpace\":{\"type\":\"GeoProperty\",\"value\":{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}},\"endpoint\":\"http://my.csource.org:1026\"}";
  String error_csourceVO =
    "{\"context\":\"http://uri.citydatahub.kr/ngsi-ld/testmodel2.jsonld\",\"description\":\"For TTD\"";

  @Test
  void testRegisterContextSource() throws Exception {
    /*
     201 Created TDD
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/csourceRegistrations")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
      )
      .andExpect(status().isCreated())
      .andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    /*
     409 Already exists id Exception TDD
*/
    ResultActions resultActions2 = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/csourceRegistrations")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
      )
      .andExpect(status().isConflict())
      .andExpect(
        (
          rslt ->
            assertTrue(
              rslt
                .getResolvedException()
                .getClass()
                .isAssignableFrom(NgsiLdBadRequestException.class)
            )
        )
      )
      .andDo(print());
    /*
     400 Request parameters error exception TDD
*/
    ResultActions resultActions3 = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/csourceRegistrations")
          .content(error_csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(error_csourceVO.length()))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        (
          rslt ->
            assertTrue(
              rslt
                .getResolvedException()
                .getClass()
                .isAssignableFrom(HttpMessageNotReadableException.class)
            )
        )
      )
      .andDo(print());
  }

  @Test
  void testQueryContextSourceRegistrations() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/csourceRegistrations")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().string(query_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Query=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testUpdateContextSource() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .patch("/csourceRegistrations/TDD")
          .content(csourceVO_update)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO_update.length()))
      )
      .andExpect(status().isNoContent())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    /*
     404 Not found id Exception TDD
*/
    ResultActions resultActions2 = mvc
      .perform(
        MockMvcRequestBuilders
          .patch("/csourceRegistrations/TDD_error")
          .content(csourceVO_update)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO_update.length()))
      )
      .andExpect(status().isNotFound())
      .andExpect(
        (
          rslt ->
            assertTrue(
              rslt
                .getResolvedException()
                .getClass()
                .isAssignableFrom(NgsiLdResourceNotFoundException.class)
            )
        )
      )
      .andDo(print());
    /*
     400 Request parameters error exception TDD
*/
    ResultActions resultActions3 = mvc
      .perform(
        MockMvcRequestBuilders
          .patch("/csourceRegistrations/TDD")
          .content(error_csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(error_csourceVO.length()))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        (
          rslt ->
            assertTrue(
              rslt
                .getResolvedException()
                .getClass()
                .isAssignableFrom(HttpMessageNotReadableException.class)
            )
        )
      )
      .andDo(print());
  }

  @Test
  void testRetrieveContextSourceRegistration() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/csourceRegistrations/TDD")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().string(retrieve_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Retrieve=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    /*
     404 Not exists id Exception TDD
*/
    ResultActions resultActions2 = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/csourceRegistrations/TDD_NotFound")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(
        (
          rslt ->
            assertTrue(
              rslt
                .getResolvedException()
                .getClass()
                .isAssignableFrom(NgsiLdResourceNotFoundException.class)
            )
        )
      )
      .andDo(print());
    MvcResult mvcResult2 = resultActions2.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult2.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testDeleteContextSource() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .delete("/csourceRegistrations/TDD")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Delete=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
    /*
     404 Not exists id Exception TDD
*/
    ResultActions resultActions2 = mvc
      .perform(
        MockMvcRequestBuilders
          .delete("/csourceRegistrations/TDD_error")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNotFound())
      .andExpect(
        (
          rslt ->
            assertTrue(
              rslt
                .getResolvedException()
                .getClass()
                .isAssignableFrom(NgsiLdResourceNotFoundException.class)
            )
        )
      )
      .andDo(print());
  }
}
