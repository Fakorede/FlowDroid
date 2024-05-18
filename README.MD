# FlowDroid

### Requirements

* JDK 11 or above
* Maven
* Android Apk(s) to run the tool on

### Building The Tool With Maven


At the first time, FlowDroid needs to be built from the parent module, i.e. the project's root folder. The full test
suite takes around 30 minutes, so we recommend to disable the tests when building:
```shell
mvn install -DskipTests
```

### Running The Command-Line Tool

If you want to use the command-line tool to run the data flow tracker, you can use the following command:

```shell
java -jar soot-infoflow-cmd/target/soot-infoflow-cmd-jar-with-dependencies.jar \
    -a <APK File> \
    -p <Android JAR folder> \
```

The Android JAR folder is the "platforms" directory inside your Android SDK installation folder.

You need to replace the placeholder ``androidJarFolder`` with the location of the
``platforms`` directory in your Android SDK installation. 

The placeholder ``apkPath`` refers to the full file path of the APK file.

For finding out about the other options of the command-line tool, you can run the tool with the `--help` option or have a look at
the `MainClass.initializeCommandLineOptions()` method in the source code (module soot-infoflow-cmd).

### Configuring FlowDroid for Performance

For some apps, FlowDroid will take very long for large apps. There are various options with which you can configure a tradeoff between performance, precision and recall.

* ```-ns``` Do not track taints on static fields and disregard static initializers.
* ```-ne``` Do not track exceptional flows.

You can also define timeouts:

* ```-dt N``` Aborts the data flow analysis after N seconds and returns the results obtained so far.
* ```-ct N``` Aborts the callback collection during callgraph construction after N seconds and continues with the (incomplete) callgraph constructed so far.
* ```-rt N``` Aborts the result collection after N seconds and returns the results obtained so far.

Note that timeouts are additive. All three stages must complete or run into a timeout for the tool to return and deliver results.


## License

FlowDroid is licensed
under the LGPL license, see LICENSE file. This basically means that you are free to use the tool (even in commercial, closed-source
projects). However, if you extend or modify the tool, you must make your changes available under the LGPL as well. This ensures that
we can continue to improve the tool as a community effort.
