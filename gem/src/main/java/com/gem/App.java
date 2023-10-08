package com.gem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import com.gem.common.HillClimber;
import com.gem.common.StringBasedGA;
import com.gem.common.utils;

public class App {

  // Fetch configuration values from application.properties file e.g target value
  // if string based,pop size etc
  public static Properties appProps = new Properties();
  public static String target = "";
  public static Float targetScore = 0f;
  public static Integer populationSize = 0;
  public static Float evalScore = 0f;
  public static Integer maxGenerations = 0;
  public static Float crossOverRate = 0f;
  public static Float mutationRate = 0f;

  public static void main(String[] args) throws FileNotFoundException, IOException {

    // Load the values from path into properties object
    InputStream stream = App.class.getClassLoader().getResourceAsStream("application.properties");
    appProps.load(stream);

    // Load ga type and evalscore from appProps
    String gaType = appProps.getProperty("ga_representation");
    String gaEvalScore = appProps.getProperty("ga_fitness_eval_score");
    String gaGenString = appProps.getProperty("ga_max_generations");
    String popSize = appProps.getProperty("ga_popluation_size");
    String gaCrossOverRate = appProps.getProperty("ga_crossover_rate");
    String gaMutationRate = appProps.getProperty("ga_mutation_rate");

    // Check gatype can be either string representaion or binary based
    // This section is if you want to use a GA within Gem
    long start = System.currentTimeMillis();
    if (gaType.equals("StringBased") && args.length == 0) {
      target = App.appProps.getProperty("ga_target");
      populationSize = Integer.parseInt(popSize);
      evalScore = Float.parseFloat(gaEvalScore);
      maxGenerations = Integer.parseInt(gaGenString);
      crossOverRate = Float.parseFloat(gaCrossOverRate);
      mutationRate = Float.parseFloat(gaMutationRate);
      targetScore = (float) App.target.length() * App.evalScore;

      StringBasedGA gBasedGA = new StringBasedGA(gaMutationRate, populationSize, evalScore, maxGenerations,
          crossOverRate, mutationRate, targetScore);
      gBasedGA.runGA();
    }

    // If you want to use a hill climber to compare results instead send in args
    if (args.length != 0 && args[0].equals("Hill") && !args[1].isEmpty()) {
      System.out.println("Testing for args");
      target = App.appProps.getProperty("ga_target");
      Integer neighborHoodSize = Integer.parseInt(args[1]);
      HillClimber climber = new HillClimber(gaType, target, neighborHoodSize);
      climber.doHillClimibing();
    }

    if (args.length != 0 && args[0].equals("Random") && !args[1].isEmpty()) {
      target = App.appProps.getProperty("ga_target");
      Integer numberOfIterations = Integer.parseInt(args[1]);
      String randomStart = utils.generateRandomString(target.length());
      String ALLCHARS = utils.returnAllCharacters();
      for (int i = 0; i < numberOfIterations; i++) {
        System.out.println("Solution:" + randomStart);
        System.out.println("Count:" + i);
        
        Random ran = new Random();
        Integer index = ran.nextInt(target.length());
        if (randomStart.charAt(index) != target.charAt(index)) {
          Random randoms = new Random();
          Integer charGen = randoms.nextInt(ALLCHARS.length());
          char randomCharacter = ALLCHARS.charAt(charGen);
          String newS = randomStart.substring(0, index) + String.valueOf(randomCharacter)
              + randomStart.substring(index + 1, randomStart.length());
          randomStart = newS;
        }

        if (randomStart.equals(target)) {
          System.out.println("We've found a solution!: " + randomStart);
          System.out.println("Count:" + i);
          break;
        }
      }
    }
  
    long end = System.currentTimeMillis(); 

    System.out.println("Elapsed Time in milli seconds: "+ (end-start));
  }

}
