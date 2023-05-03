val scala3Version = "3.2.2"
val zioVersion = "2.0.13"

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
      "dev.zio" %% "zio-logging-slf4j" % "2.1.12",
      "dev.zio" %% "zio-http" % "3.0.0-RC1",
      "dev.zio" %% "zio-direct" % "1.0.0-RC7",
      "io.getquill" %% "quill-sql" % "4.6.0.1",
      "io.getquill" %% "quill-jdbc" % "4.6.0.1",
      "io.getquill" %% "quill-jdbc-zio" % "4.6.0.1",
      "io.getquill" %% "quill-util" % "4.6.0",
      "io.getquill" %% "quill-jasync" % "4.6.0.1",
      "dev.zio" %% "zio-test" % zioVersion,
      expecty,
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )

addCommandAlias("fmt", "scalafmt; Test / scalafmt;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
// addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
// addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")

