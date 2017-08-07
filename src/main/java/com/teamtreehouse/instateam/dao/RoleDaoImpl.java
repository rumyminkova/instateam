package com.teamtreehouse.instateam.dao;

import com.teamtreehouse.instateam.model.Role;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by Rumy on 7/17/2017.
 */
@Repository
public class RoleDaoImpl implements RoleDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Role> findAll() {
        Session session=sessionFactory.openSession();
        CriteriaBuilder builder=session.getCriteriaBuilder();
        CriteriaQuery<Role> criteria= builder.createQuery(Role.class);
        criteria.from(Role.class);
        List<Role> roles=session.createQuery(criteria).getResultList();
        roles.forEach(role -> Hibernate.initialize(role.getCollaboratorsForRole()));
        session.close();
        return roles;
    }

    @Override
    public Role findById(Long id) {
        Session session = sessionFactory.openSession();
        Role role= session.get(Role.class,id);
        Hibernate.initialize(role.getCollaboratorsForRole());
        session.close();
        return role;
    }

    @Override
    public void save(Role role) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Save the category
        session.saveOrUpdate(role);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
   }

    @Override
    public void delete(Role role) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(role);
        session.getTransaction().commit();
        session.close();
   }
}
