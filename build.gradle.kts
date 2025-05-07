// ---------------------------------------------------------------------
// ORIGINAL WORK:
// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom (https://asm.ow2.io/)
// All rights reserved.
//
// Distributed under the BSD-3-Clause License
// ---------------------------------------------------------------------

// ---------------------------------------------------------------------
// MODIFIED WORK:
// ASMX: Extended bytecode manipulation toolkit based on ASM
// Copyright (c) 2025 OblivRuinDev
// Modifications: See git commits for details
//
// Distributed under the BSD-3-Clause License, preserving original terms
// for ASM code.
// ---------------------------------------------------------------------

// For full license terms, see the project's LICENSE file.
@file:Suppress("UNCHECKED_CAST")

import com.diffplug.gradle.spotless.SpotlessExtension
import com.sun.tdk.signaturetest.Setup
import com.sun.tdk.signaturetest.SignatureTest
import me.champeau.jmh.JmhParameters
import org.gradle.api.tasks.bundling.Jar
import org.sonarqube.gradle.SonarExtension
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.FileOutputStream
import java.io.PrintWriter

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

description = "AsmX, extended bytecode manipulation toolkit based on ASM"
version = "0.2-PREVIEW"

// Root project configuration
javaPlatform { allowDependencies() }

allprojects {
    group = "org.ow2.asm"
    version = rootProject.version
}

subprojects {
    repositories { mavenCentral() }

    apply(plugin = "java-library")
    apply(plugin = "jacoco")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    // Some external dependencies (such as Jacoco) depend transitively on ASM, and
    // without this rule Gradle can mix ASM jars of different versions (e.g.
    // asm-6.0.jar with the asm-tree.jar built locally).
    configurations.all { resolutionStrategy.preferProjectModules() }
}

// -----------------------------------------------------------------------------
// Project descriptions
// -----------------------------------------------------------------------------

val compileDesc = project(":compile-desc") {
    description = "A API to adjust compilation behavior"
    setCfg()
}

val asmTest = project(":asm-test") {
    description = "Utilities for testing ${rootProject.description}"
    setCfg(arrayOf("org.objectweb.asm.test"), depends = arrayOf("org.junit.jupiter:junit-jupiter-api:5.10.1", "org.junit.jupiter:junit-jupiter-params:5.10.1"), publish = false)
}

val asm = project(":asm") {
    description = rootProject.description
    setCfg(arrayOf("org.objectweb.asm", "org.objectweb.asm.signature", "dev.oblivruin.asm"))
}

val asmTree = project(":asm-tree") {
    description = "Tree API of ${rootProject.description}"
    setCfg(asm, "org.objectweb.asm.tree")
}

val asmAnalysis = project(":asm-analysis") {
    description = "Static code analysis API of ${rootProject.description}"
    setCfg(asmTree, "org.objectweb.asm.tree.analysis")
}

val asmUtil = project(":asm-util") {
    description = "Utilities for ${rootProject.description}"
    setCfg(asmAnalysis, "org.objectweb.asm.util")

    val testImplementation by configurations
    dependencies { testImplementation("org.codehaus.janino:janino:3.1.9") }
}

val asmCommons = project(":asm-commons") {
    description = "Usefull class adapters based on ${rootProject.description}"
    setCfg(asmTree, "org.objectweb.asm.commons")

    val testImplementation by configurations
    dependencies { testImplementation(asmUtil) }
}

val asmx = project(":asmx") {
    description = "AsmX"
    group = "dev.oblivruin.asmx"
    setCfg(asmTree, "dev.oblivruin.asmx", "dev.oblivruin.asmx.cherry", "dev.oblivruin.asmx.tree")
}

