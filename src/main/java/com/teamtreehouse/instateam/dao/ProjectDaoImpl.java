package com.teamtreehouse.instateam.dao;

import com.teamtreehouse.instateam.model.Project;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by Rumy on 7/24/2017.
 */

@Repository
public class ProjectDaoImpl implements ProjectDao {
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public List<Project> findAll() {
        Session session=sessionFactory.openSession();
        CriteriaBuilder builder=session.getCriteriaBuilder();
        CriteriaQuery<Project> criteria= builder.createQuery(Project.class);
        criteria.from(Project.class);
        List<Project> projects=session.createQuery(criteria).getResultList();
        projects.forEach(project -> Hibernate.initialize(project.getRolesNeeded()));
        projects.forEach(project -> Hibernate.initialize(project.getCollaborators()));
        session.close();
        return projects;
    }

    @Override
    public Project findById(Long id) {
        Session session = sessionFactory.openSession();
        Project project= session.get(Project.class,id);
        Hibernate.initialize(project.getRolesNeeded());
        project.getRolesNeeded().forEach(role -> Hibernate.initialize(role.getCollaboratorsForRole()));
        Hibernate.initialize(project.getCollaborators());
        session.close();
        return project;
    }

    @Override
    public void save(Project project) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Save the
        session.saveOrUpdate(project);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

    }

    @Override
    public void delete(Project project) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(project);
        session.getTransaction().commit();
        session.close();
      }
}
