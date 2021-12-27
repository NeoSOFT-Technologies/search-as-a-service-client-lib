package com.solr.clientwrapper.infrastructure.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SampleEntity.class)
public abstract class SampleEntity_ {

	public static volatile SingularAttribute<SampleEntity, String> password;
	public static volatile SingularAttribute<SampleEntity, Integer> phone;
	public static volatile SingularAttribute<SampleEntity, String> name;
	public static volatile SingularAttribute<SampleEntity, Long> id;
	public static volatile SingularAttribute<SampleEntity, Integer> age;

	public static final String PASSWORD = "password";
	public static final String PHONE = "phone";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String AGE = "age";

}

