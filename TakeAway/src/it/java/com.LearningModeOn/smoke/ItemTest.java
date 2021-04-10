package com.LearningModeOn.smoke;

import com.LearningModeOn.base.SmokeBaseTest;
import com.LearningModeOn.client.ListClient;
import com.LearningModeOn.response.ItemResponse;
import com.LearningModeOn.response.ListResponse;
import com.LearningModeOn.util.Status;
import com.LearningModeOn.util.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemTest extends SmokeBaseTest {

    @Test
    public void addValidItemToListTest()
            throws Exception {
        //Create List
        List<Object> itemResult = new ArrayList<>();
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String testItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListItems.json"));
        ItemResponse responseItem = new ListClient().addItemsToList(
                listId, testItems, access_token_write_only);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        int totalResults = responseItem.getResults().size();
        Assert.assertEquals(totalResults, 4);
        for (int i = 0; i < totalResults; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
            //Check item status in the list
            Assert.assertTrue(checkItemStatus(listId, responseItem.getResults().get(i).getMedia_type(),
                    responseItem.getResults().get(i).getMedia_id())
                    .isSuccess());
        }
    }

    @Test
    public void addDuplicateItemToListTest()
            throws Exception {
        //Create List
        List<Object> itemResult = new ArrayList<>();
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String testItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidItemsForUpdate.json"));
        ItemResponse responseItem = addItemToList(listId, testItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        int totalResults = responseItem.getResults().size();
        Assert.assertEquals(totalResults, 1);
        for (int i = 0; i < totalResults; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
            //Check item status in the list
            Assert.assertTrue(checkItemStatus(listId, responseItem.getResults().get(i).getMedia_type(),
                    responseItem.getResults().get(i).getMedia_id())
                    .isSuccess());
        }

        //Add the same item again
        ItemResponse responseDuplicateItem = new ListClient().addItemsToList(listId, testItems, access_token_write_only);
        Assert.assertEquals(responseDuplicateItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseDuplicateItem.assertSuccessData();
        Assert.assertTrue(responseDuplicateItem.getStatus_code().equals(1));
        Assert.assertEquals(responseDuplicateItem.getStatus_message(), "Success.");
        totalResults = responseItem.getResults().size();
        Assert.assertEquals(totalResults, 1);
        for (int i = 0; i < totalResults; i++) {
            Assert.assertFalse(responseDuplicateItem.getResults().get(i).isSuccess());
        }
    }

    @Test (dataProvider = "getItemIngestionFailureData")
    public void addInvalidItemToListTest(String file)
            throws Exception {
        //Create List
        List<Object> itemResult = new ArrayList<>();
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListDefaults.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add invalid items to the List
        String testItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath(file));
        ItemResponse responseItem = addItemToList(listId, testItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");

        //Verify that items are not added
        int totalItemsinList = responseItem.getResults().size();
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertFalse(responseItem.getResults().get(i).isSuccess());
            Assert.assertFalse(checkItemStatus(listId, responseItem.getResults().get(i).getMedia_type(),
                    responseItem.getResults().get(i).getMedia_id())
                    .isSuccess());
        }
    }

    @Test
    public void addInvalidItemToListErrorTest()
            throws Exception {
        //Create List
        List<Object> itemResult = new ArrayList<>();
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidList.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add invalid items to the List
        String testItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testInvalidSchemaItem.json"));
        ItemResponse responseItem = addItemToList(listId, testItems);
        Assert.assertNotNull(responseItem.getErrors());
    }

    @Test
    public void addItemInvalidTokenTest()
            throws Exception {
        //Create List
        List<Object> itemResult = new ArrayList<>();
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidList.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String testItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testInvalidItemsMissingMediaType.json"));
        ItemResponse responseItem = addItemToListInvalidToken(listId, testItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("ERROR_ACCESS").getCode());
        responseItem.assertErrorData();
        Assert.assertTrue(responseItem.getStatus_code().equals(36));
        Assert.assertEquals(responseItem.getStatus_message(), Status.valueOf("ERROR_ACCESS").getMessage());
    }

    @Test
    public void updateItemInListValidTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidItemsForUpdate.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Update item
        String updatedPayload = getItemsDetails(responseItem, 1, true);
        ItemResponse responseUpdatedItem = updateItemInList(listId, updatedPayload);
        //Verify response
        Assert.assertEquals(responseUpdatedItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseUpdatedItem.assertSuccessData();
        Assert.assertTrue(responseUpdatedItem.getStatus_code().equals(1));
        Assert.assertEquals(responseUpdatedItem.getStatus_message(), "Success.");
        //Verify that items are updated
        totalItemsinList = responseUpdatedItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseUpdatedItem.getResults().get(i).isSuccess());
        }
    }

    @Test
    public void updateItemInvalidListTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidItemsForUpdate.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Update item with invalid listId
        String updatedPayload = getItemsDetails(responseItem, 1, true);
        ItemResponse responseUpdatedItem = updateItemInList(listId + 111, updatedPayload);
        //Verify response
        Assert.assertEquals(responseUpdatedItem.getHttpStatusCode(), Status.valueOf("ERROR_BAD_RESOURCE").getCode());
        responseUpdatedItem.assertErrorData();
        Assert.assertTrue(responseUpdatedItem.getStatus_code().equals(34));
        Assert.assertEquals(responseUpdatedItem.getStatus_message(), Status.valueOf("ERROR_BAD_RESOURCE").getMessage());
    }

    @Test
    public void updateInvalidItemTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidItemsForUpdate.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Update non-existing items in the listId
        String updatedPayload = getInvalidItemsDetails(responseItem, 1, true);
        ItemResponse responseUpdatedItem = updateItemInList(listId, updatedPayload);
        //Verify response
        Assert.assertEquals(responseUpdatedItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseUpdatedItem.assertSuccessData();
        Assert.assertTrue(responseUpdatedItem.getStatus_code().equals(1));
        //Verify items are not updated
        totalItemsinList = responseUpdatedItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertFalse(responseUpdatedItem.getResults().get(i).isSuccess());
        }
    }

    @Test
    public void removeItemFromListValidTest()
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

        // Remove items from the list
        int totalItemsToUpdate =2;
        String updatedPayload = getItemsDetails(responseItem, totalItemsToUpdate, false);
        ItemResponse responseUpdatedItem = removeItemFromList(listId, updatedPayload);
        //Verify response
        Assert.assertEquals(responseUpdatedItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseUpdatedItem.assertSuccessData();
        Assert.assertTrue(responseUpdatedItem.getStatus_code().equals(1));
        Assert.assertEquals(responseUpdatedItem.getStatus_message(), "Success.");
        //Verify that items are  removed
        int totalItemsUpdated = responseUpdatedItem.getResults().size();
        Assert.assertEquals(totalItemsUpdated, totalItemsToUpdate);
        for (int i = 0; i < totalItemsUpdated; i++) {
            Assert.assertTrue(responseUpdatedItem.getResults().get(i).isSuccess());

        }
    }

    @Test
    public void removeItemFromListInvalidTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidItemsForUpdate.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Remove non-existing items from the list
        int totalItemsToUpdate=1;
        String updatedPayload = getInvalidItemsDetails(responseItem, totalItemsToUpdate, false);
        ItemResponse responseUpdatedItem = removeItemFromList(listId, updatedPayload);
        //Verify response
        Assert.assertEquals(responseUpdatedItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        Assert.assertTrue(responseUpdatedItem.getStatus_code().equals(1));
        Assert.assertEquals(responseUpdatedItem.getStatus_message(), "Success.");
        //Verify that items are not removed
        int totalItemsUpdated = responseUpdatedItem.getResults().size();
        Assert.assertEquals(totalItemsUpdated, totalItemsToUpdate);
        for (int i = 0; i < totalItemsUpdated; i++) {
            Assert.assertFalse(responseUpdatedItem.getResults().get(i).isSuccess());
        }
    }

    @Test
    public void removeItemInvalidTokenTest()
            throws Exception {
        //Create a new list
        String testList = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidListReqFieldsOnly.json"));
        ListResponse response = createMovieList(testList);
        Assert.assertEquals(response.getHttpStatusCode(), Status.valueOf("SUCCESS_ACCEPTED").getCode());

        //Fetch ListId from response
        Integer listId = response.getId();
        ListIds.add(listId);

        //Add items to the List
        String initialItems = TestHelper.readFileAsString(TestHelper.getTestDataFilePath("testValidItemsForUpdate.json"));
        ItemResponse responseItem = addItemToList(listId, initialItems);
        Assert.assertEquals(responseItem.getHttpStatusCode(), Status.valueOf("SUCCESS").getCode());
        responseItem.assertSuccessData();
        Assert.assertTrue(responseItem.getStatus_code().equals(1));
        Assert.assertEquals(responseItem.getStatus_message(), "Success.");
        //Verify that items are  added
        int totalItemsinList = responseItem.getResults().size();
        Assert.assertEquals(totalItemsinList, 1);
        for (int i = 0; i < totalItemsinList; i++) {
            Assert.assertTrue(responseItem.getResults().get(i).isSuccess());
        }

        // Remove items from the list with invalid token
        String updatedPayload = getItemsDetails(responseItem, 1, false);
        ItemResponse responseUpdatedItem = removeItemFromListInvalidToken(listId, updatedPayload);
        //Verify response
        Assert.assertEquals(responseUpdatedItem.getHttpStatusCode(), Status.valueOf("ERROR_BAD_KEY").getCode());
        responseUpdatedItem.assertErrorData();
        Assert.assertTrue(responseUpdatedItem.getStatus_code().equals(7));
        Assert.assertEquals(responseUpdatedItem.getStatus_message(), Status.valueOf("ERROR_BAD_KEY").getMessage());
    }
}
