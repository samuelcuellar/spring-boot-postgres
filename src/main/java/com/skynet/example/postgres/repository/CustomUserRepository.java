package com.skynet.example.postgres.repository;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.skynet.example.postgres.domain.User;

@Repository
public class CustomUserRepository extends AbstractRepository<User, Long> {
	
	public void saveUser(String username, String password) {
		Query query = getEntityManager()
				.createNativeQuery("insert into usr(id, username, password) values(nextval('entity_id_seq'), :username, :password)");
		query.setParameter("username", username);
		query.setParameter("password", password);
		query.executeUpdate();
	}

	@Override
	protected Class<User> getPersistentClass() {
		return User.class;
	}

}
