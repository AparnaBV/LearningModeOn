package com.LearningModeOn.base;

/**
 * Class for reading configuration from properties file
 *
 * @author Aparna Upadhyay
 *
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credentials {
    private  String apiKey;
    private  String username;
    private  String access_token_r;
    private  String access_token_w;
}
