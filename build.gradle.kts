plugins {
  id("java")
  id("org.jetbrains.intellij.platform") version "2.4.0"
}

group = "com.ivanovych666.intellij.plugin.jsonsorter"
version = "2.1.2"

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
dependencies {
  intellijPlatform {
    create("IC", "2022.3.3")
  }
}

intellijPlatform {
  pluginVerification {
    freeArgs = listOf(
      "-mute",
      "TemplateWordInPluginId"
    )
    ides {
      recommended()
    }
  }
}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }

  patchPluginXml {
    sinceBuild.set("223")
    untilBuild.set("251.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
