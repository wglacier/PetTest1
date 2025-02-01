package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PetStoreApiTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    public void getPetByIdTest() {
        // Endpoint to fetch pet details by ID
        int petId = 2; // Example Pet ID

        // Make a GET request to the API
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/pet/{petId}")
                .pathParam("petId", petId)
                .when()
                .get();

        // Assert the response status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");

        // Assert the response body contains expected pet details (for example, pet ID)
        Assert.assertTrue(response.getBody().asString().contains("\"id\":" + petId), "Pet ID should be " + petId);
    }

    @Test
    public void addPetTest() {
        // Endpoint to add a new pet
        String petJson = """
                {
                   "id": 1,
                   "category": {
                     "id": 1,
                     "name": "string"
                   },
                   "name": "doggie",
                   "photoUrls": [
                     "string"
                   ],
                   "tags": [
                     {
                       "id": 1,
                       "name": "string"
                     }
                   ],
                   "status": "available"
                 }
                 
  """;

        // Make a POST request to add a new pet
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType("application/json")
                .body(petJson)
                .when()
                .post();

        // Assert the response status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");

        // Assert that the pet's name in the response is "Doggie"
        Assert.assertTrue(response.getBody().asString().contains("\"name\":\"doggie\""), "The pet name should be Doggie");
    }

    @Test
    public void deletePetTest() {
        // Endpoint to delete a pet
        int petId = 10; // Example Pet ID to delete

        // Make a DELETE request to the API
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/pet/{petId}")
                .pathParam("petId", petId)
                .when()
                .delete();

        // Assert the response status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");

        // Verify the pet has been deleted (attempt to fetch it again)
        Response fetchResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/pet/{petId}")
                .pathParam("petId", petId)
                .when()
                .get();

        Assert.assertEquals(fetchResponse.getStatusCode(), 404, "Expected status code for non-existent pet is 404");
    }
}