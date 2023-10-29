package com.gem.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import com.gem.App;
import com.gem.models.StringIndividual;
import com.gem.models.TestCase;
import com.gem.models.TestSuiteIndividual;
import com.google.common.collect.Range;

public class FilebasedGA {

    // List<TestSuiteIndividual> currentPopulation = new ArrayList<>();
    List<TestCase> unshuffledTestCases = new ArrayList<>();
    Float bestScore = 0f;
    Float currentScore = 0f;

    public void run() throws IOException {
        unshuffledTestCases = readFile();

        List<TestSuiteIndividual> top3 = new ArrayList<>(5);
        List<String> genNames = new ArrayList<>(5);

        List<TestSuiteIndividual> p = generateInitialPopulation();

        TestSuiteIndividual fittesTestSuiteIndividual = findBestIndividual(p);
        TestSuiteIndividual bestFitIndividual = findBestIndividual(p);

        bestScore = fittesTestSuiteIndividual.getFitnessScore();
        currentScore = fittesTestSuiteIndividual.getFitnessScore();

        Integer generationCount = 0;

        while (generationCount <= App.maxGenerations) {
            generationCount++;

            // Return fittest individual in current generation
            fittesTestSuiteIndividual = findBestIndividual(p);

            // If fittest individual is better than our most fit change most fit to best fit
            if (fittesTestSuiteIndividual.getFitnessScore() > bestFitIndividual.getFitnessScore()) {
                bestFitIndividual = fittesTestSuiteIndividual;
                genNames.add("Generation: " + generationCount);
                top3.add(fittesTestSuiteIndividual);
            }

            if (top3.size() == 3) {
                System.out.println("Attempting to print file");
                writeToFile(top3, genNames);
                break;
            }

            // If we reach max generation and other info
            if (generationCount.equals(App.maxGenerations)) {
                System.out.println("Best Individual: \n" + bestFitIndividual);
                System.out.println("Generation Found: " + generationCount);
                System.out.println("Score: " + bestFitIndividual.getFitnessScore());
                break;
            }

            List<TestSuiteIndividual> newPopulation = createNewPopulation(p);
            mutatePopulation(p);
            // System.out.println(newPopulation);
            p = newPopulation;

        }
        // System.out.println("Here is the intial score: " + initalScore);
    }

    public List<TestCase> readFile() {
        InputStream stream = getFileAsStream(App.fileName);
        List<TestCase> testCases = new ArrayList<>();
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

        return testCases;

    }

    public List<TestSuiteIndividual> generateInitialPopulation() {

        List<TestSuiteIndividual> li = new ArrayList<TestSuiteIndividual>();

        Random r = new Random();

        for (int i = 0; i < App.populationSize; i++) {
            TestSuiteIndividual individual = new TestSuiteIndividual();

            List<TestCase> ref = new ArrayList<>(unshuffledTestCases);
            Collections.shuffle(ref, r);

            individual.setTestcases(ref);

            li.add(individual);
        }

        // System.out.println("===============================");
        // System.out.println("Inital gen");
        // System.out.println(li);
        // System.out.println("===============================");

        return li;

    }

