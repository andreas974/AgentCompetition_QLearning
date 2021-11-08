package edu.kit.exp.impl.continuousCompetition.client.quiz;

import edu.kit.exp.client.gui.screens.quiz.QuizItemMultipleChoice;
import edu.kit.exp.client.gui.screens.quiz.QuizScreen;

/**
 * Created by dschnurr on 22.10.14.
 */
public class ContinuousCompetitionQuizRC3 extends QuizScreen {

    public ContinuousCompetitionQuizRC3(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
        super(gameId, parameter, screenId, showUpTime);

        this.setTitleText("Fragen zur Anleitung");
        this.setButtonText("Weiter");
        this.setPreText("Klicken Sie \"Weiter\", um mit den Fragen zu beginnen.");
        this.setPostText("Sie haben die Fragen erfolgreich abgeschlossen.<br>Klicken Sie \"Weiter\", um mit dem Experiment fortzufahren.");
        this.setWrongAnswerText("Die Antwort ist nicht korrekt. Überprüfen Sie Ihre Antwort nochmals mit Hilfe der Anleitung.");

        QuizItemMultipleChoice question;
        question = new QuizItemMultipleChoice("Wie viele Firmen stehen miteinander im Wettbewerb?");
        question.addAnswer("1");
        question.addAnswer("2");
        question.addAnswer("3", true);
        question.addAnswer("4");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Während der gesamten Zeit stehe ich mit den gleichen Firmen im Wettbewerb.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die Konsumenten fragen nur bei der Firma nach, die die höchste Menge anbietet.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die Konsumenten fragen nur das Gut mit dem niedrigsten Preis nach.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Der Preis meines Gutes ist abhängig von den gewählten Mengen aller Firmen.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die in der Vergangenheit gewählten Mengen haben keine Auswirkung auf den momentanen Gewinn.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die in den Vergangenheit gewählten Mengen haben keine Auswirkung auf den Kontostand.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zum Preis Ihres Gutes sind richtig?<br><i>Hinweis: Es kann mehrere richtige Antworten geben.</i>");
        question.addAnswer("Wenn ich meine Menge erhöhe, sinkt der Preis meines Gutes.", true);
        question.addAnswer("Wenn ich meine Menge erhöhe, steigt der Preis meines Gutes.");
        question.addAnswer("Wenn eine der anderen Firmen ihre Menge erhöht, sinkt der Preis meines Gutes.", true);
        question.addAnswer("Wenn eine der anderen Firmen ihre Menge erhöht, steigt der Preis meines Gutes.");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zu Ihrem Gewinn sind richtig?");
        question.addAnswer("Wenn ich meine Menge erhöhe, sinkt mein Gewinn immer.");
        question.addAnswer("Wenn ich meine Menge erhöhe, steigt mein Gewinn immer.");
        question.addAnswer("Wenn ich meine Menge erhöhe und der positive Preis meines Gutes sich nicht verändert, steigt mein Gewinn immer.", true);
        question.addAnswer("<html><body>Der angezeigte momentane Gewinn entspricht dem Gewinn, den ich erhalten würde, wenn die momentane<br>Mengenkombination über eine Dauer von 30 Sekunden gehalten würde.</html></body>", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zu Ihrer Auszahlung am Ende des Experiments sind richtig?<br><i>Hinweis: Es kann mehrere richtige Antworten geben.</i>");
        question.addAnswer("Meine Auszahlung entspricht dem letzten Kontostand.", true);
        question.addAnswer("Meine Auszahlung entspricht dem angezeigten momentanen Gewinn.");
        question.addAnswer("Mein Kontostand erhöht sich kontinuierlich anteilig um den momentanen Gewinn.", true);

        this.addQuizItem(question);
    }
}
