package kr.re.keti.sc.dataservicebroker.provisioning.controller;

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
public class ProvisioningControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  String csourceVO_datamodel =
    "{\"data\":\"{\\\"attributes\\\":[{\\\"name\\\":\\\"wlobscd\\\",\\\"isRequired\\\":false,\\\"valueType\\\":\\\"String\\\",\\\"attributeType\\\":\\\"Property\\\",\\\"hasObservedAt\\\":false,\\\"hasUnitCode\\\":false,\\\"attributeUri\\\":\\\"http://110.45.181.71/context/JangLog#wlobscd\\\"},{\\\"name\\\":\\\"ymdhm\\\",\\\"isRequired\\\":false,\\\"valueType\\\":\\\"Date\\\",\\\"attributeType\\\":\\\"Property\\\",\\\"hasObservedAt\\\":false,\\\"hasUnitCode\\\":false,\\\"attributeUri\\\":\\\"http://110.45.181.71/context/JangLog#ymdhm\\\"},{\\\"name\\\":\\\"wl\\\",\\\"isRequired\\\":false,\\\"valueType\\\":\\\"Double\\\",\\\"attributeType\\\":\\\"Property\\\",\\\"hasObservedAt\\\":false,\\\"hasUnitCode\\\":false,\\\"attributeUri\\\":\\\"http://110.45.181.71/context/JangLog#wl\\\"},{\\\"name\\\":\\\"fw\\\",\\\"isRequired\\\":false,\\\"valueType\\\":\\\"Double\\\",\\\"attributeType\\\":\\\"Property\\\",\\\"hasObservedAt\\\":false,\\\"hasUnitCode\\\":false,\\\"attributeUri\\\":\\\"http://110.45.181.71/context/JangLog#fw\\\"}],\\\"context\\\":[\\\"http://110.45.181.71:38081/JangLog.jsonld\\\"],\\\"description\\\":\\\"string\\\",\\\"id\\\":\\\"JangLog\\\",\\\"name\\\":\\\"string\\\",\\\"type\\\":\\\"JangLog\\\",\\\"typeUri\\\":\\\"http://110.45.181.71/context/JangLog#JangLog\\\"}\",\"eventTime\":\"9999-11-15T20:10:00,000+09:00\",\"eventType\":\"created\",\"requestId\":\"string\",\"to\":\"/datamodels\"}";
  String csourceVO_dataset =
    "{\"data\":\"{\\\"id\\\":\\\"JangLogSet\\\",\\\"name\\\":\\\"JangLogSet\\\",\\\"metasetId\\\":\\\"metaW\\\",\\\"classification\\\":\\\"CITY\\\",\\\"ownership\\\":\\\"ADMIN\\\",\\\"qualityCheckEnabled\\\":true,\\\"createdAt\\\":\\\"2022-10-11T16:53:49.711+09:00\\\"}\",\"eventTime\":\"9999-11-15T20:10:00,000+09:00\",\"eventType\":\"created\",\"requestId\":\"string\",\"to\":\"/datamodels/JangLog\"}";
  String csourceVO_datasetflow =
    "{\"data\":\"{\\\"datasetId\\\":\\\"janglok\\\",\\\"description\\\":\\\"description\\\",\\\"historyStoreType\\\":\\\"none\\\",\\\"enabled\\\":true,\\\"bigDataStorageTypes\\\":[\\\"rdb\\\"]}\",\"eventTime\":\"9999-11-15T20:10:00,000+09:00\",\"eventType\":\"created\",\"requestId\":\"string\",\"to\":\"/datasets/weatherDataSet/flow\"}";

  @Test
  void testProvisionDataModels() throws Exception {
    /*
     201 Created TDD
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/provision/datamodels")
          .content(csourceVO_datamodel)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header(
            "Content-Length",
            String.valueOf(csourceVO_datamodel.length())
          )
      )
      .andExpect(status().isCreated())
      //  .andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testProvisionDatasets() throws Exception {
    /*
     201 Created TDD
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/provision/datasets")
          .content(csourceVO_dataset)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header("Content-Length", String.valueOf(csourceVO_dataset.length()))
      )
      .andExpect(status().isCreated())
      //  .andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testProvisionDatasetFlows() throws Exception {
    /*
    201 Created TDD
*/
    ResultActions resultActions = mvc
      .perform(
        MockMvcRequestBuilders
          .post("/provision/datasetflows")
          .content(csourceVO_datasetflow)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .header(
            "Content-Length",
            String.valueOf(csourceVO_datasetflow.length())
          )
      )
      .andExpect(status().isCreated())
      //  .andExpect(content().string("{\"id\":\"TDD\"}"))
      .andDo(print());

    MvcResult mvcResult = resultActions.andReturn();
    System.out.println("=====================Post=====================");
    System.out.println(mvcResult.getResponse().getContentAsString());
    System.out.println("=====================End=====================");
  }

  @Test
  void testProvisionAclRule() {}
}
