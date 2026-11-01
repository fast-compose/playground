import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    val projectName = libs.versions.project.name.get()
    val packageName = libs.versions.project.packageName.get()
    val versionName = libs.versions.project.versionName.get()
    application {
        mainClass = "$packageName.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            this.packageName = projectName
            packageVersion = versionName
            description = libs.versions.project.description.get()
            vendor = libs.versions.project.vendor.get()
            windows {
                iconFile.set(project.file("icons/icon.ico"))
                shortcut = true
                menuGroup = projectName
                // the same on every release, so a new installer replaces the old version
                upgradeUuid = "b6bea2fe-340c-4181-8ff9-496c6fb26f10"
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
                shortcut = true
                this.packageName = projectName.lowercase().replace(' ', '-')
                appCategory = "Development"
            }
            macOS {
                iconFile.set(project.file("icons/icon.icns"))
                bundleID = packageName
                // macOS packages need a major version above 0, so 0.x.y is packaged as 1.x.y
                if (versionName.startsWith("0.")) packageVersion = "1." + versionName.removePrefix("0.")
            }
        }
    }
}
