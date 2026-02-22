package com.hkcapital.portoflio.etoro.dto.order;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EtoroOrderDetailsResponseDTO {
    @JsonProperty("orderForOpen")
    private EtoroOrderDetails orderForOpen;

    @JsonProperty("token")
    private String token;

    // Getters and Setters
    public EtoroOrderDetails getOrderForOpen() { return orderForOpen; }
    public void setOrderForOpen(EtoroOrderDetails orderForOpen) { this.orderForOpen = orderForOpen; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }



}