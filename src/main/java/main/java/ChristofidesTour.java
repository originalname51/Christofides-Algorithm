package main.java;

import java.util.List;

public class ChristofidesTour {
    private List<Integer> finalTour;
    private int tourCost;

    public ChristofidesTour(List<Integer> finalTour, int tourCost ) {
        this.finalTour = finalTour;
        this.tourCost = tourCost;
    }

    public ChristofidesTour() {
    }
    public List<Integer> getFinalTour() {
        return finalTour;
    }

    public void setFinalTour(List<Integer> finalTour) {
        this.finalTour = finalTour;
    }

    public int getTourCost() {
        return tourCost;
    }

    public void setTourCost(int tourCost) {
        this.tourCost = tourCost;
    }
}
