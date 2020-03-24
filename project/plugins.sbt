val isWindows: Boolean = "win".r.findFirstMatchIn(System.getProperty("os.name").toLowerCase).isDefined

if(isWindows) libraryDependencies ++= Nil
else addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")