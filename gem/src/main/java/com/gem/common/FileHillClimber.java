package com.gem.common;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.gem.App;
import com.gem.models.TestCase;
import com.gem.models.TestSuiteIndividual;

public class FileHillClimber {
    String hillClimberType = "";
    String fileName = "";
    TestSuiteIndividual currentState;
    Integer neighborhoodSize = 0;
    List<TestCase> unshuffledTestCases = new ArrayList<>();

    List<TestSuiteIndividual> top5 = new ArrayList<>(5);
    List<String> genNames = new ArrayList<>(5);

    public FileHillClimber(String fileName, Integer size) {
        this.fileName = fileName;
        this.neighborhoodSize = size;
    }

    public void doHillClimibing() throws IOException {

        // if (this.hillClimberType.equals("StringBased")) {
        // Set our initial climbing state
        this.setInitialState();

        // Score inital state
        Float bestScore = scoreState(currentState);

        TestSuiteIndividual bestState = currentState;
        Integer count = 0;

        while (true) {
            count++;
            // System.out.println("Start");
            // Generate new neighbours based on current position/state
            List<TestSuiteIndividual> newNeighbors = generateNeighbors(currentState);
            // Find the best neighour index from the generated neighbourhood
            Integer bestNew = this.findBestNeighbourInNeighborhood(newNeighbors);
            // Get that neighbour
            TestSuiteIndividual newS = newNeighbors.get(bestNew);
            newS.calculateIndividualAPFD();
            // System.out.println("New State: " + newS);

            // Check if current state is the same as new state. if true use next index
            if (newS.equals(currentState)) {
                newS = newNeighbors.get(bestNew + 1);
            }

            // Score our new state
            float newScore = scoreState(newS);

            // System.out.println("Best State: " + bestState);
            // System.out.println("Best Score: " + bestScore);
            // System.out.println("New Score: " + newScore);

            if (top5.size() == 3) {
                System.out.println("Attempting to print file");
                writeToFile(top5, genNames);
                break;
            }
            // System.out.println("Middle");

            // If our new score is better than best score
            // Set current state and best score to that values if not everything will repeat
            // again
            if (newScore > bestScore) {
                bestScore = newScore;
                currentState = newS;
                bestState = newS;
                genNames.add("Generation: " + count);
                top5.add(newS);
            }
            // System.out.println("End");

        }

    }

    public void setInitialState() {

        // Generate a random neighbour and set inital state to that neighbour
        this.currentState = generateInitalNeighbourhood();

    }

    // Fitness function to score an individal. Is based on apfd
    public Float scoreState(TestSuiteIndividual ts) {
        Float val = 0f;

        Float tfScore = 0f;
        Integer faultsCount = ts.getTestcases().get(0).getFaultsDetected().size();

        for (int i = 0; i < faultsCount; i++) {

            for (int j = 0; j < ts.getTestcases().size(); j++) {

                TestCase testCase = ts.getTestcases().get(j);

                List<String> faultsList = testCase.getFaultsDetected();

                if (faultsList.get(i).equals("1")) {
                    tfScore += (j + 1);
                    break;
                } else {
                    continue;
                }

            }
        }

        // System.out.println(testcases);

        Integer faultsCountMultByTestCases = faultsCount * ts.getTestcases().size();

        Float firstHalf = tfScore / faultsCountMultByTestCases;
        Float secondHalf1 = 2f * ts.getTestcases().size();
        Float secondHalf2 = 1 / secondHalf1;

        val = 1 - firstHalf + secondHalf2;

        return val;
    }

