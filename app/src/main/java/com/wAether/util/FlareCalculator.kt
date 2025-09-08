package com.wAether.util

/**
 * Utility to calculate X-ray flare classification from GOES 1-8 Angstrom flux values.
 * Flux values are in W/m^2.
 *
 * Classifications:
 * A-class: < 1.0e-8 W/m^2
 * B-class: 1.0e-8 to < 1.0e-7 W/m^2
 * C-class: 1.0e-7 to < 1.0e-6 W/m^2
 * M-class: 1.0e-6 to < 1.0e-5 W/m^2
 * X-class: >= 1.0e-5 W/m^2
 *
 * The number following the class letter is a multiplier within that class.
 * Example: C2.0 means 2.0 * 1.0e-7 W/m^2.
 * M5.5 means 5.5 * 1.0e-6 W/m^2.
 */
object FlareCalculator {

    private const val A_CLASS_THRESHOLD = 1.0e-8
    private const val B_CLASS_THRESHOLD = 1.0e-7
    private const val C_CLASS_THRESHOLD = 1.0e-6
    private const val M_CLASS_THRESHOLD = 1.0e-5
    // X_CLASS_THRESHOLD is effectively M_CLASS_THRESHOLD

    fun getFlareClass(flux: Double?): String? {
        if (flux == null || flux <= 0) return null // No flare or invalid flux

        return when {
            flux >= M_CLASS_THRESHOLD -> { // X-class or higher M-class
                val multiplier = (flux / M_CLASS_THRESHOLD)
                if (multiplier >= 10.0) { // X10 or higher becomes X + rounded number
                    String.format("X%.1f", multiplier).replace(".0", "") // X10, X11 etc.
                } else { // M-class or X-class (X1 to X9.9)
                    if (flux >= M_CLASS_THRESHOLD * 10) {
                        String.format("X%.1f", multiplier / 10.0).replace(".0", "") // X Class
                    } else {
                        String.format("M%.1f", multiplier).replace(".0", "") // M Class
                    }
                }
            }
            flux >= C_CLASS_THRESHOLD -> {
                val multiplier = (flux / C_CLASS_THRESHOLD)
                String.format("C%.1f", multiplier).replace(".0", "")
            }
            flux >= B_CLASS_THRESHOLD -> {
                val multiplier = (flux / B_CLASS_THRESHOLD)
                String.format("B%.1f", multiplier).replace(".0", "")
            }
            flux >= A_CLASS_THRESHOLD -> { // A-class (NOAA often reports A-class flux below B1.0 as just background or <A level)
                val multiplier = (flux / A_CLASS_THRESHOLD)
                // Typically, A-class flares are not numbered unless very significant.
                // For simplicity, we can just return "A" or "A-level".
                // Or, if you want to show a number: String.format("A%.1f", multiplier).replace(".0", "")
                // Let's be more specific for values close to B
                if (multiplier >= 1.0) String.format("A%.1f", multiplier).replace(".0", "") else "<A1.0"
            }
            else -> "<A1.0" // Below A-class threshold
        }
    }
}
