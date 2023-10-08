(Please ensure you have maven installed before hand)

How to run the GA 
 - Open project in terminal of choice
 - Navigate to folder where `pom.xml` is located.
 - Modify `application.properties` file if needed.
 - Run `mvn clean install` to add needed dependencies
 - Run `mvn package` to build project;
 - Run `java -cp target/gem-1.0.jar com.gem.App` to run the project and let the magic happen

How to run Hill Climber
 - Same as GA only difference is adding Hill after `com.gem.App` and adding your neighbourhood size after
 - Target cli should look like this `java -cp target/gem-1.0.jar com.gem.App Hill 50`

 How to run Random Solution
 - Same as Hill climber only difference is adding Random after `com.gem.App` and your number of iterations
 - Target cli should look like this `java -cp target/gem-1.0.jar com.gem.App Random 4500`


 You can also run it with VSCode by having the following extension installed
 - Extension Pack for java (https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
 - Once its installed properly open up the `App.java` file and scroll to the `main` function
 - There should be a small `Run | Debug` text over it. Click on any one of them and it should start working.
 - If you want to use the Hill Climber or Random Solution. Create a folder called `.vscode` and add create a file called `launch.json` inside
 - The file should look like this:
```json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "args": [
            ],
            "mainClass": "com.gem.App",
            "projectName": "gem"
        }
    ]
}
```
 - Once that is done add `Hill` or `Random` as the first element in your `args` array and any whole number larger than 1 as the second element in your args array and run it.