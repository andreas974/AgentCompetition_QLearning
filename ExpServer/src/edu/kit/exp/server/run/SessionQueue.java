package edu.kit.exp.server.run;

import java.util.ArrayList;

import edu.kit.exp.server.communication.ClientMessage;

/**
 * This is the thread save queue for incoming client messages, that are processed by the session thread.  
 *
 */
public class SessionQueue {

	private static SessionQueue instance;
	private ArrayList<ClientMessage> queue;	
	private RunStateLogger runStateLogger = RunStateLogger.getInstance();

	public static SessionQueue getInstance() {

		if (instance == null) {
			instance = new SessionQueue();
		}
		return instance;
	}	
	
	private SessionQueue() {
		queue = new ArrayList<ClientMessage>();
		
	}

	/**
	 * Remove an item from the queue (fifo)
	 * @return
	 */
	public synchronized ClientMessage pop() {
		
//		System.out.println(Thread.currentThread() + ": popping");
		
		while (isEmpty()) {
			
			try {
//				System.out.println(Thread.currentThread() + ": waiting to pop");
				wait();
			} catch (InterruptedException e) {
				runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
			}			
		}
		
		ClientMessage obj =  queue.get(0);		
		queue.remove(obj);
		
		
//		System.out.println(Thread.currentThread() + ": notifying after pop");
		notify();
		return obj;
	}

	/**
	 * Add an item to the queue.
	 * @param message 
	 */
	public synchronized void push(ClientMessage message) {
		
//		System.out.println(Thread.currentThread() + ": pushing");

		queue.add(message);
//		System.out.println(Thread.currentThread() + ": notifying after push");
		notify();
	}


	/**
	 * Checks if the queue is empty.
	 * @return
	 */
	public boolean isEmpty() {
		
		return queue.isEmpty();
		
		
	}

}
