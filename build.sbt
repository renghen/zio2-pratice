val zioVersion   = "2.0.15"
val quillVersion = "4.6.0.1"
val tapirVersion = "1.6.1"

val expectyRepo = "Expecty Repository" at "https://raw.github.com/pniederw/expecty/master/m2repo/"
val expecty     = "org.expecty" % "expecty" % "0.10"

resolvers += expectyRepo

lazy val root = project
  .in(file("."))
  .settings(BuildHelper.stdSettings)
  .settings(
    name         := "zio2-pratice",
    version      := "0.1.0",   
    libraryDependencies ++= Seq(
      "dev.zio"                       %% "zio"                           % zioVersion,
      "dev.zio"                       %% "zio-streams"                   % zioVersion,
      "dev.zio"                       %% "zio-logging-slf4j"             % "2.1.13",
      "dev.zio"                       %% "zio-http"                      % "3.0.0-RC2",
      "dev.zio"                       %% "zio-direct"                    % "1.0.0-RC7",
      "dev.zio"                       %% "zio-json"                      % "0.6.0",
      "org.json4s"                    %% "json4s-native"                 % "4.1.0-M3",
      "io.getquill"                   %% "quill-sql"                     % quillVersion,
      "io.getquill"                   %% "quill-jdbc"                    % quillVersion,
      "io.getquill"                   %% "quill-jdbc-zio"                % quillVersion,
      "io.getquill"                   %% "quill-util"                    % "4.6.0",
      "io.getquill"                   %% "quill-jasync"                  % quillVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-zio-http-server"         % tapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-prometheus-metrics"      % tapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-swagger-ui-bundle"       % tapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-json-zio"                % tapirVersion,
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % "3.8.16",
      "com.softwaremill.sttp.tapir"   %% "tapir-sttp-client"             % tapirVersion,
      "ch.qos.logback"                 % "logback-classic"               % "1.4.8",
      "dev.zio"                       %% "zio-logging"                   % "2.1.12",
      "dev.zio"                       %% "zio-logging-slf4j"             % "2.0.0",
      "dev.zio"                       %% "zio-test"                      % zioVersion,
      expecty,
      "org.scalameta"                 %% "munit"                         % "1.0.0-M8" % Test,
    ),
    // scalacOptions ++= Seq("-deprecation", "-feature")
  )

addCommandAlias("fmt", "scalafmt; Test / scalafmt;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
// addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
// addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")
