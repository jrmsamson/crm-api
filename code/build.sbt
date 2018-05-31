name := """crm-api"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies += guice

PlayKeys.fileWatchService := play.dev.filewatch.FileWatchService.polling(2000)