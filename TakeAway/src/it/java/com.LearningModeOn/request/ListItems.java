package com.LearningModeOn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude (JsonInclude.Include.NON_NULL)
public class ListItems {
    private String media_type;
    private Integer media_id;
    private boolean success;
    private int sort_order;
    private String comment;
}
