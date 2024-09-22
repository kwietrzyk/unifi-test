package api;

import com.google.gson.Gson;
import configuration.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.config.SSLConfig.sslConfig;

public class ApiClient {

    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private String sessionCookie;
    private String csrfToken;

    public ApiClient() {
        RestAssured.baseURI = Config.BASE_URL;
        RestAssured.config = RestAssured.config()
                .sslConfig(sslConfig().relaxedHTTPSValidation());

        responseSpecification = RestAssured.expect()
                .statusCode(200)
                .contentType(ContentType.JSON);

        requestSpecification = given()
                .contentType(ContentType.JSON);
    }

    public void postLocalAdmin(LocalAdminDto body) {
        String endpoint = "/api/cmd/sitemgr";
        Gson gson = new Gson();
        String jsonBody = gson.toJson(body);
        postRequest(endpoint, jsonBody);
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
        authenticate();
        return given()
                .spec(requestSpecification)
                .cookie("unifises", sessionCookie)
                .header("X-CSRF-Token", csrfToken)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpecification)
                .extract()
                .response();
    }

    private void authenticate() {
        String endpoint = Config.BASE_URL + "api/login";

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
        response.then().log().all();

        if (response.statusCode() == 200) {
            sessionCookie = response.getCookie("unifises");
            csrfToken = response.getCookie("csrf_token");
        } else {
            throw new RuntimeException("Authentication failed: " + response.statusLine());
        }
    }

    public void logOut() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("unifises", sessionCookie)
                .header("X-CSRF-Token", csrfToken)
                .post(Config.BASE_URL + "api/logout");

        if (response.getStatusCode() == 200) {
            System.out.println("User is logged out");
        } else {
            System.err.println("Logout failed with status code: " + response.getStatusCode());
        }
    }
}