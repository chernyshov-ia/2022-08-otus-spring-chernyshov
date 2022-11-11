package ru.otus.testing.domain;

public class TestResult {
    private final int rightAnswers;
    private final int totalQuestions;

    public TestResult(int rightAnswers, int totalQuestions) {
        this.rightAnswers = rightAnswers;
        this.totalQuestions = totalQuestions;
    }

    public int getRightAnswers() {
        return rightAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getRightAnswersRationPercents() {
        return  (int) Math.round(100 * rightAnswers / (totalQuestions * 1.00));
    }

    public boolean isPassed(int passThresholdPercents) {
       return getRightAnswersRationPercents() >= passThresholdPercents;
    }
}
