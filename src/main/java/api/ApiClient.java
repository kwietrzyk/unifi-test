package api;

import configuration.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.config.SSLConfig.sslConfig;

public class ApiClient {

    private final RequestSpecification requestSpecification;
    private final ResponseSpecification responseSpecification;
    private String sessionCookie;

    public ApiClient() {
        RestAssured.baseURI = Config.BASE_URL;
        RestAssured.config = RestAssured.config()
                .sslConfig(sslConfig().relaxedHTTPSValidation());

        // Response Specification
        responseSpecification = RestAssured.expect()
                .statusCode(200)
                .contentType(ContentType.JSON);

        authenticate();

        // Request Specification, dodanie sesyjnego ciasteczka po autoryzacji
        requestSpecification = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("SESSIONID", sessionCookie);  // Dodanie sesyjnego ciasteczka do specyfikacji
    }

    public void postLocalAdmin(String body) {
        String endpoint = "/api/cmd/sitemgr";
        postRequest(endpoint, body);
    }

    public void postApplicationName(String appName) {
        String endpoint = "/api/set/setting/super_identity";
        String body = "{\"name\": \"" + appName + "\"}";
        postRequest(endpoint, body);
    }

    public void postCountryCode(String countryCode) {
        String endpoint = "/api/set/setting/country";
        String body = "{\"code\": \"" + countryCode + "\"}";
        postRequest(endpoint, body);
    }

    public void postTimezone(String timezone) {
        String endpoint = "/api/set/setting/locale";
        String body = "{\"timezone\": \"" + timezone + "\"}";
        postRequest(endpoint, body);
    }

    public void postConfigState(String configState) {
        String endpoint = "/api/cmd/system";
        String body = "{\"cmd\": \"" + configState + "\"}";
        postRequest(endpoint, body);
    }

    public Response getAdmin() {
        String endpoint = "/api/self";
        return getRequest(endpoint);
    }

    public Response getCountry() {
        String endpoint = "/api/s/default/get/setting/country";
        return getRequest(endpoint);
    }

    private void postRequest(String endpoint, String body) {
        given()
                .spec(requestSpecification)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpecification);
    }

    private Response getRequest(String endpoint) {
        return given()
                .spec(requestSpecification)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpecification)
                .extract()
                .response();
    }

    private void authenticate() {
        String endpoint = "/api/auth/login";

        String loginBody = """
            {
                "username": "%s",
                "password": "%s"
            }
            """.formatted(Config.USERNAME, Config.PASSWORD);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post(endpoint);

        if (response.statusCode() == 200) {
            sessionCookie = response.getCookie("SESSIONID");
        } else {
            throw new RuntimeException("Authentication failed: " + response.statusLine());
        }
    }
}