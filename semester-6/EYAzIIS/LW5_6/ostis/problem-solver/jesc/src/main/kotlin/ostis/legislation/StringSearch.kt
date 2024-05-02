package ostis.legislation

object StringSearch {

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length

        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) {
            for (j in 0..n) {
                when {
                    i == 0 -> dp[i][j] = j
                    j == 0 -> dp[i][j] = i
                    else -> {
                        val cost = if (s1[i - 1] == s2[j - 1]) 0 else 2
                        dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
                    }
                }
            }
        }

        return dp[m][n]
    }

    fun findMostSimilarString(target: String, strings: List<String>): String? {
        var minDistance = Int.MAX_VALUE
        var mostSimilarString: String? = null

        for (string in strings) {
            val distance = levenshteinDistance(target, string)
            if (distance < minDistance) {
                minDistance = distance
                mostSimilarString = string
            }
        }

        return mostSimilarString
    }

}
