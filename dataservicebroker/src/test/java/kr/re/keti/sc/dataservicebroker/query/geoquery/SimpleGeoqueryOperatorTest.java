package kr.re.keti.sc.dataservicebroker.query.geoquery;


import java.io.IOException;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.re.keti.sc.dataservicebroker.common.TestEnvironment;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
public class SimpleGeoqueryOperatorTest {
    private static final String TEST_DATAMODEL_ID = "TestModel3";
    private static final String TEST_DATASET_ID = "TestModel3Flow";

    @Autowired
    private TestEnvironment testEnvironment;

    private void setupTestData() {
        // TODO: Geoquery 테스트 가능한 Entity 생성
    }

    @BeforeClass
    public void setup() throws IOException {
        testEnvironment.setup(TEST_DATAMODEL_ID, TEST_DATASET_ID);

        setupTestData();
    }

    @AfterClass
    public void cleanup() {
        testEnvironment.cleanup(TEST_DATAMODEL_ID, TEST_DATASET_ID);
    }
    
    @Test
    public void nearOperatorTestForPoint() {
        // Geo-Query 요청
    }
}
