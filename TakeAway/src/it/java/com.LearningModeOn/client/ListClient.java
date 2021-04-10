package com.LearningModeOn.client;

import com.LearningModeOn.response.ItemResponse;
import com.LearningModeOn.response.ListResponse;
import com.LearningModeOn.util.Constants;
import com.LearningModeOn.util.TestHelper;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@Slf4j
public class ListClient {

    public ListResponse createMovieList(String uri, String payload, String accessToken) {
        Response response = given()
                .baseUri(uri)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .body(payload)
                .when()
                .post(Constants.THEMOVIEDB_LIST_BASE_PATH);
        ListResponse apiResponse = null;
        try {
            apiResponse = (ListResponse) TestHelper.getResponseObject(response.asString(), ListResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ListResponse getMovieList(Integer listId, String accessToken, String apikey) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .queryParam("api_key", apikey)
                .when()
                .get(Constants.THEMOVIEDB_LIST_ID_PATH_PARAM);
        ListResponse apiResponse = null;
        try {
            apiResponse = (ListResponse) TestHelper.getResponseObject(response.asString(), ListResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ListResponse updateMovieList(Integer listId, String payload, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .body(payload)
                .when()
                .put(Constants.THEMOVIEDB_LIST_ID_PATH_PARAM);
        ListResponse apiResponse = null;
        try {
            apiResponse = (ListResponse) TestHelper.getResponseObject(response.asString(), ListResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ListResponse deleteMovieList(Integer listId, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .when()
                .delete(Constants.THEMOVIEDB_LIST_ID_PATH_PARAM);
        ListResponse apiResponse = null;
        try {
            apiResponse = (ListResponse) TestHelper.getResponseObject(response.asString(), ListResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ItemResponse addItemsToList(Integer listId, String payload, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .body(payload)
                .when()
                .post(Constants.LIST_ADD_ITEM_BASE_PATH);
        ItemResponse apiResponse = null;
        try {
            apiResponse = (ItemResponse) TestHelper.getResponseObject(response.asString(), ItemResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ItemResponse updateItemsInList(Integer listId, String payload, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .body(payload)
                .when()
                .put(Constants.LIST_ADD_ITEM_BASE_PATH);
        ItemResponse apiResponse = null;
        try {
            apiResponse = (ItemResponse) TestHelper.getResponseObject(response.asString(), ItemResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ItemResponse removeItemFromList(Integer listId, String payload, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .body(payload)
                .when()
                .delete(Constants.LIST_ADD_ITEM_BASE_PATH);
        ItemResponse apiResponse = null;
        try {
            apiResponse = (ItemResponse) TestHelper.getResponseObject(response.asString(), ItemResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;
    }

    public ListResponse clearMovieList(Integer listId, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .when()
                .get(Constants.THEMOVIEDB_CLEAR_LIST_BASE_PATH);
        ListResponse apiResponse = null;
        try {
            apiResponse = (ListResponse) TestHelper.getResponseObject(response.asString(), ListResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;

    }

    public ListResponse checkItemStatus(Integer listId, Integer media_id, String media_type, String accessToken) {
        Response response = given()
                .baseUri(Constants.THEMOVIEDB_HOST)
                .basePath(Constants.THEMOVIEDB_LIST_BASE_PATH)
                .header("content-type", "application/json;charset=utf-8")
                .header("authorization", "Bearer " + accessToken)
                .pathParam("list_id", listId)
                .queryParam("media_id", media_id)
                .queryParam("media_type", media_type)
                .when()
                .get(Constants.LIST_CHECK_ITEM_STATUS_BASE_PATH);
        ListResponse apiResponse = null;
        try {
            apiResponse = (ListResponse) TestHelper.getResponseObject(response.asString(), ListResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        apiResponse.setHttpStatusCode(response.statusCode());
        return apiResponse;

    }
}
