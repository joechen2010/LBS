package cn.edu.nju.software.gof.entity;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static final EntityManagerFactory instance = Persistence
			.createEntityManagerFactory("mutong");
	
	private EMF() {
	}

	public static EntityManagerFactory getInstance() {
		return instance;
	}
}