    public TestSuiteIndividual findBestIndividual(List<TestSuiteIndividual> individuals) {

        TestSuiteIndividual fittest = new TestSuiteIndividual();

        Float highestScore = 0f;

        for (TestSuiteIndividual ind : individuals) {

            // System.out.println(ind);
            Float indScore = ind.calculateIndividualAPFD();
            // System.out.println(indScore);
            if (indScore > highestScore) {
                highestScore = indScore;
                fittest = ind;
            }

        }

        return fittest;

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

    // Selection Strategies Here
    public TestSuiteIndividual tournamentSelection(List<TestSuiteIndividual> individuals) {

        TestSuiteIndividual winningIndividual;
        Random randGen = new Random();

        // Select two random indexes from list
        Integer parent1Index = randGen.nextInt(individuals.size() - 1);
        Integer parent2Index = randGen.nextInt(individuals.size() - 1);

        // If both indexes are the same increase by 1
        if (parent1Index == parent2Index) {
            parent2Index += 1;
        }

        // Get the parents by index
        TestSuiteIndividual parent1 = individuals.get(parent1Index);
        TestSuiteIndividual parent2 = individuals.get(parent2Index);

        // Compare parent 1 against 2
        if (parent1.getFitnessScore() > parent2.getFitnessScore()) {
            winningIndividual = parent1;
        } else {
            winningIndividual = parent2;
        }

        // If one is better than the other return that individual
        return winningIndividual;

    }

    public TestSuiteIndividual[] createOffSpring(TestSuiteIndividual parent1, TestSuiteIndividual parent2) {
        TestSuiteIndividual[] offspring = new TestSuiteIndividual[2];

        Random rand = new Random();
        float randProb = rand.nextFloat();

        if (randProb <= App.crossOverRate) {
            int number1 = rand.nextInt(parent1.getTestcases().size() - 1);

            int number2 = rand.nextInt(parent1.getTestcases().size());

            int lowerbound = Math.min(number1, number2);
            int upperbound = Math.max(number1, number2);

            // System.out.println(lowerbound);
            // System.out.println(upperbound);

            int size = parent1.getTestcases().size();

            List<TestCase> child1 = new Vector<TestCase>();
            List<TestCase> child2 = new Vector<TestCase>();

            child1.addAll(parent1.getTestcases().subList(lowerbound, upperbound));
            child2.addAll(parent2.getTestcases().subList(lowerbound, upperbound));

            int currentTestcaseIndex = 0;
            TestCase curentTestCaseInParent1;
            TestCase curentTestCaseInParent2;
            for (int i = 0; i < parent1.getTestcases().size(); i++) {

                currentTestcaseIndex = (upperbound + i) % size;

                curentTestCaseInParent1 = parent1.getTestcases().get(currentTestcaseIndex);
                curentTestCaseInParent2 = parent1.getTestcases().get(currentTestcaseIndex);

                if (!child1.contains(curentTestCaseInParent2)) {
                    child1.add(curentTestCaseInParent2);
                }

                if (!child2.contains(curentTestCaseInParent1)) {
                    child2.add(curentTestCaseInParent1);
                }
            }

            Collections.rotate(child1, lowerbound);
            Collections.rotate(child2, lowerbound);

            Collections.copy(parent1.getTestcases(), child2);
            Collections.copy(parent2.getTestcases(), child1);

            TestSuiteIndividual offSpring1 = new TestSuiteIndividual();
            offSpring1.setTestcases(child1);

            TestSuiteIndividual offSpring2 = new TestSuiteIndividual();
            offSpring2.setTestcases(child2);

            offspring[0] = offSpring1;
            offspring[1] = offSpring2;

          
        } else {
            offspring[0] = parent1;
            offspring[1] = parent2;
        }

        return offspring;
    }

    // Population generation here
    public List<TestSuiteIndividual> createNewPopulation(List<TestSuiteIndividual> individuals) {
        List<TestSuiteIndividual> newPop = new ArrayList<>(App.populationSize);

        for (int i = 0; i < App.populationSize; i += 2) {
            TestSuiteIndividual ind1;
            TestSuiteIndividual ind2;

            // System.out.println(individuals.size());

            ind1 = tournamentSelection(individuals);
            ind2 = tournamentSelection(individuals);

            if (ind1.equals(ind2) || ind2.equals(ind1)) {
                ind2 = tournamentSelection(individuals);
            }

            TestSuiteIndividual[] offSpring = createOffSpring(ind1, ind2);

            TestSuiteIndividual child1 = offSpring[0];
            TestSuiteIndividual child2 = offSpring[1];

            // Add offspring to list
            newPop.add(i, child1);
            newPop.add(i + 1, child2);

        }

        return newPop;
    }

    public void mutatePopulation(List<TestSuiteIndividual> individuals) {

        Random rand = new Random();
        float randProb = rand.nextFloat();

        if (randProb <= App.mutationRate) {
            Integer randomInvdIndex = rand.nextInt(individuals.size() - 1);
            TestSuiteIndividual randIndividual = individuals.get(randomInvdIndex);

            Integer randomIndex1 = rand.nextInt(randIndividual.getTestcases().size() - 1);
            Integer randomIndex2 = rand.nextInt(randIndividual.getTestcases().size() - 1);

            if (randomIndex1.equals(randomIndex2)) {
                randomIndex2 = randomIndex2 - 1;
            }

            Collections.swap(randIndividual.getTestcases(), randomIndex1, randomIndex2);
        }

        // return individuals;
    }

    public void writeToFile(List<TestSuiteIndividual> inds, List<String> genNames) throws IOException {
        // Path source = Paths.get(this.getClass().getResource("").getPath());
        // Path newFolder = Paths.get(source.toAbsolutePath() + "/generationLogs/");
        // Files.createDirectories(newFolder);

        String fileName = "GA_"+ LocalDateTime.now().toString() + "_logs";

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