    // Create a list of neighbours from out current position
    public List<TestSuiteIndividual> generateNeighbors(TestSuiteIndividual currentNeighbor) {
        List<TestSuiteIndividual> neighbors = new ArrayList<>();
        // String ALLCHARS = utils.returnAllCharacters();
        // Integer currentNeighborLength = currentNeighbor.length();
        // System.out.println("Start of generating neighbors");
        for (int i = 0; i < neighborhoodSize; i++) {
            TestSuiteIndividual newNeighbour = new TestSuiteIndividual();
            List<TestCase> transformedTestCases = new ArrayList<TestCase>(currentNeighbor.getTestcases().size());

            Random randGen = new Random();

            int number1 = randGen.nextInt(currentNeighbor.getTestcases().size() - 1);

            int number2 = randGen.nextInt(currentNeighbor.getTestcases().size());

            int lowerbound = Math.min(number1, number2);
            int upperbound = Math.max(number1, number2);

            // System.out.println("Begining of first loop of generating neighbors");

            if (lowerbound == upperbound) {
                upperbound = lowerbound + 1;
            }

            transformedTestCases.addAll(currentNeighbor.getTestcases().subList(0, lowerbound));

            transformedTestCases
                    .addAll(currentNeighbor.getTestcases().subList(upperbound, currentNeighbor.getTestcases().size()));

            while (transformedTestCases.size() < currentNeighbor.getTestcases().size()) {
                int randomIndex = randGen.nextInt(currentNeighbor.getTestcases().size());

                // System.out.println("Inside loop of generating neighbors");
                // System.out.println("Currently generation for new neighbor: " + i);
                TestCase randomTestCase = unshuffledTestCases.get(randomIndex);
                // System.out.println("Random Testcase from unshuffled " + randomTestCase);
                // System.out.println("To be transformed size:" + transformedTestCases.size());
                // System.out.println("Current neighbour size: " +
                // currentNeighbor.getTestcases().size());

                if (transformedTestCases.size() == currentNeighbor.getTestcases().size()) {

                    break;
                }

                if (transformedTestCases.contains(randomTestCase)) {
                    continue;
                } else {
                    transformedTestCases.add(randomTestCase);
                    // System.out.println("To be transformed:" + transformedTestCases);
                    // System.out.println("Current neighbour: " + currentNeighbor.getTestcases());
                    // System.out.println("To be transformed size:" + transformedTestCases.size());
                    // System.out.println("Current neighbour size: " +
                    // currentNeighbor.getTestcases().size());
                    // System.out.println("Array already contains " + randomTestCase);
                }
            }

            // System.out.println(transformedTestCases.size());

            newNeighbour.setTestcases(transformedTestCases);

            neighbors.add(newNeighbour);

            // System.out.println("End of generating neighbors");

        }
        return neighbors;
    }

    // Take a list of neighours and score each one to find the best neighbour by
    // index
    public Integer findBestNeighbourInNeighborhood(List<TestSuiteIndividual> neighbors) {
        float bestScore = 0f;
        int bestNeighborIndex = 0;

        for (int i = 0; i < neighbors.size(); i++) {
            float neighborScore = this.scoreState(neighbors.get(i));
            if (neighborScore > bestScore) {
                bestScore = neighborScore;
                bestNeighborIndex = i;
            }
        }

        return bestNeighborIndex;
    }

    private InputStream getFileAsStream(final String fileName) {
        InputStream ioStream = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    public TestSuiteIndividual generateInitalNeighbourhood() {
        InputStream stream = getFileAsStream(fileName);

        List<TestCase> testCases = new ArrayList<>();

        unshuffledTestCases = testCases;

        try (InputStreamReader in = new InputStreamReader(stream); BufferedReader br = new BufferedReader(in);) {
            Integer count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                count++;
                String[] testcaseArray = line.split(",");
                TestCase tc = new TestCase();
                tc.setTestCaseId(testcaseArray[0]);
                List<String> faultsList = new ArrayList<>();

                for (int i = 0; i < testcaseArray.length; i++) {
                    if (i != 0) {
                        faultsList.add(testcaseArray[i]);
                    }
                }
                tc.setFaultsDetected(faultsList);

                testCases.add(tc);

            }

            // System.out.println(Arrays.toString(unshuffledTestCases.toArray()));

            br.close();

        } catch (IOException e) {
            System.out.println("File Read Error");
        }

        TestSuiteIndividual li = new TestSuiteIndividual();

        Random r = new Random();
        Collections.shuffle(testCases, r);
        li.setTestcases(testCases);

        return li;
    }

    public void writeToFile(List<TestSuiteIndividual> inds, List<String> genNames) throws IOException {
        // Path source = Paths.get(this.getClass().getResource("").getPath());
        // Path newFolder = Paths.get(source.toAbsolutePath() + "/generationLogs/");
        // Files.createDirectories(newFolder);

        String fileName = "Hill_climber_" + LocalDateTime.now().toString() + "_logs";

        // Path path = Paths.get(fileName);
        // byte[] strToBytes = str.getBytes();

        // Files.write(path, strToBytes);

        String thing = "";

        for (int i = 0; i < inds.size(); i++) {
            String test = "=======" + genNames.get(i) + "======= \n" + inds.get(i).toString() + "\n";
            thing += test;

        }

        String dir = getClass().getResource("/").getFile();
        // String dir = WriteResource.class.getResource("/dir").getFile();
        OutputStream os = new FileOutputStream(dir + fileName.replaceAll(":", "_") + ".txt");

        final PrintStream printStream = new PrintStream(os);

        printStream.println(thing);
        printStream.close();

    }
}
