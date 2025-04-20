@file:Suppress("UNCHECKED_CAST")

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.bundling.Jar
import org.sonarqube.gradle.SonarExtension
import java.io.PrintWriter
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.function.BiConsumer

buildscript {
    repositories { mavenCentral() }
    dependencies { classpath("org.netbeans.tools:sigtest-maven-plugin:1.5") }
}

plugins {
    `java-platform`
    checkstyle
    pmd
    jacoco
    signing
    `maven-publish`
    id("com.diffplug.spotless") version "6.23.3" apply false
    id("me.champeau.jmh") version "0.7.2" apply false
    id("org.sonarqube") version "4.3.1.3277" apply false
}

description = "ASM, a very small and fast Java bytecode manipulation framework"

// Root project configuration
javaPlatform { allowDependencies() }

dependencies {
    constraints {
        api(project(":asm"))
        api(project(":asm-tree"))
        api(project(":asm-analysis"))
        api(project(":asm-util"))
        api(project(":asm-commons"))
    }
}

allprojects {
    group = "org.ow2.asm"
    version = "9.9" + if (rootProject.hasProperty("release")) "" else "-SNAPSHOT"
}

subprojects {
    repositories { mavenCentral() }

    apply(plugin = "java-library")
    apply(plugin = "jacoco")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    extra["provides"] = mutableListOf<String>()
    extra["requires"] = mutableListOf<String>()
    extra["depends"] = mutableListOf<String>()

    val provides: MutableList<String> by extra
    val requires: MutableList<String> by extra
    val depends: MutableList<String> by extra

    extra["transitiveRequires"] = (extra["requires"] as List<String>)
        .map { project(it) }
        .flatMap { subProj ->
            val transitive = subProj.extra["transitiveRequires"] as () -> Set<String>
            val provides1 = (subProj.extra["provides"] as List<String>).first()
            transitive().plus(provides1)
        }
        .toSet()

    extra["transitiveImports"] = (extra["requires"] as List<String>)
        .map { project(it) }
        .flatMap { subProj ->
            val transitive = subProj.extra["transitiveImports"] as () -> Set<String>
            val provides1 = subProj.extra["provides"] as List<String>
            transitive().plus(provides1)
        }
        .toSet()

    // Some external dependencies (such as Jacoco) depend transitively on ASM, and
    // without this rule Gradle can mix ASM jars of different versions (e.g.
    // asm-6.0.jar with the asm-tree.jar built locally).
    configurations.all { resolutionStrategy.preferProjectModules() }
}

// -----------------------------------------------------------------------------
// Project descriptions
// -----------------------------------------------------------------------------

project(":asm") {
    description = rootProject.description
    extra["provides"] = mutableListOf("org.objectweb.asm", "org.objectweb.asm.signature", "dev.oblivruin.asm")
}

project(":asmx") {
    description = "AsmX"
    group = "dev.oblivruin.asmx"
    extra["provides"] = mutableListOf("dev.oblivruin.asmx")
}

project(":asm-analysis") {
    description = "Static code analysis API of ${rootProject.description}"
    extra["provides"] = mutableListOf("org.objectweb.asm.tree.analysis")
    extra["requires"] = mutableListOf(":asm-tree", ":asm")
}

project(":asm-commons") {
    description = "Usefull class adapters based on ${rootProject.description}"
    extra["provides"] = mutableListOf("org.objectweb.asm.commons")
    extra["requires"] = mutableListOf(":asm", ":asm-tree")
    val testImplementation by configurations
    dependencies {
        testImplementation(project(":asm-util"))
    }
}

project(":asm-test") {
    description = "Utilities for testing ${rootProject.description}"
    extra["provides"] = mutableListOf("org.objectweb.asm.test")

    extra["depends"] = mutableListOf(
        "org.junit.jupiter:junit-jupiter-api:5.10.1",
        "org.junit.jupiter:junit-jupiter-params:5.10.1")
}

