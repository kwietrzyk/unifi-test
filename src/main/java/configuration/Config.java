package configuration;

import api.ApiClient;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String CONFIG_FILE_PATH = "src/main/resources/configuration.properties";
    private static final Properties PROPERTIES = new Properties();

    public static final String SCRIPT_PATH;
    public static final String BASE_URL;
    public static final String USERNAME;
    public static final String EMAIL;
    public static final String PASSWORD;

    static {
        try {
            PROPERTIES.load(new FileInputStream(CONFIG_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SCRIPT_PATH = PROPERTIES.getProperty("script.path");
        BASE_URL = PROPERTIES.getProperty("app.url");
        USERNAME = PROPERTIES.getProperty("username");
        EMAIL = PROPERTIES.getProperty("email");
        PASSWORD = PROPERTIES.getProperty("password");
    }
}
