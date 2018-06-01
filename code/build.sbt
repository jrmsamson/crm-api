
name := """crm-api"""

organization := "com.crmapi"

version := "1.0-SNAPSHOT"

sbtPlugin := true

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

lazy val postgresql = "42.2.2"
lazy val jooqVersion = "3.10.7"

libraryDependencies ++= Seq(
  ws,
  jdbc,
  guice,
  evolutions,
  filters,
  "org.postgresql"  %     "postgresql"      %   postgresql     withSources() withJavadoc(),
  "org.jooq"        %     "jooq"            %   jooqVersion    withSources() withJavadoc(),
  "org.jooq"        %     "jooq"            %   jooqVersion    withSources() withJavadoc(),
  "org.jooq"        %     "jooq-meta"       %   jooqVersion    withSources() withJavadoc(),
  "org.jooq"        %     "jooq-codegen"    %   jooqVersion    withSources() withJavadoc(),
)

PlayKeys.fileWatchService := play.dev.filewatch.FileWatchService.polling(2000)

// **************************************************
// Task to generate the JOOQ model from the DB tables
// **************************************************



val generateJooqModelsTask = Def.task {
  val src = sourceManaged.value
  val cp = (fullClasspath in Compile).value
  val r = (runner in Compile).value
  val s = streams.value
  r.run("org.jooq.util.GenerationTool", cp.files, Array("jooq.xml"), s.log)
  ((src / "main/generated") ** "*.java").get
}

val generateJooqModels = taskKey[Seq[File]]("Generate jOOQ Models")
generateJooqModels := generateJooqModelsTask.value

unmanagedSourceDirectories in Compile += sourceManaged.value / "main/generated"

// **************************************************************
// Avoid adding the Javadocs when generating a distributable file
// **************************************************************

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false