// Use "gradle benchmarks:jmh [-PjmhInclude='<regex>']" to run the benchmarks.
val benchmarks = project(":benchmarks") {
    description = "Benchmarks for ${rootProject.description}"
    apply(plugin = "me.champeau.jmh")
    val implementation by configurations
    val jmh by configurations
    dependencies {
        implementation(files("libs/csg-bytecode-1.0.0.jar", "libs/jclasslib.jar"))
        jmh(asm)
        jmh(asmTree)
        implementation(asm)
        implementation(asmTree)
    }
    setCfg(emptyArray(), emptyArray(), arrayOf(
        "kawa:kawa:1.7",
        "net.sf.jiapi:jiapi-reflect:0.5.2",
        "net.sourceforge.serp:serp:1.15.1",
        "org.apache.bcel:bcel:6.6.0",
        "org.aspectj:aspectjweaver:1.8.10",
        "org.cojen:cojen:2.2.5",
        "org.javassist:javassist:3.21.0-GA",
        "org.mozilla:rhino:1.7.7.1"
    ), false)
    val version = version

    tasks.register<Copy>("asm$version") {
        from(asm.sourceSets["main"].output.classesDirs, asmTree.sourceSets["main"].output.classesDirs)
        into("${layout.buildDirectory.get().asFile}/asm$version")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    tasks.named("classes") { dependsOn("asm$version") }

    val j11ClsCfg = configurations.create("input-classes-java11")
    dependencies.add("input-classes-java11", "io.vavr:vavr:0.10.0@jar")

    tasks.register<Copy>("copyInputClasses") {
        from(j11ClsCfg.map { zipTree(it) })
        into("${layout.buildDirectory.get().asFile}/input-classes-java11")
    }
    tasks.named("classes") { dependsOn("copyInputClasses") }

    configure<JmhParameters> {
        jvmArgs.add("-Duser.dir=${rootDir}")
        resultFormat.set("CSV")
        profilers.set(listOf("org.objectweb.asm.benchmarks.MemoryProfiler"))
        if (rootProject.hasProperty("jmhInclude")) {
            includes.set(listOf(rootProject.property("jmhInclude") as String))
        }
    }
}

val buildTools = project(":build-tools") {
    description = "Build tools for Asm"
    setCfg(arrayOf("dev.oblivruin.asmx.buildtools"), arrayOf(asm), publish = false)
}

dependencies {
    constraints {
        api(asm)
        api(asmTree)
        api(asmAnalysis)
        api(asmUtil)
        api(asmCommons)
        api(asmx)
    }
}

// All projects are checked with googleJavaFormat, Checkstyle and PMD,
// and tested with :asm-test and JUnit.
subprojects {
    if (extra.has("cfg")) {
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

        val cfg: Cfg = getCfg()
        dependencies {
            for (project: Project in cfg.requires) {
                api(project)
            }
            for (str: String in cfg.depends) {
                api(str)
            }

            val testRuntimeOnly by configurations
            val testImplementation by configurations
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
            testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
            testImplementation(asmTest)
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
        if (cfg.packages.isNotEmpty()) {
            val moduleName = cfg.packages.first()

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
                            val sigTest = SignatureTest()

                            val args = listOf(
                                "-ApiVersion", version.toString(),
                                "-Backward",
                                "-Static",
                                "-Mode", "bin",
                                "-FileName", sigFile.absolutePath,
                                "-Classpath", rootProject.layout.projectDirectory.file("jdk8-api.jar").asFile.path +
                                        File.pathSeparator + sourceSets["main"].output.classesDirs.asPath,
                                "-Package"
                            ) + cfg.packages

                            sigTest.run(
                                args.toTypedArray(),
                                File(outputs.files.singleFile.absolutePath).printWriter(Charsets.UTF_8),
                                null
                            )

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
                        val setup = Setup()

                        val args = listOf(
                            "-ApiVersion", version.toString(),
                            "-FileName", outputs.files.singleFile.absolutePath,
                            "-Classpath", listOf<String?>(
                                rootProject.layout.projectDirectory.file("jdk8-api.jar").asFile.absolutePath,
                                sourceSets["main"].output.classesDirs.asPath,
                                configurations["compileClasspath"].asPath
                            ).joinToString(File.pathSeparator),
                            "-Package"
                        ) + cfg.packages

                        setup.run(args.toTypedArray(), PrintWriter(System.err, true), null)

                        if (!setup.isPassed) {
                            throw GradleException("Failed to generate sigtest")
                        }
                    }
                }
            }

            tasks.named<JavaCompile>("compileJava") {
                options.release = 8
            }

            tasks.register("genModuleInfo") {
                group = "Build"
                val moduleFile = layout.buildDirectory.file("generated/module-info/module-info.class").get().asFile
                outputs.doNotCacheIf("Disabled cache always") { true }
                outputs.upToDateWhen { false }

                outputs.file(moduleFile)
                doLast {
                    moduleFile.createNewFile()
                    ModuleBuilder().writeFile(moduleFile, cfg)
                }
            }

            // Manifest configure
            tasks.named<Jar>("jar") {
                dependsOn("genModuleInfo")
                from(tasks.named("genModuleInfo").get().outputs.files)

                manifest {
                    attributes(
                        "Implementation-Title" to project.description,
                        "Implementation-Version" to version
                    )
                    if (name != "asm-test") {
                        val imports = arrayListOf<String>()
                        for (req : Project in cfg.requires) {
                            imports.addAll(req.getCfg().packages)
                        }

                        attributes(
                            "Bundle-DocURL" to "https://github.com/OblivRuinDev/AsmX",
                            "Bundle-License" to "BSD-3-Clause;link=https://github.com/OblivRuinDev/AsmX/blob/dev/LICENSE",
                            "Bundle-ManifestVersion" to "2",
                            "Bundle-Name" to moduleName,
                            "Bundle-RequiredExecutionEnvironment" to "J2SE-1.5",
                            "Bundle-SymbolicName" to moduleName,
                            "Bundle-Version" to version,
                            "Export-Package" to cfg.packages.joinToString(",") {
                                "$it;version=\"$version\""
                            } + if (imports.isNotEmpty()) ";uses:=\"${imports.joinToString(",")}\"" else ""
                        )

                        if (imports.isNotEmpty()) {
                            attributes(
                                "Import-Package" to imports.joinToString(",") {
                                    "$it;version=\"$version\""
                                },
                                //"Module-Requires" to (extra["transitiveRequires"] as () -> Set<String>)()
                                //    .joinToString(",") { "$it;transitive=true" }
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
                group = "documentation"
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
                group = "documentation"
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

        //publish project
        if (cfg.publish) {
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
                            username = System.getenv("MVN_USER_NAME")
                            password = System.getenv("MVN_PASSWORD")
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
                                url.set("https://github.com/OblivRuinDev/AsmX/blob/dev/LICENSE")
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
                            url.set("https://github.com/oblivruindev/asmx/issues")
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
                    }
                }
            }

            signing {
                isRequired = rootProject.hasProperty("release")
                sign(publishing.publications["maven"])
            }

            tasks.withType<GenerateModuleMetadata> { enabled = false }
        }
    }
}

fun Project.setCfg(packages : Array<String>, requires : Array<Project> = emptyArray(), depends : Array<String> = emptyArray(), publish : Boolean = true) {
    extra["cfg"] = Cfg(packages, getRequire(requires), depends, publish, this)
}
fun Project.setCfg() {
    extra["cfg"] = Cfg(publish = false, project = this)
}
fun Project.setCfg(require: Project, vararg packages: String) {
    extra["cfg"] = Cfg(packages = packages as Array<String>, requires = getRequire(require), project = this)
}
fun Project.getCfg() : Cfg {
    return extra["cfg"] as Cfg
}

class Cfg(
    val packages: Array<String> = emptyArray(),
    /** The projects in this array cannot be duplicated. */
    val requires: Array<Project> = emptyArray(),
    val depends: Array<String> = emptyArray(),
    val publish: Boolean = true,
    val project: Project)

fun getRequire(projects : Array<Project>) : Array<Project> {
    if (projects.isEmpty()) {
        return projects
    } else {
        val set : MutableSet<Project> = mutableSetOf()
        for (project : Project in projects) {
            set.add(project)
            set.addAll(project.getCfg().requires)
        }
        return set.toTypedArray()
    }
}
fun getRequire(project : Project) : Array<Project> {
    return project.getCfg().requires + project
}

/** Hardcode module-info */
class ModuleBuilder: ByteArrayOutputStream(4096) {
    companion object {
        const val UTF8: Int = 1
        const val CLASS: Int = 7
        const val MODULE: Int = 19
        const val PACKAGE: Int = 20
    }

    private val output = DataOutputStream(this)
    fun writeFile(file: File, cfg: Cfg) {
        val output = FileOutputStream(file)
        readCfg(cfg)
        output.use {
            it.write(buf, 0, count)
        }
    }

    private fun utf8(string: String) {
        write(UTF8)
        output.writeUTF(string)
    }

    private fun readCfg(cfg: Cfg) {
        super.writeBytes(byteArrayOf(-54, -2, -70, -66, 0, 0, 0, 53)) //magic number and major & minor version
        output.writeShort((cfg.requires.size + cfg.packages.size) * 2 + 9) //constant pool size
        utf8("module-info");write(CLASS);output.writeShort(1) //#1  #2
        utf8(cfg.packages[0]);write(MODULE);output.writeShort(3) //#3  #4
        utf8(cfg.project.version.toString())//#5
        //#6  #7
        utf8("java.base");write(MODULE);output.writeShort(6)
        var pos = 8
        for (project: Project in cfg.requires) {//utf and module constant
            utf8(project.getCfg().packages[0]);write(MODULE);output.writeShort(pos)
            pos += 2
        }
        for (str: String in cfg.packages) {//utf and package constant
            utf8(str.replace('.', '/'));write(PACKAGE);output.writeShort(pos)
            pos += 2
        }
        utf8("Module")
        super.writeBytes(byteArrayOf(0x80.toByte(), 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01))
        output.writeShort(pos)
        //attribute size
        output.writeInt(22 + (cfg.requires.size + cfg.packages.size) * 6)
        super.writeBytes(byteArrayOf(0x00, 0x04, 0x00, 0x20, 0x00, 0x05))
        var int = cfg.requires.size
        output.writeShort(int + 1) // require size
        super.writeBytes(byteArrayOf(0x00, 0x07, 0x80.toByte(), 0x00, 0x00, 0x00)) // "java.base" ACC_MANDATED
        pos = 9
        repeat(int) {
            output.writeShort(pos)
            super.writeBytes(byteArrayOf(0x00, 0x20, 0x00, 0x00)) //ACC_TRANSITIVE
            pos += 2
        }
        int = cfg.packages.size
        output.writeShort(int) // exports size
        repeat(int) {
            output.writeShort(pos)
            super.writeBytes(byteArrayOf(0, 0, 0, 0))
            pos += 2
        }
        super.writeBytes(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
    }
}