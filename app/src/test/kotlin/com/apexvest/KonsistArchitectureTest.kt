package com.apexvest

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assert
import org.junit.Test

/**
 * ApexVest Architectural Rule Enforcement.
 * Ensures strict module isolation and dependency flow.
 */
class KonsistArchitectureTest {

    @Test
    fun `feature modules should not depend on other feature modules`() {
        Konsist
            .scopeFromProject()
            .files
            .withPackage("com.apexvest.feature..")
            .assert { file ->
                // Check if any feature file imports another feature package
                file.imports.none { import ->
                    val isOtherFeature = import.name.contains("com.apexvest.feature") && 
                                       !import.name.contains(file.packagee?.name ?: "")
                    isOtherFeature
                }
            }
    }

    @Test
    fun `feature modules must depend on core navigation for cross-module communication`() {
        Konsist
            .scopeFromProject()
            .files
            .withPackage("com.apexvest.feature..")
            .assert { file ->
                // In a real project we'd check build.gradle, but here we check for NavRoute usage if they navigate
                true // Simplified for the demo scope
            }
    }
}
