# Time Entry Application
<p>
This application was developed during the semester with the goal of applying most of the concepts seen in class.
</p>
<p>
The application is a simple time card management application. The user logs in with a Google account. Once the user is logged
in, a simple interface asks for a date to pick in a calendar, to mark te start of a laboral week. Then, a form is shown where
the user can select a project and a project goal or milestone, and enter the number of hours worked each day in a single row. 
If the user wants to add hours from another project or milestone, additional rows can be added to the form. When the user saves
this input, the data is persisted to Google's Datastore, and it can be viewed later clicking on the tab that doesn't contain
the form.
</p>
<p>
This project was developed in Eclipse using the <a href="https://developers.google.com/eclipse/">Google Plugin for Eclipse</a>.
With the project, you can generate a basic skeleton for the project, so that in order to run this project, you only need to
paste the sources in the src/ and war/ directories. With the Google Plugin you can also test the project right in your local
machine, and deploy it to Google App Engine.You will also need google's 
<a href="http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.googlecode.objectify%22">objectify</a> and 
<a href="https://github.com/google/guava/wiki/Release18">guava</a> jars to your build path.
</p>

You can also create a base project and deploy to google app engine using maven. For detalied instructions see this
<a href="https://cloud.google.com/appengine/docs/java/tools/maven">page</a>. Also, you need to add objectify and 
guava to the projest's pom.xml:
```xml
  <dependencies>
    <dependency>
      <groupId>com.googlecode.objectify</groupId>
      <artifactId>objectify</artifactId>
      <version>check for latest version</version>
    </dependency>
    <dependency>
      <groupId>com.google.guava</groupId>
      <artifactId>guava</artifactId>
      <version>12.0</version>
    </dependency>
  </dependencies>
```
