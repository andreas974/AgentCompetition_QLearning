package edu.kit.exp.server.communication;

public class QuizProtocolMessage extends ClientMessage{

	private Boolean passed;
	private String quizSolution;

	protected QuizProtocolMessage(String clientId, Boolean passed, String quizSolution) {
		super(clientId);
		
		this.passed = passed;
		this.quizSolution = quizSolution;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public String getQuizSolution() {
		return quizSolution;
	}

	public void setQuizSolution(String quizSolution) {
		this.quizSolution = quizSolution;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((passed == null) ? 0 : passed.hashCode());
		result = prime * result + ((quizSolution == null) ? 0 : quizSolution.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuizProtocolMessage other = (QuizProtocolMessage) obj;
		if (passed == null) {
			if (other.passed != null)
				return false;
		} else if (!passed.equals(other.passed))
			return false;
		if (quizSolution == null) {
			if (other.quizSolution != null)
				return false;
		} else if (!quizSolution.equals(other.quizSolution))
			return false;
		return true;
	}
	
	

}
