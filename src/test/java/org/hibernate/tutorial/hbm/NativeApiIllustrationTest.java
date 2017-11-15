/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.tutorial.hbm;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import junit.framework.TestCase;

/**
 * Illustrates use of Hibernate native APIs.
 *
 * @author Steve Ebersole
 */
public class NativeApiIllustrationTest extends TestCase {
	private SessionFactory sessionFactory;

	@Override
	protected void setUp() throws Exception {
		System.out.println("setUp()");
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // Configures settings from resources/hibernate.cfg.xml by default.
				             // You can specify the config file name as the parameter if you don't want to use the default name.
				             // At runtime, 'hibernate.properties not found' will prompt, but StandardServiceRegistryBuilder
							 // still looks for hibernate.cfg.xml if hibernate.properties is not found
				.build();
		System.out.println("registry=" + registry);
		try {
			// Using the StandardServiceRegistry we create the org.hibernate.boot.MetadataSources which is the start point
			// for telling Hibernate about your domain model.
			// The buildMetadata() returns an org.hibernate.boot.Metadata object, which represents the complete, partially
			// validated view of the application domain model which the SessionFactory will be based on. This method loads
			// the Hibernate mapping file (Event.hbm.xml in this example), which is defined in hibernate.cfg.xml.
			// The SessionFactory is a thread-safe object that is instantiated once to serve the entire application.
			// The SessionFactory acts as a factory for org.hibernate.Session instances, which should be thought of as a
			// corollary to a "unit of work".
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
			System.out.println("setUp() sessionFactory =" + sessionFactory);
		}
		catch (Exception e) {
			//add this to print the err stack trace in case any exception creating sessionFactory. e.g. config file path issue
			e.printStackTrace();
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
		}
	}

	@Override
	protected void tearDown() throws Exception {
		if ( sessionFactory != null ) {
			sessionFactory.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void testBasicUsage() {
		System.out.println("testBasicUsage() sessionFactory=" + sessionFactory);
		/* create a couple of events... */
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save( new Event( "Our very first event!", new Date() ) );
		session.save( new Event( "A follow up event", new Date() ) );
		session.getTransaction().commit();
		session.close();

		/* now lets pull events from the database and list them  */
		session = sessionFactory.openSession();
		session.beginTransaction();
		//Here we see an example of the Hibernate Query Language (HQL) to load all existing Event objects from the
		//database by generating the appropriate SELECT SQL, sending it to the database and populating Event objects
		//with the result set data.
		List result = session.createQuery( "from Event" ).list();
		for ( Event event : (List<Event>) result ) {
			System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
		}
		session.getTransaction().commit();
		session.close();
	}
}
