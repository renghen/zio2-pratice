val scala3Version = "3.3.0"
val zioVersion = "2.0.15"
val quillVersion = "4.6.0.1"

val expectyRepo = "Expecty Repository" at "https://raw.github.com/pniederw/expecty/master/m2repo/"
val expecty = "org.expecty" % "expecty" % "0.10"

resolvers += expectyRepo

lazy val root = project
  .in(file("."))
  .settings(BuildHelper.stdSettings)
  .settings(
    name := "zio2-pratice",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-logging-slf4j" % "2.1.13",
      "dev.zio" %% "zio-http" % "3.0.0-RC2",
      "dev.zio" %% "zio-direct" % "1.0.0-RC7",
      "dev.zio" %% "zio-json" % "0.5.0",
      "org.json4s" %% "json4s-native" % "4.1.0-M3",
      "io.getquill" %% "quill-sql" % quillVersion,
      "io.getquill" %% "quill-jdbc" % quillVersion,
      "io.getquill" %% "quill-jdbc-zio" % quillVersion,
      "io.getquill" %% "quill-util" % "4.6.0",
      "io.getquill" %% "quill-jasync" % quillVersion,
      "dev.zio" %% "zio-test" % zioVersion,
      expecty,
      "org.scalameta" %% "munit" % "1.0.0-M8" % Test
    )
  )

addCommandAlias("fmt", "scalafmt; Test / scalafmt;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
// addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
// addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")

