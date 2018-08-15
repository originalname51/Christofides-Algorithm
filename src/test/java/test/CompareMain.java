package test;

import main.ChristofidesTour;
import main.mainProgram;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            ChristofidesTour newChristofidesTour = mainProgram.ChristofidesAlgorithm(newTourFile, 0);
            assertEquals(oldChristofidesTour.getTourCost(), newChristofidesTour.getTourCost());
            assertEqualsArray(oldChristofidesTour.getFinalTour(), newChristofidesTour.getFinalTour());
        }
    }

   private static void assertEqualsArray(List<Integer> listOne, List<Integer> listTwo) {
        for (int i = 0; i < listOne.size(); i++) {
            assertTrue(listOne.get(i).equals(listTwo.get(i)));
        }
        for (int i = 0; i < listTwo.size(); i++) {
            assertTrue(listOne.get(i).equals(listTwo.get(i)));
        }
    }

   private static ChristofidesTour parseAnswer(String filePath) throws IOException {
       try(Stream<String> stream = Files.lines(Paths.get(filePath))) {
           List<Integer>  finalTour = stream.map(Integer::parseInt)
                   .collect(Collectors.toCollection(ArrayList::new));
           int totalDistance = finalTour.get(0);
           finalTour.remove(0);
           return new ChristofidesTour(finalTour, totalDistance);
       }
    }


}