project(":asm-tree") {
    description = "Tree API of ${rootProject.description}"
    extra["provides"] = mutableListOf("org.objectweb.asm.tree")
    extra["requires"] = mutableListOf(":asm")
}

project(":asm-util") {
    description = "Utilities for ${rootProject.description}"
    extra["provides"] = mutableListOf("org.objectweb.asm.util")

    extra["requires"] = mutableListOf(":asm", ":asm-tree", ":asm-analysis")

    val testImplementation by configurations
    dependencies { testImplementation("org.codehaus.janino:janino:3.1.9") }
}

// Use "gradle benchmarks:jmh [-PjmhInclude='<regex>']" to run the benchmarks.
project(":benchmarks") {
    description = "Benchmarks for ${rootProject.description}"
    apply(plugin = "me.champeau.jmh")
    val implementation by configurations
    val jmh by configurations
    dependencies {
        implementation(files("libs/csg-bytecode-1.0.0.jar", "libs/jclasslib.jar"))
        jmh(project(":asm"))
        jmh(project(":asm-tree"))
    }
    extra["depends"] = mutableListOf(
        "kawa:kawa:1.7",
        "net.sf.jiapi:jiapi-reflect:0.5.2",
        "net.sourceforge.serp:serp:1.15.1",
        "org.apache.bcel:bcel:6.0",
        "org.aspectj:aspectjweaver:1.8.10",
        "org.cojen:cojen:2.2.5",
        "org.javassist:javassist:3.21.0-GA",
        "org.mozilla:rhino:1.7.7.1"
    )
    listOf("4.0", "5.0.1", "6.0", "7.0", "8.0.1", "9.0").forEach { version ->
        val asmConfig = configurations.create("asm$version")
        dependencies.add("asm$version", "org.ow2.asm:asm:$version@jar")
        dependencies.add("asm$version", "org.ow2.asm:asm-tree:$version@jar")

        tasks.register<Copy>("asm$version") {
            from(asmConfig.map { zipTree(it) })
            into("${layout.buildDirectory.get().asFile}/asm$version")
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
        tasks.named("classes") { dependsOn("asm$version") }
    }

    val j11ClsCfg = configurations.create("input-classes-java11")
    dependencies.add("input-classes-java11", "io.vavr:vavr:0.10.0@jar")

    tasks.register<Copy>("copyInputClasses") {
        from(j11ClsCfg.map { zipTree(it) })
        into("${layout.buildDirectory.get().asFile}/input-classes-java11")
    }
    tasks.named("classes") { dependsOn("copyInputClasses") }

    configure<me.champeau.jmh.JmhParameters> {
        jvmArgs.add("-Duser.dir=${rootDir}")
        resultFormat.set("CSV")
        profilers.set(listOf("org.objectweb.asm.benchmarks.MemoryProfiler"))
        if (rootProject.hasProperty("jmhInclude")) {
            includes.set(listOf(rootProject.property("jmhInclude") as String))
        }
    }
}

project(":tools") {
    description = "Tools used to build ${rootProject.description}"
}

project(":tools:retrofitter") {
    description = "JDK 1.5 class retrofitter based on ${rootProject.description}"

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets.named("main").configure {
        java.srcDirs(project(":asm").sourceSets.named("main").get().java.srcDirs)
    }
}
// All projects are checked with googleJavaFormat, Checkstyle and PMD,
// and tested with :asm-test and JUnit.
subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<SpotlessExtension> {
        java {
            target("**/*.java")
            targetExclude("src/resources/java/**/*")
            googleJavaFormat("1.18.1")
        }
    }

    // Check the coding style with Checkstyle. Fail in case of error or warning.
    apply(plugin = "checkstyle")
    configure<CheckstyleExtension> {
        configFile = rootProject.layout.projectDirectory.file("tools/checkstyle.xml").asFile
        maxErrors = 0
        maxWarnings = 0
    }

    // Check the code with PMD.
    apply(plugin = "pmd")
    configure<PmdExtension> {
        ruleSets = emptyList<String>()
        ruleSetFiles = files(rootProject.layout.projectDirectory.file("tools/pmd.xml"))
        isConsoleOutput = true
    }
    tasks.named("pmdMain") { dependsOn(":asm:jar") }
    tasks.named("pmdTest") { dependsOn(":asm:jar") }

    val requires: List<String> by extra
    val depends: List<String> by extra
    dependencies {
        requires.forEach { projectName ->
            api(project(projectName))
        }
        depends.forEach { name ->
            api(name)
        }

        val testRuntimeOnly by configurations
        val testImplementation by configurations
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
        testImplementation(project(":asm-test"))
    }

    // Produce byte-for-byte reproducible archives.
    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        dirMode = "775".toInt(8)
        fileMode = "664".toInt(8)
    }

    // Configure the projects with a non-empty 'provides' property. They must be
    // checked for code coverage and backward compatibility, retrofitted to Java 1.5,
    // and packaged with generated module-info classes.
    if (extra.has("provides")) {
        val provides = extra["provides"] as MutableList<String>
        if (provides.isNotEmpty()) {
            configure<JacocoPluginExtension> {
                toolVersion = "0.8.12"
            }
            tasks.named<JacocoReport>("jacocoTestReport") {
                reports { xml.required.set(true) }
                classDirectories.setFrom(sourceSets["main"].output.classesDirs)
            }
            tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
                classDirectories.setFrom(sourceSets["main"].output.classesDirs)
                violationRules {
                    rule {
                        limit {
                            minimum = "0.95".toBigDecimal()
                            counter = "INSTRUCTION"
                        }
                    }
                }
                dependsOn(
                    ":asm:jar",
                    ":asm-tree:jar",
                    ":asm-commons:jar"
                )
            }
            tasks.named("check") { dependsOn("jacocoTestCoverageVerification") }

            // Retrofit the code in-place to Java 1.5 and generate a module-info class
            // from the code content, in compileJava.doLast.
            if (name != "asm-test") {
                tasks.named("compileJava") {
                    dependsOn(":tools:retrofitter:classes")
                    doLast {
                        val retrofitterClasspath = project(":tools:retrofitter")
                            .sourceSets["main"].runtimeClasspath
                        val classLoader = URLClassLoader(
                            retrofitterClasspath.files.map { it.toURI().toURL() }.toTypedArray()
                        )
                        val retrofitter = classLoader
                            .loadClass("org.objectweb.asm.tools.Retrofitter")
                            .getDeclaredConstructor()
                            .newInstance() as BiConsumer<Path, Any>

                        val classesDir = sourceSets["main"].output.classesDirs.singleFile.toPath()

                        retrofitter.accept(classesDir, version.toString() as Any)
                        if (extra.has("transitiveRequires")) {
                            val requires0 = (extra["transitiveRequires"] as () -> Set<String>)().toList()
                            if (requires0.isNotEmpty()) {
                                retrofitter.accept(classesDir, arrayOf(
                                    version.toString(),
                                    provides,
                                    requires0))
                            }
                        }
                    }
                }
            }

            // Create one backward compatibility checking task for each 'sigtest-*' file
            // in test/resources, and make the 'check' task depend on all these tasks.
            val testResourcesDir = file("src/test/resources")
            if (testResourcesDir.exists()) {
                testResourcesDir.listFiles { file ->
                    file.name.matches(Regex("sigtest-.*"))
                }?.forEach { sigFile ->
                    tasks.register(sigFile.name) {
                        group = "Verification"
                        dependsOn("classes")
                        inputs.files(sigFile, sourceSets["main"].allJava)
                        outputs.file("${layout.buildDirectory.get()}/${sigFile.name}")

                        doLast {
                            val sigTest = com.sun.tdk.signaturetest.SignatureTest()

                            val args = listOf(
                                "-ApiVersion", version.toString(),
                                "-Backward",
                                "-Static",
                                "-Mode", "bin",
                                "-FileName", sigFile.absolutePath,
                                "-Classpath", project(":tools").file("jdk8-api.jar").path +
                                        File.pathSeparator + sourceSets["main"].output.classesDirs.asPath,
                                "-Package"
                            ) + provides

                            sigTest.run(args.toTypedArray(),
                                File(outputs.files.singleFile.absolutePath).printWriter(Charsets.UTF_8),
                                null)

                            if (!sigTest.isPassed) {
                                throw GradleException("Signature test failed for ${sigFile.name}")
                            }
                        }
                    }
                    tasks.named("check") { dependsOn(sigFile.name) }
                }

                // Define a task to create a sigtest file for the current version.
                tasks.register("buildSigtest") {
                    group = "Build"
                    dependsOn("classes")
                    inputs.files(sourceSets["main"].allJava)
                    outputs.file("src/test/resources/sigtest-${version}.txt")

                    doLast {
                        val setup = com.sun.tdk.signaturetest.Setup()

                        val args = listOf(
                            "-ApiVersion", version.toString(),
                            "-FileName", outputs.files.singleFile.absolutePath,
                            "-Classpath", listOf<String?>(
                                project(":tools").file("jdk8-api.jar").absolutePath,
                                sourceSets["main"].output.classesDirs.asPath,
                                configurations["compileClasspath"].asPath
                            ).joinToString(File.pathSeparator),
                            "-Package"
                        ) + provides

                        setup.run(args.toTypedArray(), PrintWriter(System.err, true), null)

                        if (!setup.isPassed) {
                            throw GradleException("Failed to generate sigtest")
                        }
                    }
                }
            }

            // Manifest configure
            tasks.named<Jar>("jar") {
                manifest {
                    attributes(
                        "Implementation-Title" to project.description,
                        "Implementation-Version" to version
                    )
                    if (name != "asm-test") {
                        val imports = if (extra.has("transitiveImports")) extra["transitiveImports"] as Set<String> else setOf()

                        attributes(
                            "Bundle-DocURL" to "http://asm.ow2.org",
                            "Bundle-License" to "BSD-3-Clause;link=https://asm.ow2.io/LICENSE.txt",
                            "Bundle-ManifestVersion" to "2",
                            "Bundle-Name" to provides.first(),
                            "Bundle-RequiredExecutionEnvironment" to "J2SE-1.5",
                            "Bundle-SymbolicName" to provides.first(),
                            "Bundle-Version" to version,
                            "Export-Package" to provides.joinToString(",") {
                                "$it;version=\"$version\""
                            } + if (imports.isNotEmpty()) ";uses:=\"${imports.joinToString(",")}\"" else ""
                        )

                        if (imports.isNotEmpty()) {
                            attributes(
                                "Import-Package" to imports.joinToString(",") {
                                    "$it;version=\"$version\""
                                },
                                "Module-Requires" to (extra["transitiveRequires"] as () -> Set<String>)()
                                    .joinToString(",") { "$it;transitive=true" }
                            )
                        }
                    }
                }
            }

            // Apply the SonarQube plugin to monitor the code quality of the project.
            // Use with 'gradlew sonar -Dsonar.host.url=https://sonarqube.ow2.org'.
            apply(plugin = "org.sonarqube")
            configure<SonarExtension> {
                properties { property("sonar.projectKey", "ASM:${project.name}") }
            }

            // Add a task to generate a private javadoc and add it as a dependency of the
            // 'check' task.
            tasks.register<Javadoc>("privateJavadoc") {
                source = sourceSets["main"].allJava
                classpath = configurations["compileClasspath"]

                setDestinationDir(file("${destinationDir}-private"))
                (options as CoreJavadocOptions).apply {
                    memberLevel = JavadocMemberLevel.PRIVATE
                    addBooleanOption("Xdoclint:all,-missing", true)
                }
            }
            tasks.named("check") { dependsOn("privateJavadoc") }

            // Add tasks to generate the Javadoc and a source jar, to be uploaded to Maven
            // together with the main jar (containing the compiled code).
            tasks.register<Jar>("javadocJar") {
                archiveClassifier.set("javadoc")
                from(destinationDirectory)
            }

            tasks.register<Jar>("sourcesJar") {
                archiveClassifier.set("sources")
                from(sourceSets["main"].allSource)
            }

            configure<JavaPluginExtension> {
                withJavadocJar()
                withSourcesJar()
            }
        }
    }
}

