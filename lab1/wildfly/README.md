# WildFly Get Started

This project uses WildFly, a powerful, modular, & lightweight application server
that helps you build amazing applications.

If you want to learn more about WildFly, please visit its website: 
https://www.wildfly.org/ .

## Installing and starting the WildFly server

First, WildFly must be downloaded and is available as different deliverables
at https://www.wildfly.org/downloads/ . 

Next, we need to choose a location for WildFly to live. By default, WildFly will be extracted into wildfly-27.x.x.x (where 27.x.x.x matches the version you downloaded):

```shell script
unzip wildfly-27.x.x.x.zip
```

Alternatively, you can use the version that is downloaded with the first
maven build and is placed at `wildfly/target/wildfly-27.x.x.x/`.

Now, let’s start WildFly in standalone mode:

```shell script
wildfly-27.x.x.x/bin/standalone.sh
```

> **_NOTE:_**  If you want to stop WildFly, simply press Crtl-C whilst the terminal has focus.

That’s it, WildFly is installed and running! Visit http://localhost:8080/ to check the server has started properly.


## Packaging and deploying the application

The next thing to understand is the layout of the distribution and to explore the server directory structure, key configuration files, log files, user deployments and so on.
For this we recommend [a Quick Tour of the Getting Started Guide](https://docs.wildfly.org/27/Getting_Started_Guide.html#wildfly---a-quick-tour). 

The application can be packaged using:
```shell script
$mvn package
```
It produces the `verteilte-anwendung.war` file in the `target/` directory.
The application is now deployable using the deployment scanner.

If you want to deploy your application, copy your deployment content by executing the following command:
```shell script
$cp target/verteilte-anwendung.war target/wildfly-27.0.1.Final/standalone/deployments
```

> **_NOTE:_**  The server’s management API is recommended for installing deployment content. File system based deployment scanning capabilities remain for developer convenience.

