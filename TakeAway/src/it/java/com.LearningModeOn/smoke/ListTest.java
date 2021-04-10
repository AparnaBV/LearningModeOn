package com.LearningModeOn.smoke;

import com.LearningModeOn.base.SmokeBaseTest;
import com.LearningModeOn.request.MovieList;
import com.LearningModeOn.response.ItemResponse;
import com.LearningModeOn.response.ListResponse;
import com.LearningModeOn.util.Status;
import com.LearningModeOn.util.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

@Slf4j
public class ListTest extends SmokeBaseTest {

    @Test (dataProvider = "getInvalidMovieListData")
    public void createInvalidListTest(String fileName, String errorMessage)
            throws Exception {
        String testPayLoadData = TestHelper.readFileAsString(TestHelper.getTestDataFilePath(fileName));
        ListResponse response = createMovieList(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("ERROR_MISSING_REQUIRED_DATA").getCode());
        Assert.assertNotNull(response.getErrors());
        response.getErrors().forEach(error -> {
            Assert.assertEquals(error, errorMessage);
        });
        response.assertErrorData();
    }

    @Test
    public void createListInvalidTokenTest()
            throws Exception {
        String testPayLoadData = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidList.json"));
        ListResponse response = createMovieListInvalidToken(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("ERROR_ACCESS").getCode());
        response.assertErrorData();
        Assert.assertEquals(response.getStatus_message(), Status.valueOf("ERROR_ACCESS").getMessage());
        int status_code = response.getStatus_code();
        Assert.assertEquals(status_code, 36);
    }

    @Test (dataProvider = "getValidMovieListData")
    public void createValidListTest(String fileName)
            throws Exception {
        String testPayLoadData = TestHelper.readFileAsString(TestHelper.getTestDataFilePath(fileName));
        ListResponse response = createMovieList(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());
        //Fetch ListId from response
        log.info("List Id {} created successfully ", response.getId());
        Integer listId = response.getId();
        ListIds.add(listId);
        response.assertSuccessData();
        Assert.assertTrue(response.getStatus_code().equals(1));
        Assert.assertEquals(response.getStatus_message(), Status.valueOf("SUCCESS_ACCEPTED").getMessage());
    }

    @Test
    public void updateListValidTest()
            throws Exception {
        //Create a List
        String initialList = TestHelper.getTestDataFilePath("testValidListForUpdate.json");
        String testPayLoadData = TestHelper.readFileAsString(initialList);
        ListResponse response = createMovieList(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Update the List
        File file = new File(initialList);
        MovieList originalList = (MovieList) TestHelper.getResponseObjFromFile(file, MovieList.class);
        String originalName = originalList.getName();
        originalList.setName(originalName + " Updated!");
        String updatedPayload = TestHelper.getJsonString(originalList);
        response = updateMovieList(listId, updatedPayload);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_UPDATE_ACCEPTED").getCode());
        response.assertSuccessData();
        Assert.assertTrue(response.getStatus_code().equals(12));
        Assert.assertEquals(response.getStatus_message(), Status.valueOf("SUCCESS_UPDATE_ACCEPTED").getMessage());
    }

    @Test
    public void updateListInvalidTokenTest()
            throws Exception {
        //Create a List
        String initialList = TestHelper.getTestDataFilePath("testValidListForUpdate.json");
        String testPayLoadData = TestHelper.readFileAsString(initialList);
        ListResponse response = createMovieList(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Update the List
        File file = new File(initialList);
        MovieList originalList = (MovieList) TestHelper.getResponseObjFromFile(file, MovieList.class);
        String originalName = originalList.getName();
        originalList.setName(originalName + " Updated!");
        String updatedPayload = TestHelper.getJsonString(originalList);
        response = updateMovieListInvalidToken(listId, updatedPayload);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("ERROR_SCOPE").getCode());
        response.assertErrorData();
        Assert.assertTrue(response.getStatus_code().equals(36));
        Assert.assertEquals(response.getStatus_message(), Status.valueOf("ERROR_ACCESS").getMessage());
    }

    @Test
    public void updateListInvalidIdTest()
            throws Exception {
        //Create List
        String initialList = TestHelper.getTestDataFilePath("testValidListForUpdate.json");
        String testPayLoadData = TestHelper.readFileAsString(initialList);
        ListResponse response = createMovieList(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Update the List
        File file = new File(initialList);
        MovieList originalList = (MovieList) TestHelper.getResponseObjFromFile(file, MovieList.class);
        String originalName = originalList.getName();
        originalList.setName(originalName + " Updated!");
        String updatedPayload = TestHelper.getJsonString(originalList);
        response = updateMovieList(listId + 888, updatedPayload);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("ERROR_BAD_RESOURCE").getCode());
        response.assertErrorData();
        Assert.assertEquals(response.getStatus_message(), Status.valueOf("ERROR_BAD_RESOURCE").getMessage());
        Assert.assertTrue(response.getStatus_code().equals(34));
    }

    @Test
    public void updateListInvalidDataTest()
            throws Exception {
        //Create List
        String initialList = TestHelper.getTestDataFilePath("testValidListForUpdate.json");
        String testPayLoadData = TestHelper.readFileAsString(initialList);
        ListResponse response = createMovieList(testPayLoadData);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Update the List
        String updatedList = TestHelper.getTestDataFilePath("testInvalidListForUpdate.json");
        String updatedPayload = TestHelper.readFileAsString(updatedList);
        response = updateMovieList(listId, updatedPayload);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("INTERNAL_ERROR").getCode());
        response.assertErrorData();
        Assert.assertEquals(response.getStatus_message(), Status.valueOf("INTERNAL_ERROR").getMessage());
    }

    @Test
    public void clearAllItemsinValidListTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListItems.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 4);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Clear all items from the list
        ListResponse responseUpdatedList = clearList(listId);
        //Verify response
        Assert.assertEquals(responseUpdatedList.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseUpdatedList.assertSuccessData();
        Assert.assertEquals(responseUpdatedList.getId(), listId);
        Assert.assertTrue(responseUpdatedList.getStatus_code().equals(1));
        Assert.assertEquals(responseUpdatedList.getStatus_message(), "Success.");
        Assert.assertEquals(responseUpdatedList.getItems_deleted(), totalItemsinList);
    }

    @Test
    public void clearAllItemsinInvalidListTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListItems.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 4);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Clear all items from an invalid list
        ListResponse responseUpdatedList = clearList(listId + 999);
        //Verify response
        Assert.assertEquals(responseUpdatedList.getHttpStatusCode(), Status.valueOf("ERROR_BAD_RESOURCE").getCode());
        responseUpdatedList.assertErrorData();
        Assert.assertTrue(responseUpdatedList.getStatus_code().equals(34));
        Assert.assertEquals(responseUpdatedList.getStatus_message(), Status.valueOf("ERROR_BAD_RESOURCE").getMessage());
    }

    @Test
    public void clearListInvalidTokenTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListItems.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 4);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Clear all items from an invalid list
        ListResponse responseUpdatedList = clearListInvalidToken(listId);
        //Verify response
        Assert.assertEquals(responseUpdatedList.getHttpStatusCode(), Status.valueOf("ERROR_BAD_KEY").getCode());
        responseUpdatedList.assertErrorData();
        Assert.assertTrue(responseUpdatedList.getStatus_code().equals(7));
        Assert.assertEquals(responseUpdatedList.getStatus_message(), Status.valueOf("ERROR_BAD_KEY").getMessage());
    }
}
