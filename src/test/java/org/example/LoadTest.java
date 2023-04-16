package org.example;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Unit test for simple App.
 */
public class LoadTest {

    private static final int NUM_REQUESTS = 100;
    private static final int NUM_THREADS = 10;

    @Test
    public void simpleLoadTest() throws InterruptedException {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Create a thread pool to simulate concurrent users
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        // Submit multiple requests to the thread pool
        for (int i = 0; i < NUM_REQUESTS; i++) {
            executorService.submit(() -> {
                given()
                        .when()
                        .get("/posts/1")
                        .then()
                        .statusCode(200)
                        .body("userId", equalTo(1))
                        .body("id", equalTo(1));
            });
        }

        // Wait for all requests to complete
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

}
