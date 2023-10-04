(Please ensure you have maven installed before hand)
How to run the GA 
 - Open project in terminal of choice
 - Navigate to folder where **pom.xml** is located.
 - Modify application.properties file if needed.
 - Run `mvn package` to build project;
 - Run `java -cp target/gem-1.0.jar com.gem.App` to run the project and let the magic 

How to run Hill Climber
 - Same as Hill climber only difference is adding Hill after `com.gem.App`
 - Target cli should look like this `java -cp target/gem-1.0.jar com.gem.App Hill`