package edu.kit.exp.impl.continuousCompetition.client.quiz;

import edu.kit.exp.client.gui.screens.quiz.QuizItemMultipleChoice;
import edu.kit.exp.client.gui.screens.quiz.QuizScreen;

/**
 * Created by dschnurr on 22.10.14.
 */
public class ContinuousCompetitionQuizRB2 extends QuizScreen {

    public ContinuousCompetitionQuizRB2(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
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

        question = new QuizItemMultipleChoice("Während der gesamten Zeit stehe ich mit den gleichen Firmen im Wettbewerb.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die Konsumenten fragen nur das Gut mit dem niedrigeren Preis nach.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die Menge meines Gutes ist abhängig von den Preisen aller Firmen.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die in der Vergangenheit gewählten Preise haben keine Auswirkung auf den momentanen Gewinn.");
        question.addAnswer("Wahr", true);
        question.addAnswer("Falsch");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Die in den Vergangenheit gewählten Preise haben keine Auswirkung auf den Kontostand.");
        question.addAnswer("Wahr");
        question.addAnswer("Falsch", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zur nachgefragten Menge Ihres Gutes sind richtig?<br><i>Hinweis: Es kann mehrere richtige Antworten geben.</i>");
        question.addAnswer("Wenn ich meinen Preis erhöhe, sinkt die nachgefragte Menge meines Gutes.", true);
        question.addAnswer("Wenn ich meinen Preis erhöhe, steigt die nachgefragte Menge meines Gutes.");
        question.addAnswer("Wenn die andere Firma ihren Preis erhöht, sinkt die nachgefragte Menge meines Gutes.");
        question.addAnswer("Wenn die andere Firma ihren Preis erhöht, steigt die nachgefragte Menge meines Gutes.", true);
        question.addAnswer("Wenn ich einen höheren Preis als die andere Firma anbiete, ist die nachgefragte Menge meines Gutes stets null.");
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zu Ihrem Gewinn sind richtig?");
        question.addAnswer("Wenn ich meinen Preis erhöhe, sinkt mein Gewinn immer.");
        question.addAnswer("Wenn ich meinen Preis erhöhe, steigt mein Gewinn immer.");
        question.addAnswer("Wenn ich meinen Preis erhöhe und die positive Menge meines Gutes sich nicht verändert, steigt mein Gewinn immer.", true);
        question.addAnswer("<html><body>Der angezeigte momentane Gewinn entspricht dem Gewinn, den ich erhalten würde, wenn die momentane<br>Preiskombination über eine Dauer von 30 Sekunden gehalten würde.</html></body>", true);
        this.addQuizItem(question);

        question = new QuizItemMultipleChoice("Welche der folgenden Aussagen zu Ihrer Auszahlung am Ende des Experiments sind richtig?<br><i>Hinweis: Es kann mehrere richtige Antworten geben.</i>");
        question.addAnswer("Meine Auszahlung entspricht dem letzten Kontostand.", true);
        question.addAnswer("Meine Auszahlung entspricht dem angezeigten momentanen Gewinn.");
        question.addAnswer("Mein Kontostand erhöht sich kontinuierlich anteilig um den momentanen Gewinn.", true);
        this.addQuizItem(question);
    }
}
