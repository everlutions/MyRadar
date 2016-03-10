package nl.everlutions.myradar.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseObjectDto {

    @JsonProperty
    public Boolean success;
    @JsonProperty
    public String msg;
}
