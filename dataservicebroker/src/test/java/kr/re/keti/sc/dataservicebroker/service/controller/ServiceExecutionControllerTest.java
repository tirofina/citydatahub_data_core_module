package kr.re.keti.sc.dataservicebroker.service.controller;

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
public class ServiceExecutionControllerTest {

  @Autowired
  private MockMvc mvc;

  String csourceVO =
    "{\"id\":\"testid\",\"serviceRegistrationId\":\"testid\",\"type\":\"ServiceRegistration\",\"executions\":[{\"id\":\"testid\",\"name\":\"nametest\",\"type\":\"typetest\",\"entityId\":\"urn:datahub:JangLog:5002690:2022-10-11T18:30:00Z\",\"entityType\":\"typetest\",\"input\":{\"type\":\"inputtypetest\",\"attribs\":[{\"attribname\":\"test\",\"datatype\":\"test\"}]}}]}";

  @Test
  void testServiceExecution() throws Exception {
    /*
     200 Created TDD
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/service/execute")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
      )
      .andExpect(status().isOk())
      .andExpect(content().string("{\"id\":\"testid\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testRetrieveServiceExecutionStatus() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/service/execute/testid")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      //.andExpect(content().string(retrieve_response_compare))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Retrieve=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }
}
