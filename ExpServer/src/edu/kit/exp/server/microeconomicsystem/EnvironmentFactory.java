package edu.kit.exp.server.microeconomicsystem;

/**
 * Environment Factory Class -  This Class is used to create environment objects of different environment subclasses.
 *
 */
public class EnvironmentFactory {

	private EnvironmentFactory() {

	}

	/**
	 * Factory Method to create an environment.
	 * 
	 * @param environmentFactoryKey Class to be initiated
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Environment> T createEnvironment(String environmentFactoryKey) {
        try
        {
            return (T)(Class.forName(environmentFactoryKey).getConstructor().newInstance());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
	}
}
