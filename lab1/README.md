# Verteilte Anwendungen Übungsaufgabe 1

Mögliche Punktzahl: 20 Punkte

## Deadlines

- 1. Zug: 4.5.2023
- 2. Zug: 2.5.2023

## Aufgabestellung
Mit dieser Aufgabe erhalten Sie ein Multi-Module Maven Projekt,
welches aus drei Submodulen besteht:
- **nginx** - Submodul für Aufgabe 1
- **quarkus** - Submodul für Aufgabe 2
- **wildfly** - Submodul für Aufgabe 3

Bevor Sie sich jedoch an die Bearbeitung der nachfolgenden Aufgaben machen,
stellen Sie bitte sicher, dass Sie dieses Projekt ohne Fehler mittels Maven bauen können.

```shell script
$mvn package
```

1.  **(5P)** Schreiben Sie ein Dockerfile mit dem Sie das [offizielle Image des NGINX von Docker Hub](https://hub.docker.com/_/nginx) erweitern. Erstellen Sie eine individuelle HTML Seite, die die Matrikelnummern der Gruppenteilnehmer darstellt und die Namen der Gruppenmitglieder als Meta Tag „author“ ausliefert. Starten Sie einen Container mit ihrem eigenen Image mittels ``$ docker run -d -p 8080:80 verteilte-anwendungen-nginx`` und stellen Sie sicher, dass die erstellte HTML Datei unter [http://localhost:8080/verteilte-anwendungen/test.html](http://localhost:8080/verteilte-anwendungen/test.html) von dem Container richtig ausgeliefert wird.
2.  **(5P)** Folgen Sie der Anleitung in [Quarkus Get Started](quarkus/README.md) und bauen Sie die Quarkus Applikation in verschiedenen Varianten. Wählen Sie anschließend eine Variante für sich aus und erstellen Sie ein entsprechendes Docker Image. Starten Sie die Quarkus Applikation innerhalb eines Containers und testen Sie die RESTful Webservices mit Hilfe eines REST Clients (z.B. curl oder Insomnia).
3.  **(5P)** Folgen Sie der Anleitung in [WildFly Get Started](wildfly/README.md) und deployen Sie die WAR Applikation im JBoss Application Server. Schreiben Sie anschließend ein Dockerfile mit dem Sie das [offizielle Image des WildFly von Quay.io](https://quay.io/repository/wildfly/wildfly) erweitern. Starten Sie einen Container mit ihrem eigenen Image mittels ``$ docker run -d -p 8080:80 verteilte-anwendungen-wildfly`` und stellen Sie sicher, dass die korrekte Implementierung unter [http://localhost:8080/verteilte-anwendung/rest/hello](http://localhost:8080/verteilte-anwendung/rest/hello) Sie mit Ihrem eigenen Namen begrüßt.
4.  **(5P)** Schreiben Sie ein Docker Compose file mit dem Sie die eigenen Images aus den Aufgaben 1 - 3 starten. Der NGINX Container soll über Port 8181 erreichbar sein. Der Quarkus Container soll über Port 8182 erreichbar sein. Und der WildFly Container soll über Port 8183 erreichbar sein. 


## Maven Get Started

`pom.xml` contains the Project Object Model (POM) for this project. The POM is the basic unit of work in Maven. This is important to remember because the POM contains every important piece of information about your project and is essentially one-stop-shopping for finding anything related to your project. Understanding the POM is important and new users are encouraged to refer to the [Maven Getting Started Guide](https://maven.apache.org/guides/getting-started/).


http://localhost:8181/verteilte-anwendungen/test.html
http://localhost:8182/
http://localhost:8183/verteilte-anwendung/rest/hello?name=Lennard
