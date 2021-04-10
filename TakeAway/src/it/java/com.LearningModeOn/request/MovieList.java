package com.LearningModeOn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude (JsonInclude.Include.NON_NULL)
public class MovieList {
    private Integer id;
    private String name;
    private String description;
    private Boolean Public;
    private String iso_3166_1;
    private String iso_639_1;
    private String created_by;
    private String poster_path;
    private int item_count;
    private String sort_by;
    private List<ListItems> items;
}
