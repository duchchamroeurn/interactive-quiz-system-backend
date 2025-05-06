package com.chamroeurn.iqs.utils

import org.springframework.stereotype.Component

@Component
class GradingUtils {

    /**
     * Calculates the percentage score from earned points and total possible points.
     *
     * @param earnedPoints The number of points a student earned.
     * @param totalPossiblePoints The total number of points possible.
     * @return The percentage score, or null if totalPossiblePoints is zero.
     */
    fun calculatePercentage(earnedPoints: Double, totalPossiblePoints: Double): Double? {
        if (totalPossiblePoints == 0.0) {
            return null // Avoid division by zero.  Consider logging this as an error.
        }
        return (earnedPoints / totalPossiblePoints) * 100.0
    }

    /**
     * Determines the letter grade based on the percentage score, using a configurable grading scale.
     *
     * @param percentage The percentage score.
     * @param gradingScale A map defining the percentage ranges for each letter grade.
     * The keys are the minimum percentage for the grade, and the values are the letter grades.
     * The map should be ordered in descending order of percentages (e.g., 90 to "A", 80 to "B").
     * @return The letter grade, or null if the percentage is not within any defined range.
     */
    fun getLetterGrade(percentage: Double?, gradingScale: Map<Double, String>): String? {
        if (percentage == null) {
            return null // Handle null percentage
        }

        for ((minPercentage, grade) in gradingScale) {
            if (percentage >= minPercentage) {
                return grade
            }
        }
        return null // Percentage didn't fall into any range.  Consider logging this.
    }

    /**
     * Determines if the student passed or failed based on the percentage score and a passing threshold.
     *
     * @param percentage The percentage score.
     * @param passingThreshold The minimum percentage required to pass.
     * @return "Pass" if the percentage is greater than or equal to the passingThreshold, "Fail" otherwise.
     * Returns null if the percentage is null.
     */
    fun getPassOrFail(percentage: Double?, passingThreshold: Double): String? {
        if (percentage == null) {
            return null
        }
        return if (percentage >= passingThreshold) "Pass" else "Fail"
    }

    /**
     * Evaluates the quiz result by calculating the percentage, letter grade, and pass/fail status.
     *
     * @param earnedPoints Points earned by the student.
     * @param totalPossiblePoints Total points possible for the quiz.
     * @param gradingScale Map defining percentage to letter grade mapping.
     * @param passingThreshold Minimum percentage to pass.
     * @return A map containing the 'percentage', 'letterGrade', and 'passOrFail' results.
     * Returns null if  earnedPoints or totalPossiblePoints is null.
     */
    fun evaluateQuiz(
        earnedPoints: Double?,
        totalPossiblePoints: Double?,
        gradingScale: Map<Double, String>,
        passingThreshold: Double
    ): Map<String, Any?>? {

        if (earnedPoints == null || totalPossiblePoints == null) {
            return null
        }
        //added null pointer checks
        val percentage = calculatePercentage(earnedPoints, totalPossiblePoints)
        val letterGrade = getLetterGrade(percentage, gradingScale)
        val passOrFail = getPassOrFail(percentage, passingThreshold)

        return mapOf(
            "percentage" to percentage,
            "letterGrade" to letterGrade,
            "passOrFail" to passOrFail
        )
    }
}