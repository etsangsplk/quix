lazy val publishSettings = Seq(
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  pomExtra in ThisBuild :=
    <scm>
      <url>https://github.com/wix/quix.git</url>
      <connection>scm:git:https://github.com/wix/quix.git</connection>
    </scm>
      <developers>
        <developer>
          <id>frolovv</id>
          <name>Valery Frolov</name>
          <email>valeryf@wix.com</email>
          <organization>Wix</organization>
        </developer>
      </developers>
)

lazy val compileOptions = Seq(
  scalaVersion := "2.12.10",
  crossScalaVersions := Seq("2.12.10", "2.13.1"),
)

lazy val noPublish = Seq(publish := {}, publishLocal := {}, publishArtifact := false)

lazy val baseSettings =
  publishSettings ++
    compileOptions ++
    Seq(
      organization := "com.wix",
      homepage := Some(url("https://github.com/wix/quix")),
      licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
    )

val loggingDeps = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

val specs2Deps = Seq(
  "org.specs2" %% "specs2-core" % "4.8.3" % "test",
  "org.specs2" %% "specs2-junit" % "4.8.3" % "test",
  "org.specs2" %% "specs2-mock" % "4.8.3" % "test",
)

lazy val quixApi = (project in file("quix-api"))
  .settings(Seq(
    name := "Quix Api",
    libraryDependencies += "io.monix" %% "monix" % "3.1.0"
  ) ++ baseSettings)

lazy val quixCore = (project in file("quix-core"))
  .dependsOn(quixApi)
  .settings(Seq(
    name := "Quix Core",

    libraryDependencies += "com.github.blemale" %% "scaffeine" % "3.1.0",

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.10.2",

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
    libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.2",

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jdk8
    libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.10.2",

    // https://mvnrepository.com/artifact/io.prestosql/presto-parser
    libraryDependencies += "io.prestosql" % "presto-parser" % "329",

    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,

    // https://mvnrepository.com/artifact/com.wix/wix-embedded-mysql
    libraryDependencies += "com.wix" % "wix-embedded-mysql" % "4.6.1" % Test,

    // https://mvnrepository.com/artifact/mysql/mysql-connector-java
    libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.19" % Test,
  ) ++ baseSettings)

lazy val quixAthenaModule = (project in file("quix-modules/quix-athena-module"))
  .dependsOn(quixCore)
  .settings(Seq(
    name := "Quix Athena Module",

    // https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-athena
    libraryDependencies += "com.amazonaws" % "aws-java-sdk-athena" % "1.11.715",
    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,
  ) ++ baseSettings)

lazy val quixBigqueryModule = (project in file("quix-modules/quix-bigquery-module"))
  .dependsOn(quixCore)
  .settings(Seq(
    name := "Quix Bigquery Module",

    // https://mvnrepository.com/artifact/com.google.cloud/google-cloud-bigquery
    libraryDependencies += "com.google.cloud" % "google-cloud-bigquery" % "1.106.0",
    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,
  ) ++ baseSettings)

lazy val quixDummyModule = (project in file("quix-modules/quix-dummy-module"))
  .dependsOn(quixCore)
  .settings(Seq(
    name := "Quix Dummy Module",

    libraryDependencies += "io.monix" %% "monix" % "3.1.0",
    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,
  ) ++ baseSettings ++ noPublish)

lazy val quixJdbcModule = (project in file("quix-modules/quix-jdbc-module"))
  .dependsOn(quixCore)
  .settings(Seq(
    name := "Quix Jdbc Module",

    libraryDependencies += "io.monix" %% "monix" % "3.1.0",
    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,
    // https://mvnrepository.com/artifact/com.wix/wix-embedded-mysql
    libraryDependencies += "com.wix" % "wix-embedded-mysql" % "4.6.1" % Test,

    // https://mvnrepository.com/artifact/mysql/mysql-connector-java
    libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.19" % Test,
  ) ++ baseSettings)

lazy val quixPrestoModule = (project in file("quix-modules/quix-presto-module"))
  .dependsOn(quixCore)
  .settings(Seq(
    name := "Quix Presto Module",

    libraryDependencies += "io.monix" %% "monix" % "3.1.0",
    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,
    // https://mvnrepository.com/artifact/io.prestosql/presto-parser
    libraryDependencies += "io.prestosql" % "presto-parser" % "329",
    // https://mvnrepository.com/artifact/io.prestosql/presto-client
    libraryDependencies += "io.prestosql" % "presto-client" % "329",
    // https://mvnrepository.com/artifact/org.scalaj/scalaj-http
    libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2",
  ) ++ baseSettings)

lazy val quixPythonModule = (project in file("quix-modules/quix-python-module"))
  .dependsOn(quixCore)
  .settings(Seq(
    name := "Quix Python Module",

    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,
    // https://mvnrepository.com/artifact/com.zaxxer/nuprocess
    libraryDependencies += "com.zaxxer" % "nuprocess" % "1.2.6",
    // https://mvnrepository.com/artifact/net.sf.py4j/py4j
    libraryDependencies += "net.sf.py4j" % "py4j" % "0.10.9",
  ) ++ baseSettings)

lazy val quixWebSpring = (project in file("quix-webapps/quix-web-spring"))
  .dependsOn(quixCore,
    quixAthenaModule,
    quixBigqueryModule,
    quixDummyModule,
    quixJdbcModule,
    quixPrestoModule,
    quixPythonModule)
  .settings(Seq(
    name := "Quix Web Spring",

    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= specs2Deps,

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    libraryDependencies += "org.springframework.boot" % "spring-boot-starter-web" % "2.2.4.RELEASE",

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-jetty
    libraryDependencies += "org.springframework.boot" % "spring-boot-starter-jetty" % "2.2.4.RELEASE",

    // https://mvnrepository.com/artifact/com.pauldijou/jwt-core
    libraryDependencies += "com.pauldijou" %% "jwt-core" % "4.2.0",

    // https://mvnrepository.com/artifact/org.springframework/spring-websocket
    libraryDependencies += "org.springframework" % "spring-websocket" % "5.2.3.RELEASE",

    // https://mvnrepository.com/artifact/org.eclipse.jetty.websocket/websocket-common
    libraryDependencies += "org.eclipse.jetty.websocket" % "websocket-common" % "9.4.26.v20200117",

    // https://mvnrepository.com/artifact/org.eclipse.jetty.websocket/websocket-api
    libraryDependencies += "org.eclipse.jetty.websocket" % "websocket-api" % "9.4.26.v20200117",

    // https://mvnrepository.com/artifact/org.eclipse.jetty.websocket/websocket-server
    libraryDependencies += "org.eclipse.jetty.websocket" % "websocket-server" % "9.4.26.v20200117",

    // https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api
    libraryDependencies += "javax.servlet" % "javax.servlet-api" % "4.0.1" % "provided",

    // https://mvnrepository.com/artifact/com.typesafe/config
    libraryDependencies += "com.typesafe" % "config" % "1.4.0",

    // https://mvnrepository.com/artifact/org.springframework/spring-jdbc
    libraryDependencies += "org.springframework" % "spring-jdbc" % "5.2.3.RELEASE",

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
    libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.2",

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    libraryDependencies += "org.springframework.boot" % "spring-boot-starter-test" % "2.2.4.RELEASE" % Test,

    // https://mvnrepository.com/artifact/org.scalatest/scalatest
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  ) ++ baseSettings ++ noPublish)