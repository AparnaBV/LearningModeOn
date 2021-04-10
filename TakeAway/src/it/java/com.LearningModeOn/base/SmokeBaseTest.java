package com.LearningModeOn.base;

import com.LearningModeOn.client.ListClient;
import com.LearningModeOn.response.ItemResponse;
import com.LearningModeOn.response.ListResponse;
import com.LearningModeOn.util.Constants;
import com.LearningModeOn.util.ListItemFailureData;
import com.LearningModeOn.util.MovieListInvalidData;
import com.LearningModeOn.util.MovieListValidData;
import com.LearningModeOn.util.TestHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class SmokeBaseTest {

    public static String apikey;
    public static String access_token_read_only;
    public static String access_token_write_only;
    public static List<Integer> ListIds = new ArrayList<>();
    private Map<String, Object> updateListPayload = new HashMap<>();
    private List<Object> itemList = new ArrayList<>();
    private Map<String, Object> listPayload = new HashMap<>();

    @BeforeSuite
    public void beforeSuite()
            throws IOException {
        log.info("Started SMOKE Suite");
        fetchCredentials();
    }

    @AfterSuite
    public void afterSuite()
            throws Exception {
        deleteAllMovieLists();
        log.info("Finished SMOKE Suite");
    }

    protected String getHostName() {
        log.info("host:{}", System.getProperty("serviceHost"));
        return System.getProperty("serviceHost");
    }

    private void deleteAllMovieLists() {
        if (!ListIds.isEmpty()) {
            log.info("Deleting the Movie Lists ...");
            ListIds.forEach(list -> new ListClient().deleteMovieList(list, access_token_write_only));
        }
    }

    private void fetchCredentials()
            throws IOException {
        String credsfilePath = TestHelper.getPasskeyFilePath("passkeys.json");
        File file = new File(credsfilePath);
        Credentials creds = TestHelper.getAccessCredentials(file);
        apikey = creds.getApiKey();
        access_token_read_only = creds.getAccess_token_r();
        access_token_write_only = creds.getAccess_token_w();
    }

    @DataProvider
    public Iterator<Object[]> getInvalidMovieListData() {
        List<Object[]> data = Lists.newLinkedList();
        data.add(new Object[] {MovieListInvalidData.valueOf("MISSING_ISO_639").getIngestedFile(),
                MovieListInvalidData.valueOf("MISSING_ISO_639").getErrorMessage()});
        data.add(new Object[] {MovieListInvalidData.valueOf("MISSING_NAME").getIngestedFile(),
                MovieListInvalidData.valueOf("MISSING_NAME").getErrorMessage()});
        // :: Bug :: Assertion on length of 'iso_639_1' is failing
//        data.add(new Object[] {MovieListInvalidData.valueOf("INVALID_ISO_639").getIngestedFile(),
//                MovieListInvalidData.valueOf("INVALID_ISO_639").getErrorMessage()});
        return data.iterator();
    }

    @DataProvider
    public Iterator<Object[]> getValidMovieListData() {
        List<Object[]> data = Lists.newLinkedList();
        data.add(new Object[] {MovieListValidData.valueOf("VALID_LIST").getIngestedFile()});
        data.add(new Object[] {MovieListValidData.valueOf("VALID_ALL_DEFAULTS_LIST").getIngestedFile()});
        data.add(new Object[] {MovieListValidData.valueOf("VALID_REQ_FIELDS_ONLY_LIST").getIngestedFile()});
        return data.iterator();
    }

    @DataProvider
    public Iterator<Object[]> getItemIngestionFailureData() {
        List<Object[]> data = Lists.newLinkedList();
        data.add(new Object[] {ListItemFailureData.valueOf("MISSING_MEDIA_ID").getIngestedItem()});
        data.add(new Object[] {ListItemFailureData.valueOf("MISSING_MEDIA_TYPE").getIngestedItem()});
        data.add(new Object[] {ListItemFailureData.valueOf("INVALID_MEDIA_TYPE").getIngestedItem()});
        data.add(new Object[] {ListItemFailureData.valueOf("INVALID_MEDIA_ID").getIngestedItem()});
        return data.iterator();
    }

    public ListResponse createMovieList(String payload) {
        ListResponse response = new ListClient().createMovieList(
                Constants.THEMOVIEDB_HOST, payload, access_token_write_only);
        return response;
    }

    public ListResponse createMovieListInvalidToken(String payload) {
        ListResponse response = new ListClient().createMovieList(
                Constants.THEMOVIEDB_HOST, payload, access_token_read_only);
        return response;
    }

    public ListResponse updateMovieList(Integer listId, String payload) {
        ListResponse response = new ListClient().updateMovieList(
                listId, payload, access_token_write_only);
        return response;
    }

    public ListResponse updateMovieListInvalidToken(Integer listId, String payload) {
        ListResponse response = new ListClient().updateMovieList(
                listId, payload, access_token_read_only);
        return response;
    }

    public ItemResponse addItemToList(Integer listId, String payload) {
        ItemResponse response = new ListClient().addItemsToList(listId, payload, access_token_write_only);
        return response;
    }

    public ItemResponse addItemToListInvalidToken(Integer listId, String payload) {
        ItemResponse response = new ListClient().addItemsToList(listId, payload, access_token_read_only);
        return response;
    }

    public ListResponse checkItemStatus(Integer listId, String media_type, Integer media_id) {
        ListResponse response = new ListClient().checkItemStatus(listId, media_id, media_type, access_token_write_only);
        return response;
    }

    public ItemResponse updateItemInList(Integer listId, String payload) {
        ItemResponse response = new ListClient().updateItemsInList(listId, payload, access_token_write_only);
        return response;
    }

    public ItemResponse removeItemFromList(Integer listId, String payload) {
        ItemResponse response = new ListClient().removeItemFromList(listId, payload, access_token_write_only);
        return response;
    }

    public ItemResponse removeItemFromListInvalidToken(Integer listId, String payload) {
        ItemResponse response = new ListClient().removeItemFromList(listId, payload, access_token_write_only + "phgh");
        return response;
    }

    public ListResponse clearList(Integer listId) {
        ListResponse response = new ListClient().clearMovieList(listId, access_token_write_only);
        return response;
    }

    public ListResponse clearListInvalidToken(Integer listId) {
        ListResponse response = new ListClient().clearMovieList(listId, access_token_read_only + "sdsds");
        return response;
    }

    public String getItemsDetails(ItemResponse responseItem, int itemsToUpdate, boolean isUpdate)
            throws Exception {
        itemList.clear();
        //Fetch item details to be updated
        int i = 0;
        String finalData = null;
        while (i < itemsToUpdate) {
            String _media_type = responseItem.getResults().get(i).getMedia_type();
            Integer _media_id = responseItem.getResults().get(i).getMedia_id();
            updateListPayload.clear();
            updateListPayload.put("media_type", _media_type);
            updateListPayload.put("media_id", _media_id);
            if (isUpdate) {
                String _comments = "List Item Updated!";
                updateListPayload.put("comment", _comments);
            }
            String mediaData = TestHelper.getJsonString(updateListPayload);
            itemList.add(mediaData);
            i++;
        }
        listPayload.clear();
        listPayload.put("items", itemList);
        finalData = TestHelper.getJsonString(listPayload);
        finalData = StringEscapeUtils.unescapeJava(finalData);
        finalData = finalData.replace("}\"", "}").replace("\"{", "{");
        //return payload for updating item
        return finalData;
    }

    public String getInvalidItemsDetails(ItemResponse responseItem, int itemsToUpdate, boolean isUpdate)
            throws Exception {
        //Fetch item details to be updated
        itemList.clear();
        int i = 0;
        String finalData = null;
        while (i < itemsToUpdate) {
            String _media_type = responseItem.getResults().get(i).getMedia_type();
            Integer _media_id = responseItem.getResults().get(i).getMedia_id();
            updateListPayload.clear();
            updateListPayload.put("media_type", _media_type);
            updateListPayload.put("media_id", _media_id + 111);
            if (isUpdate) {
                String _comments = "List Item Updated!";
                updateListPayload.put("comment", _comments);
            }
            String mediaData = TestHelper.getJsonString(updateListPayload);
            itemList.add(mediaData);
            i++;
        }
        listPayload.clear();
        listPayload.put("items", itemList);
        finalData = TestHelper.getJsonString(listPayload);
        finalData = StringEscapeUtils.unescapeJava(finalData);
        finalData = finalData.replace("}\"", "}").replace("\"{", "{");
        //return payload for updating item
        return finalData;
    }
}
