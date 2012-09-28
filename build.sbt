import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

organization := "com.github.mystelynx"

name := "nekopic"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-filter" % "0.6.4",
  "net.databinder" %% "unfiltered-netty" % "0.6.4",
  "net.databinder" %% "unfiltered-netty-server" % "0.6.4",
  "net.databinder" %% "unfiltered-oauth" % "0.6.4",
  "net.databinder" %% "unfiltered-json" % "0.6.4",
  "org.mockito" % "mockito-all" % "1.9.5-rc1" % "test",
  "org.powermock" % "powermock-core" % "1.4.12" % "test",
  "org.powermock" % "powermock-api-mockito" % "1.4.12" % "test",
  "org.fusesource.scalate" % "scalate-core" % "1.5.3",
  "org.clapper" %% "avsl" % "0.4",
  "net.databinder" %% "unfiltered-spec" % "0.6.4" % "test",
  "net.databinder" %% "unfiltered-scalatest" % "0.6.4" % "test",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.0.RC4" % "container",
  "javax" % "javaee-web-api" % "6.0" % "provided"
)

resolvers ++= Seq(
  "java m2" at "http://download.java.net/maven/2"
)

seq(webSettings :_*)

seq(coffeeSettings :_*)

seq(lessSettings :_*)

(resourceManaged in (Compile, CoffeeKeys.coffee)) <<= (resourceManaged in Compile)(_ / "www" / "js")

(resourceManaged in (Compile, LessKeys.less)) <<= (resourceManaged in Compile)(_ / "www" / "css")
