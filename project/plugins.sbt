resolvers += Resolver.typesafeRepo("releases")
// See available sbt 1.x plugins in https://github.com/sbt/sbt/wiki/sbt-1.x-plugin-migration
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.18")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
