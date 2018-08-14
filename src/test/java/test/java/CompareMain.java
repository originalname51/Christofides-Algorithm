package test.java;

import main.java.ChristofidesTour;
import main.java.Vertex;
import main.java.mainProgram;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompareMain {

    private static final String NAME_OF_OLD_TOUR = "test-input-";
    private static final String END_OF_OLD_TOUR = ".txt.baseline.tour";
    private static final String NAME_OF_INPUT_FILE = "test-input-";
    private static final String END_OF_FILE = ".txt";


    @Test
    public void resultsFromFirstTourAreTheSame() throws IOException {
        for (int i = 1; i < 8; i++) {
            String oldTourFile = NAME_OF_OLD_TOUR + i + END_OF_OLD_TOUR;
            String newTourFile = NAME_OF_INPUT_FILE + i + END_OF_FILE;
            ChristofidesTour oldChristofidesTour = parseAnswer(oldTourFile);
            ChristofidesTour newChristofidesTour = mainProgram.ChristofidesAlgorithm(newTourFile);
            assertEquals(oldChristofidesTour.getTourCost(), newChristofidesTour.getTourCost());
            assertEqualsArray(oldChristofidesTour.getFinalTour(), newChristofidesTour.getFinalTour());
        }
    }


    @Test
    public void correctlyParseOldTour() throws IOException {

        ChristofidesTour oldChristofidesTour = parseAnswer(NAME_OF_OLD_TOUR);
        System.out.println(oldChristofidesTour.getTourCost());
        for (Integer tourStop : oldChristofidesTour.getFinalTour()) {
            System.out.println(tourStop);
        }

    }

    static void assertEqualsArray(List<Integer> listOne, List<Integer> listTwo) {
        for (int i = 0; i < listOne.size(); i++) {
            assertTrue(listOne.get(i).equals(listTwo.get(i)));
        }
        for (int i = 0; i < listTwo.size(); i++) {
            assertTrue(listOne.get(i).equals(listTwo.get(i)));
        }
    }

    static ChristofidesTour parseAnswer(String in) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(in));
        ArrayList<Vertex> graph = new ArrayList<Vertex>();
        String line;
        line = br.readLine();
        int totalDistance = Integer.parseInt(line);
        List<Integer> finalTour = new ArrayList<Integer>();
        while ((line = br.readLine()) != null) {
            Integer tourId = Integer.parseInt(line);
            finalTour.add(tourId);
        }
        br.close();

        return new ChristofidesTour(finalTour, totalDistance);
    }


}
