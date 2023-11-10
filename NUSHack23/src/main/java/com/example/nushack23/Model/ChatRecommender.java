package com.example.nushack23.Model;

import java.util.ArrayList;

public class ChatRecommender {
    public static double affinity_weight=0.8, compatibility_weight=0.1, similarity_weight=0.6, neighbour_weight=0.1;
    public static ArrayList<Chat> getRecommendations(Account user) {
        ArrayList<Double> scores = user.getScores();
        // TODO: CONSIDER SCORES OF NEIGHBOURS TOO
        for 
    }
}
