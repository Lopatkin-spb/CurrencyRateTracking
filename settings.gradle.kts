pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "CurrencyRateTracking"
/**
 * Modules
 */
include(":app")
include(":shared:common-android")
include(":shared:core")
include(":shared:api-remote")
include(":shared:model")
include(":shared:common")
include(":shared:api-locale")
include(":feature:currencies")
include(":shared:ui-theme")
include(":feature:favorites")
