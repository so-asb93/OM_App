package com.example.om_app;

import java.util.List;
public class RestJoueursResponse {
    private int count;
    private String logo;
    private List<Joueurs> results;
    public int getCount() {
        return count;
    }

    public String getLogo() {
        return logo;
    }

    public List<Joueurs> getResults() {
        return results;
    }
}