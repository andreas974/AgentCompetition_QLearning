package edu.kit.exp.server.microeconomicsystem;

import java.util.List;

import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;

/**
 * Institution Factory Class - This Class is used to create institution objects
 * of different institution subclasses.
 * 
 */
public class InstitutionFactory {

	private InstitutionFactory() {

	}

	/**
	 * Factory Method to create an institution.
	 * 
	 * @param institutionFactoryKey
	 * @param messageSender
	 * @param queueId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Institution<? extends Environment>> T createInstitution(String institutionFactoryKey, Environment environment, List<Membership> memberships, ServerMessageSender messageSender, String queueId) {
        try
        {
        	//return (T)(Class.forName(institutionFactoryKey).getConstructor(Environment.class, List.class, ServerMessageSender.class, String.class).newInstance(environment, memberships, messageSender, queueId));
        	//return (T)(Class.forName(institutionFactoryKey).getConstructor(??GenericParameter??, List.class, ServerMessageSender.class, String.class).newInstance(environment, memberships, messageSender, queueId));
        	return (T)(Class.forName(institutionFactoryKey).getConstructors()[0].newInstance(environment, memberships, messageSender, queueId));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
	}
}
