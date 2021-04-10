package com.LearningModeOn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude (JsonInclude.Include.NON_NULL)
public class ListResponse extends BaseResponse {
    private Integer id;
    private Integer status_code;
    private String status_message;
    private boolean success;
    private List<String> errors;
    private int items_deleted;
    private int total_results;
    private String media_type;
    private String media_id;

    public void assertErrorData() { Assert.assertFalse(isSuccess()); }

    public void assertSuccessData() {
        Assert.assertTrue(isSuccess());
    }
}
