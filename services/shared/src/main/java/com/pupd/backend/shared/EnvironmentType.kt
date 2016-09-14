package com.pupd.backend.shared

/**
 * Enumeration of possible environment types
 *
 * Created by maxiaojun on 9/6/16.
 */
enum class EnvironmentType {
    Default,
    Development,
    Production;

    companion object {
        /**
         * A more relaxed string to EnvironmentType method than valueOf.
         * Should be used over valueOf.
         */
        fun fromString(value: String): EnvironmentType {
            return when (value.toLowerCase()) {
                "dev", "development" -> EnvironmentType.Development
                "prod", "production" -> EnvironmentType.Production
                else -> EnvironmentType.Default
            }
        }
    }
}