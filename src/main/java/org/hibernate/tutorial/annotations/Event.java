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
package org.hibernate.tutorial.annotations;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity  //The @javax.persistence.Entity annotation is used to mark a class as an entity.
		 //It equivalents to the <class/> mapping element in the Hibernate mapping file Event.hbm.xml.
@Table( name = "EVENTS" )  //The @javax.persistence.Table annotation explicitly specifies the table name.
						   //Without this specification, the default table name would be EVENT.
public class Event {
    private Long id;

    private String title;
    private Date date;

	public Event() {
		// this form used by Hibernate
	}

	public Event(String title, Date date) {
		// for application use, to create new events
		this.title = title;
		this.date = date;
	}

	@Id  //Annotation @javax.persistence.Id marks the property which defines the entity’s identifier.
	//Annotation @javax.persistence.GeneratedValue and @org.hibernate.annotations.GenericGenerator work in tandem to
	//indicate that Hibernate should use Hibernate’s increment generation strategy for this entity’s identifier values.
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }

    //The date property needs special handling to account for its special naming and its SQL type.
	@Temporal(TemporalType.TIMESTAMP) //javax.persistence.TemporalType.
	@Column(name = "EVENT_DATE")
    public Date getDate() {
		return date;
    }

    public void setDate(Date date) {
		this.date = date;
    }

    //Attributes of an entity are considered persistent by default when mapping with annotations, which is why we don’t
	//see any mapping information associated with title.
    public String getTitle() {
		return title;
    }

    public void setTitle(String title) {
		this.title = title;
    }
}