configure(subprojects.filter { (it.extra["provides"] as MutableList<String>).isNotEmpty() }) {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    publishing {
        repositories {
            maven {
                name = "nexus"
                url = uri(if (rootProject.hasProperty("release"))
                    "https://repository.ow2.org/nexus/service/local/staging/deploy/maven2"
                else "https://repository.ow2.org/nexus/content/repositories/snapshots")
                credentials {
                    username = System.getenv("NEXUS_USER_NAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }
        }
        publications.create<MavenPublication>("maven") {
            val isRoot = project == rootProject
            artifactId = if (isRoot) "asm-bom" else project.name

            from(components.getByName(if (isRoot) "javaPlatform" else "java"))

            pom.withXml {
                val parentNode = asNode().appendNode("parent")
                parentNode.appendNode("groupId", "org.ow2")
                parentNode.appendNode("artifactId", "ow2")
                parentNode.appendNode("version", "1.5.1")
            }

            pom {
                name.set(artifactId)
                description.set(project.description)
                packaging = if (isRoot) "pom" else "jar"
                inceptionYear.set("2000")

                licenses {
                    license {
                        name.set("BSD-3-Clause")
                        url.set("https://asm.ow2.io/license.html")
                    }
                }

                url.set("http://asm.ow2.io/")

                mailingLists {
                    mailingList {
                        name.set("ASM Users List")
                        subscribe.set("https://mail.ow2.org/wws/subscribe/asm")
                        post.set("asm@objectweb.org")
                        archive.set("https://mail.ow2.org/wws/arc/asm/")
                    }
                    mailingList {
                        name.set("ASM Team List")
                        subscribe.set("https://mail.ow2.org/wws/subscribe/asm-team")
                        post.set("asm-team@objectweb.org")
                        archive.set("https://mail.ow2.org/wws/arc/asm-team/")
                    }
                }

                issueManagement {
                    url.set("https://gitlab.ow2.org/asm/asm/issues")
                }

                scm {
                    connection.set("scm:git:https://gitlab.ow2.org/asm/asm/")
                    developerConnection.set("scm:git:https://gitlab.ow2.org/asm/asm/")
                    url.set("https://gitlab.ow2.org/asm/asm/")
                }

                developers {
                    developer {
                        name.set("Eric Bruneton")
                        id.set("ebruneton")
                        email.set("ebruneton@free.fr")
                        roles.set(listOf("Creator", "Java Developer"))
                    }
                    developer {
                        name.set("Eugene Kuleshov")
                        id.set("eu")
                        email.set("eu@javatx.org")
                        roles.set(listOf("Java Developer"))
                    }
                    developer {
                        name.set("Remi Forax")
                        id.set("forax")
                        email.set("forax@univ-mlv.fr")
                        roles.set(listOf("Java Developer"))
                    }
                    developer {
                        name.set("OblivRuinDev")
                        id.set("oblivruindev")
                        email.set("oblivruindev@qq.com")
                        roles.set(listOf("AsmX Fork Creator", "Java Developer"))
                    }
                }

                organization {
                    name.set("OW2")
                    url.set("http://www.ow2.org/")
                }
            }
        }
    }

    signing {
        isRequired = rootProject.hasProperty("release")
        sign(publishing.publications["maven"])
    }

    tasks.withType<GenerateModuleMetadata> { enabled = false }
}
