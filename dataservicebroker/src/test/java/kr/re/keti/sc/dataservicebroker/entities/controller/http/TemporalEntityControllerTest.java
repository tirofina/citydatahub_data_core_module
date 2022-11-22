package kr.re.keti.sc.dataservicebroker.entities.controller.http;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class TemporalEntityControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void testGetEntityCount() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/temporal/entitycount")
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
          .get("/temporal/entities")
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
          .get(
            "/temporal/entities/urn:datahub:test4:70-b3-d5-67-60-00-5c-1e_test2838"
          )
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
  void testGetEntityCountById() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get(
            "/temporal/entitycount/urn:datahub:Test3:70-b3-d5-67-60-00-5c-1e_test2343"
          )
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
  void testAddAttributesToTemporalRepresentationOfAnEntity() {}

  @Test
  void testCreatOrUpdateTemporalRepresentationOfEntities() {}

  @Test
  void testDeleteAttributeFromTemporalRepresentationOfAnEntity() {}

  @Test
  void testDeleteAttributeInstanceFromTemporalRepresentationOfAnEntity() {}

  @Test
  void testDeleteTemporalRepresentaionOfAndEntity() {}

  @Test
  void testModifyAttributeInstanceInTemporalRepresentaionOfAnEntity() {}
}
