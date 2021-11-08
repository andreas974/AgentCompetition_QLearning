package edu.kit.exp.server.run;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class provides true random numbers. It acts as a HTTP client to use the
 * web services of <b>www.random.org</b>. The randomness of the numbers comes
 * from <i>atmospheric noise</i>, which for many purposes is better than the
 * pseudo-random number algorithms typically used in computer programs. (see www.random.org)
 * 
 */
public class RandomNumberGenerator {

	private static RandomNumberGenerator instance;

	/**
	 * Get instance of RandomNumberGenerator.
	 * 
	 * @return
	 */
	public static RandomNumberGenerator getInstance() {

		if (instance == null) {
			instance = new RandomNumberGenerator();
		}

		return instance;
	}

	/**
	 * Constructor
	 */
	private RandomNumberGenerator() {

	}

	
	/**
	 * This method returns repeating Integers. It will draw a random number x
	 * between min (x>=min) and max (x<=max) <b>with replacement</b>.
	 * 
	 * @param number
	 *            The number of random integers.
	 * @param min
	 *            The smallest value allowed for each integer.
	 * @param max
	 *            The largest value allowed for each integer.
	 * @param base
	 *            The base that will be used to print the numbers, i.e., binary,
	 *            octal, decimal or hexadecimal.
	 * @return ArrayList of random Integers
	 * @throws RandomGeneratorException
	 */
	public ArrayList<Integer> generateRepeatingIntegers(Integer number, Integer min, Integer max, Integer base) throws RandomGeneratorException {

		checkQuota();

		ArrayList<Integer> integerList = new ArrayList<Integer>();

		try {

			/*String url = "http://www.random.org/integers/";
			url += "?num=" + number + "&";
			url += "min=" + min + "&";
			url += "max=" + max + "&";
			url += "col=1&";
			url += "base=" + base + "&";
			url += "format=plain&";
			url += "rnd=new";

			System.out.println(url);

			//ArrayList<String> list = httpGet(url); */
			while (integerList.size() < (max-min + 1)) {
				Integer line = new Integer ((int) (min + (Math.random() * ((max-min) + 1))));
				//System.out.println(line);
				integerList.add(Integer.valueOf(line));
			}	
		} catch (Exception e) {
			throw new RandomGeneratorException(e.getMessage());
		}

		return integerList;
	}

	/**
	 * This method returns an interval of <b>non</b> repeating Integers. It will
	 * draw a random number x between min (x>=min) and max (x<=max) <b>without
	 * replacement</b>.
	 * 
	 * @param min
	 *            The lower bound of the interval (inclusive).
	 * @param max
	 *            The upper bound of the interval (inclusive).
	 * @return ArrayList with the integers of the given interval in random
	 *         order.
	 * @throws RandomGeneratorException
	 */
	public ArrayList<Integer> generateNonRepeatingIntegers(Integer min, Integer max) throws RandomGeneratorException {

		checkQuota();

		ArrayList<Integer> integerList = new ArrayList<Integer>();

		try {

			// http://www.random.org/sequences/?min=1&max=52&col=1&format=plain&rnd=new

		/*	String url = "http://www.random.org/sequences/?";
			url += "min=" + min + "&";
			url += "max=" + max + "&";
			url += "col=1&";
			url += "format=plain&";
			url += "rnd=new";

			System.out.println(url); */

			//ArrayList<String> list = httpGet(url);
			while (integerList.size() < (max-min + 1)) {
				
				Integer line = min + (int) (Math.random() * ((max - min) + 1));
				//System.out.println(line);
				if (!integerList.contains(line)) {
					integerList.add(Integer.valueOf(line));
				}
			}
		} catch (Exception e) {
			throw new RandomGeneratorException(e.getMessage());
		}

		return integerList;
	}

	/**
	 * Compliant to the guide lines of random.org for automated clients the
	 * quota must be checked before a query is send to the web services. Every
	 * client (IP-Address) has a daily free contingent of 1000000 random Bits.
	 * 
	 * @throws RandomGeneratorException
	 *             when daily quota is <= 0.
	 */
	private void checkQuota() throws RandomGeneratorException {

		//ArrayList<String> list = httpGet("http://www.random.org/quota/?format=plain");
		//Integer quota = Integer.valueOf(list.get(0));
		Random rand = new Random ();
		Integer quota = rand.nextInt(1000000);
		//System.out.println(quota);

		if (quota <= 0) {
			throw new RandomGeneratorException("Daily free quota reached!");
		}
	}
}
