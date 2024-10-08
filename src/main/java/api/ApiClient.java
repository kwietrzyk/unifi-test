package api;

import com.google.gson.Gson;
import configuration.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.config.SSLConfig.sslConfig;

public class ApiClient {

    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private String sessionCookie;
    private String csrfToken;
    private boolean isLoggedIn = false;

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

    public Response postLocalAdmin(LocalAdminDto body) {
        String endpoint = "/api/cmd/sitemgr";
        Gson gson = new Gson();
        String jsonBody = gson.toJson(body);
        return postRequest(endpoint, jsonBody);
    }

    public Response postApplicationName(String appName) {
        String endpoint = "/api/set/setting/super_identity";
        String body = "{\"name\": \"" + appName + "\"}";
        return postRequest(endpoint, body);
    }

    public Response postCountryCode(String countryCode) {
        String endpoint = "/api/set/setting/country";
        String body = "{\"code\": \"" + countryCode + "\"}";
        return postRequest(endpoint, body);
    }

    public Response postTimezone(String timezone) {
        String endpoint = "/api/set/setting/locale";
        String body = "{\"timezone\": \"" + timezone + "\"}";
        return postRequest(endpoint, body);
    }

    public Response postConfigState(String configState) {
        String endpoint = "/api/cmd/system";
        String body = "{\"cmd\": \"" + configState + "\"}";
        return postRequest(endpoint, body);
    }

    public Response getAdmin() {
        String endpoint = "/api/self";
        return getRequest(endpoint);
    }

    public Response getCountry() {
        String endpoint = "/api/s/default/get/setting/country";
        return getRequest(endpoint);
    }

    private Response postRequest(String endpoint, String body) {
        return given()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    private Response getRequest(String endpoint) {
        return given()
                .cookie("unifises", sessionCookie)
                .header("X-CSRF-Token", csrfToken)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public void authenticate(String username, String password) {
        String endpoint = Config.BASE_URL + "api/login";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(new AuthenticationDto(username, password))
                .post(endpoint);

        if (response.statusCode() == 200) {
            sessionCookie = response.getCookie("unifises");
            csrfToken = response.getCookie("csrf_token");
            isLoggedIn = true;
        } else {
            throw new RuntimeException("Authentication failed: " + response.statusLine());
        }
    }

    public void logOut() {
        if (!isLoggedIn) {
            System.out.println("User is not logged in");
            return;
        }
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("unifises", sessionCookie)
                .header("X-CSRF-Token", csrfToken)
                .post(Config.BASE_URL + "api/logout");

        if (response.getStatusCode() == 200) {
            System.out.println("User is logged out");
            isLoggedIn = false;
        } else {
            System.err.println("Logout failed with status code: " + response.getStatusCode());
        }
    }
}