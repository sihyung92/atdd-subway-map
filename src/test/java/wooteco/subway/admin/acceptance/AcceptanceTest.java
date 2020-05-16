package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    LineResponse getLine(Long id) {
        return given().
                when().
                get("/lines/" + id).
                then().
                log().all().
                extract().as(LineResponse.class);
    }

    void createLine(String name) {
        LineRequest lineRequest = new LineRequest(
                name,
                "bg-red-500",
                LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME),
                LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME),
                10);

        given().
                body(lineRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    void updateLine(Long id, String name, String color, String startTime, String endTime, int intervalTime) {
        LineRequest lineRequest = new LineRequest(
                name,
                color,
                startTime,
                endTime,
                intervalTime
        );

        given().
                body(lineRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("/lines/" + id).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    List<LineResponse> getLines() {
        return
                given().
                        when().
                        get("/lines").
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", LineResponse.class);
    }

    void deleteLine(Long id) {
        given().
                when().
                delete("/lines/" + id).
                then().
                log().all();
    }

    void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/stations").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    List<StationResponse> getStations() {
        return given().
                when().
                get("/stations").
                then().
                log().all().
                extract().
                jsonPath().getList(".", StationResponse.class);
    }

    void deleteStation(Long id) {
        given().
                when().
                delete("/stations/" + id).
                then().
                log().all();
    }
}