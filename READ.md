This application Streams any text file into Ignite cache.i.e Alice-in-wonderland.txt I used here.
you can use your by keeping the file in resource directory.

1.Install Ignite 2.6.0 and build it with maven.

2.Start Ignite by $IGNITE_HOME/bin/ignite.sh start example-ignite.xml

3.My custom example-ignite.xml is already kept in conf folder.Copy the entire and point to example that to start Ignite.

4.Clone the repo ignite-file-streamer

5.Build the repo:

mvn clean install

6.Run the application:

java -jar target/igniteFileStreaming-1.0-SNAPSHOT.jar

This will start the streaming of the given text file into Ignite cache named liblu1.

Now you can use the module igniteQueryWordCount in my other git located in to get the word count from the Ignite cache.



