package edu.kit.exp.impl.continuousCompetition.client.quiz;

import edu.kit.exp.client.gui.screens.quiz.QuizItemMultipleChoice;
import edu.kit.exp.client.gui.screens.quiz.QuizScreen;

/**
 * Created by dschnurr on 26.10.14.
 */
public class ContinuousCompetitionQuizDC2 extends QuizScreen {
    public ContinuousCompetitionQuizDC2(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
        super(gameId, parameter, screenId, showUpTime);

        this.setTitleText("Fragen zur Anleitung");
        this.setButtonText("Weiter");
        this.setPreText("Klicken Sie \"Weiter\", um mit den Fragen zu beginnen.");
        this.setPostText("Sie haben die Fragen erfolgreich abgeschlossen.<br>Klicken Sie \"Weiter\", um mit dem Experiment fortzufahren.");
        this.setWrongAnswerText("Die Antwort ist nicht korrekt. Überprüfen Sie Ihre Antwort nochmals mit Hilfe der Anleitung.");

        QuizItemMultipleChoice question;
        question = new QuizItemMultipleChoice("Wie viele Firmen stehen miteinander im Wettbewerb?");
        question.addAnswer("1");
        question.addAnswer("2", true);
        question.addAnswer("3");
        question.addAnswer("4");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Während der gesamten Zeit stehe ich mit der gleichen Firma im Wettbewerb.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die Konsumenten fragen nur bei der Firma nach, die die höhere Menge anbietet.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die Konsumenten fragen nur das Gut mit dem niedrigeren Preis nach.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Der Preis meines Gutes ist abhängig von den gewählten Mengen aller Firmen.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die in den vorhergehenden Perioden gewählten Mengen haben keine Auswirkung auf den Gewinn in dieser Periode.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die in den vorhergehenden Perioden gewählten Mengen haben keine Auswirkung auf den Kontostand in dieser Periode.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zum Preis Ihres Gutes sind richtig?<br><i>Hinweis: Es kann mehrere richtige Antworten geben.</i>");
        question.addAnswer("Wenn ich meine Menge erhöhe, sinkt der Preis meines Gutes.", true);
        question.addAnswer("Wenn ich meine Menge erhöhe, steigt der Preis meines Gutes.");
        question.addAnswer("Wenn die andere Firma ihre Menge erhöht, sinkt der Preis meines Gutes.", true);
        question.addAnswer("Wenn die andere Firma ihre Menge erhöht, steigt der Preis meines Gutes.");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zu Ihrem Gewinn sind richtig?");
        question.addAnswer("Wenn ich meine Menge erhöhe, sinkt mein Gewinn immer.");
        question.addAnswer("Wenn ich meine Menge erhöhe, steigt mein Gewinn immer.");
        question.addAnswer("Wenn ich meine Menge erhöhe und der positive Preis meines Gutes sich nicht verändert, steigt mein Gewinn immer.", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zu Ihrer Auszahlung am Ende des Experiments sind richtig?<br><i>Hinweis: Es kann mehrere richtige Antworten geben.</i>");
        question.addAnswer("Meine Auszahlung entspricht der Summe meiner Gewinne über alle Perioden.", true);
        question.addAnswer("Meine Auszahlung entspricht dem in der Testumgebung angezeigten Gewinn.");
        question.addAnswer("Meine Auszahlung entspricht dem letzten Kontostand.", true);
        question.addAnswer("Mein Kontostand erhöht sich jede Periode um den Gewinn der vorhergehenden Periode.", true);
        this.addQuizItem(question);
    }
}
