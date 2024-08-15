package org.Test;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class AppTest {

    private WireMockServer wireMockServer;

    @BeforeClass
    public void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        String responseBody = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": \"12345\",\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"email\": \"johndoe@example.com\",\n" +
                "    \"address\": {\n" +
                "      \"street\": \"123 Elm Street\",\n" +
                "      \"city\": \"Somewhere\",\n" +
                "      \"state\": \"CA\",\n" +
                "      \"postalCode\": \"90210\"\n" +
                "    },\n" +
                "    \"phoneNumbers\": [\n" +
                "      {\n" +
                "        \"type\": \"home\",\n" +
                "        \"number\": \"+1-555-1234\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"mobile\",\n" +
                "        \"number\": \"+1-555-5678\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"preferences\": {\n" +
                "      \"newsletter\": true,\n" +
                "      \"notifications\": {\n" +
                "        \"email\": true,\n" +
                "        \"sms\": false\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        // Set up the WireMock stub to match the POST request with query parameter and body
        wireMockServer.stubFor(post(urlPathEqualTo("/baledung"))  // Matches the URL path without query parameters
                .withQueryParam("id", equalTo("10"))              // Matches the query parameter
                .withRequestBody(equalTo("{}"))                   // Matches the exact body content
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withBody(responseBody)
                        .withHeader("Accept", "application/json")
                        .withStatus(200)));
    }

    @Test(testName = "sampleTest")
    public void sampleTest() {
        String expectedResponse = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": \"12345\",\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"email\": \"johndoe@example.com\",\n" +
                "    \"address\": {\n" +
                "      \"street\": \"123 Elm Street\",\n" +
                "      \"city\": \"Somewhere\",\n" +
                "      \"state\": \"CA\",\n" +
                "      \"postalCode\": \"90210\"\n" +
                "    },\n" +
                "    \"phoneNumbers\": [\n" +
                "      {\n" +
                "        \"type\": \"home\",\n" +
                "        \"number\": \"+1-555-1234\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"mobile\",\n" +
                "        \"number\": \"+1-555-5678\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"preferences\": {\n" +
                "      \"newsletter\": true,\n" +
                "      \"notifications\": {\n" +
                "        \"email\": true,\n" +
                "        \"sms\": false\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String actualResponse = given()
                .header("Content-Type", "application/json")
                .queryParam("id", 10)
                .body("{}")
                .post("http://localhost:8080/baledung")
                .then()
                .extract()
                .response()
                .asString();

        Assert.assertEquals(actualResponse.trim(), expectedResponse.trim(), "The response does not match the expected response.");
    }

    @Test(testName = "skippedtest",description="test case got skipped")
    public void test()
    {
        throw new SkipException("test case is skipped");
    }
    @AfterClass
    public void teardown() {
        wireMockServer.stop();
    }
}
