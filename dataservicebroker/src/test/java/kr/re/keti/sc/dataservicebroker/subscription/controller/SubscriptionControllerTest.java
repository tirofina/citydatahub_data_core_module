package kr.re.keti.sc.dataservicebroker.subscription.controller;

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
public class SubscriptionControllerTest {

  @Autowired
  private MockMvc mvc;

  String csourceVO =
    "{\"csf\":\"string\",\"datasetIds\":[\"Test3\"],\"description\":\"string\",\"entities\":[{\"id\":\"urn:datahub:OffStreetParking:yatap_01\",\"type\":\"http://211.253.243.121/context/Test3#Test3\"}],\"expires\":\"9999-11-15T20:10:00.000+09:00\",\"geoQ\":{\"georel\":\"near;maxDistance==2000\"},\"id\":\"urn:ngsi-ld:CsourceRegistrationSubscription:myCsourceRegistrationSubscriptionTDD\",\"isActive\":true,\"name\":\"string\",\"notification\":{\"attributes\":[\"http://speed\"],\"endpoint\":{\"accept\":\"application/json\",\"uri\":\"http://my.endpoint.org/notify\"},\"format\":\"normalized\"},\"q\":\"string\",\"temporalQ\":{\"endTime\":\"9999-11-15T20:10:00.000+09:00\",\"time\":\"9999-11-15T20:10:00.000+09:00\",\"timeproperty\":\"String\",\"timerel\":\"after\"},\"throttling\":1,\"type\":\"Subscription\",\"watchedAttributes\":[\"http://address\"]}";
  String csourceVO_update =
    "{\"csf\":\"string\",\"datasetIds\":[\"Test3\"],\"description\":\"stringUpdate\",\"entities\":[{\"id\":\"urn:datahub:OffStreetParking:yatap_01\",\"type\":\"http://211.253.243.121/context/Test3#Test3\"}],\"expires\":\"9999-11-15T20:10:00.000+09:00\",\"geoQ\":{\"georel\":\"near;maxDistance==2000\"},\"id\":\"urn:ngsi-ld:CsourceRegistrationSubscription:myCsourceRegistrationSubscriptionTDD\",\"isActive\":true,\"name\":\"string\",\"notification\":{\"attributes\":[\"http://speed\"],\"endpoint\":{\"accept\":\"application/json\",\"uri\":\"http://my.endpoint.org/notify\"},\"format\":\"normalized\"},\"q\":\"string\",\"temporalQ\":{\"endTime\":\"9999-11-15T20:10:00.000+09:00\",\"time\":\"9999-11-15T20:10:00.000+09:00\",\"timeproperty\":\"String\",\"timerel\":\"after\"},\"throttling\":1,\"type\":\"Subscription\",\"watchedAttributes\":[\"http://address\"]}";

  @Test
  void testCreateSubscription() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/subscriptions")
          .content(csourceVO)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO.length()))
      )
      .andExpect(status().isCreated())
      .andExpect(
        content()
          .string(
            "{\"id\":\"urn:ngsi-ld:CsourceRegistrationSubscription:myCsourceRegistrationSubscriptionTDD\"}"
          )
      )
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testQuerySubscriptions() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get("/subscriptions")
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
  void testRetrieveSubscription() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .get(
            "/subscriptions/urn:ngsi-ld:CsourceRegistrationSubscription:myCsourceRegistrationSubscriptionTDD"
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
  void testUpdateSubscription() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .patch(
            "/subscriptions/urn:ngsi-ld:CsourceRegistrationSubscription:myCsourceRegistrationSubscriptionTDD"
          )
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
  }

  @Test
  void testDelete() throws Exception {
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .delete(
            "/subscriptions/urn:ngsi-ld:CsourceRegistrationSubscription:myCsourceRegistrationSubscriptionTDD"
          )
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent())
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Delete=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }
}
