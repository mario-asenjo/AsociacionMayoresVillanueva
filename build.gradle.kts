plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    // Repos centralizados en settings.gradle.kts (FAIL_ON_PROJECT_REPOS)
}

subprojects {
    // Ktlint para todo el repo (app y futuros módulos)
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    extensions.configure(org.jlleitschuh.gradle.ktlint.KtlintExtension::class.java) {
        android.set(true)
        verbose.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(false)
        filter {
            exclude("**/build/**")
            include("**/src/**/*.kt")
            include("**/src/**/*.kts")
        }
    }
}
