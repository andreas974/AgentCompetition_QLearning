package edu.kit.exp.common.communication;

import java.awt.Frame;

import javax.swing.JOptionPane;

import edu.kit.exp.common.communication.IncommingMessageQueue;
import edu.kit.exp.common.communication.MessageReceiver;

public class MessageDeliveryThread<T> extends Thread {
	private IncommingMessageQueue<T> messageQueue;
	private MessageReceiver<T> messageReceiver;

	public MessageDeliveryThread(String name, IncommingMessageQueue<T> queue, MessageReceiver<T> receiver) {
		super(name);
		this.messageQueue = queue;
		this.messageReceiver = receiver;
//		System.out.println(this);
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		T msg = null;

		while (true) {
			msg = messageQueue.pop();
//			System.out.println(msg.toString());
			try {
				messageReceiver.routeMessage(msg);
			} catch (Exception e) {
				if (Frame.getFrames().length > 0){
					JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					e.printStackTrace();
				}
			}

		}
	}

}
