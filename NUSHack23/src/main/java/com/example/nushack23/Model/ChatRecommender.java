package com.example.nushack23.Model;

import java.util.ArrayList;
import javafx.util.Pair;

public class ChatRecommender {
    public static double affinity_weight=0.8, compatibility_weight=0.1, similarity_weight=0.6, neighbour_weight=0.1;
    public static ArrayList<Chat> getRecommendations(Account user) {
        ArrayList<Double> scores = user.getScores();
        // TODO: CONSIDER SCORES OF NEIGHBOURS TOO


        ArrayList<Pair<Chat, Double>> pairs = new ArrayList<>();
        for (int i=0; i<scores.size(); i++) {
            pairs.add(new Pair<Chat, Double>(Database.all_chats.get(i), scores.get(i)));
        }
        pairs.sort((Pair<Chat, Double> p1, Pair<Chat, Double> p2) -> (p1.getValue().compareTo(p2.getValue())));

        ArrayList<Chat> res = new ArrayList<>();
        for (int i=0; i<Math.min(3, scores.size()); i++) {
            res.add(pairs.get(i).getKey());
        }
        return res;
    }
}
