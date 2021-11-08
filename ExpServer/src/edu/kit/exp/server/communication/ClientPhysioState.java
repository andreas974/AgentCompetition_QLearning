package edu.kit.exp.server.communication;

public class ClientPhysioState {
		private ClientPhysioDataMessage latestPhysioMessage;

		public ClientPhysioDataMessage getLatestPhysioMessage() {
			return latestPhysioMessage;
		}

		public void setLatestPhysioMessage(ClientPhysioDataMessage latestPhysioMessage) {
			this.latestPhysioMessage = latestPhysioMessage;
		}
}
