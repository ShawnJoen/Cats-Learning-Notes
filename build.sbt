name := "Cats-Learning-Notes"

version := "0.1"

scalaVersion := "2.13.8"

lazy val compilerOptions = Seq(
  "-Ypartial-unification",
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonDependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.6.0"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  libraryDependencies ++= commonDependencies
)

lazy val global = project.in(file("."))
  .aggregate(
    chapter01,
    chapter02
  )

lazy val chapter01 = project.in(file("chapter01"))
  .settings(commonSettings: _*)

lazy val chapter02 = project.in(file("chapter02"))
  .settings(commonSettings: _*)
