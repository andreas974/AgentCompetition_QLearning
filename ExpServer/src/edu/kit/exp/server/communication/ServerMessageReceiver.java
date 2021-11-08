package edu.kit.exp.server.communication;

import javax.naming.CommunicationException;

import edu.kit.exp.common.communication.MessageReceiver;
import edu.kit.exp.server.run.RunStateLogger;
import edu.kit.exp.server.run.SessionQueue;
import edu.kit.exp.server.run.SubjectRegistration;

/**
 * This class get all client messages and routes them to their receiver.
 * 
 */
public class ServerMessageReceiver extends MessageReceiver<ClientMessage> {

	private ServerMessageSender messageSender;
	private SubjectRegistration subjectRegistration;
	private SessionQueue sessionQueue;

	ServerMessageReceiver(){
		messageSender = ServerCommunicationManager.getInstance().getMessageSender();
		subjectRegistration = SubjectRegistration.getInstance();
		sessionQueue = SessionQueue.getInstance();
	}
	
	/**
	 * Routes a client message to its destination. If <code>msg</code> is a
	 * ClientRegistrationMessage the subjectRegistration is called. If
	 * <code>msg</code> is is a ClientResponseMessage it is routed to the
	 * addressed message queue.
	 * 
	 * @param msg A client message
	 * @throws Exception 
	 */
	@Override
	public void routeMessage(ClientMessage msg) throws Exception {

		if (msg.getClass().equals(ClientRegistrationMessage.class)) {

			ClientRegistrationMessage clientRegistrationMessage = (ClientRegistrationMessage) msg;

			String clientId = clientRegistrationMessage.getClientId();

			try {
				subjectRegistration.registerSubject(clientRegistrationMessage);				
				
			} catch (CommunicationException e) {
				RunStateLogger.getInstance().createOutputMessage("Client Login error."+clientId);
				messageSender.sendLoginError(clientRegistrationMessage.getClientRemoteObject());
				
			}

		}
		
		else if (msg.getClass().equals(ClientPhysioDataMessage.class)) {
			ClientState.getClient(msg.getClientId()).getPhysioState().setLatestPhysioMessage((ClientPhysioDataMessage) msg);
		}

		else if (msg.getClass().equals(ClientResponseMessage.class)
					|| msg.getClass().equals(ClientTrialLogMessage.class)
					|| msg.getClass().equals(QuizProtocolMessage.class)) {
			
			sessionQueue.push(msg);
		}
	}

}
