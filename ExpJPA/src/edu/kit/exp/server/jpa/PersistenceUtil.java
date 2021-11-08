package edu.kit.exp.server.jpa;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.kit.exp.common.Constants;

/**
 * This Class handles the entity manager factory
 * 
 */
public class PersistenceUtil {

	private static EntityManagerFactory entitymanagerFactory = null;
	private static String databaseIP = "localhost";

	public static String getDatabaseIP() {
		return databaseIP;
	}

	public static void setDatabaseIP(String databaseIP) {
		if (entitymanagerFactory == null) {
			PersistenceUtil.databaseIP = databaseIP;
		} else {
			try {
				throw new Exception("Changing database IP not possible. Database already connected!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static EntityManager createEntityManager() {
		return getEntitymanagerFactory().createEntityManager();
	}

	public static EntityManagerFactory getEntitymanagerFactory() {
		if (entitymanagerFactory == null) {
			Properties persistenceProperties = Constants.loadProperties(new String[] { "META-INF/persistence.properties", "META-INF/database.properties" });

			final String dbUrlProperty = "javax.persistence.jdbc.url";
			persistenceProperties.put(dbUrlProperty, ((String) persistenceProperties.get(dbUrlProperty)).replaceAll("\\{SERVERIP\\}", databaseIP));

			entitymanagerFactory = Persistence.createEntityManagerFactory("ExpPU", persistenceProperties);
		}
		return entitymanagerFactory;
	}
}
