package com.LearningModeOn.response;

import com.LearningModeOn.request.ListItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.testng.Assert;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse extends  BaseResponse {

    private List<ListItems> results;
    private Integer status_code;
    private String status_message;
    private boolean success;
    private List<String> errors;

    public void assertErrorData() {
        Assert.assertFalse(isSuccess());
    }

    public void assertSuccessData() {
        Assert.assertTrue(isSuccess());
    }

}
