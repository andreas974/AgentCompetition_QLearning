package edu.kit.exp.common.communication;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class IncommingMessageQueue<T> {
	private ArrayList<T> queue;
	
	IncommingMessageQueue() {
		queue = new ArrayList<T>();
	}

	/**
	 * Remove an item from the queue (fifo)
	 * 
	 * @return
	 */
	public synchronized T pop() {

//		System.out.println(Thread.currentThread() + ": popping");
		while (isEmpty()) {

			try {
//				System.out.println(Thread.currentThread() + ": waiting to pop");
				wait();
			} catch (InterruptedException e) {
				if (Frame.getFrames().length > 0){
					JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					e.printStackTrace();
				}
			}
		}

		T obj = queue.get(0);
		queue.remove(obj);

//		System.out.println(Thread.currentThread() + ": notifying after pop");
		notify();
		return obj;
	}

	/**
	 * Add an item to the queue.
	 * 
	 * @param message
	 */
	public synchronized void push(T message) {

//		System.out.println(Thread.currentThread() + ": pushing");

		queue.add(message);
//		System.out.println(Thread.currentThread() + ": notifying after push");
		notify();
	}

	/**
	 * Checks if the queue is empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {

		return queue.isEmpty();

	}
}
