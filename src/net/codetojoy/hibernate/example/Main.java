package net.codetojoy.hibernate.example;

import org.hibernate.*;
import org.hibernate.cfg.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    
    private static SessionFactory sessionFactory;
    
    static {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    
    static SessionFactory getSessionFactory() { return sessionFactory; }
    static void shutdown() { getSessionFactory().close(); }
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("starting....");
        
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        List<Owner> owners = (List<Owner>) session.createQuery("from Owner o").list();
        
        for( Owner owner : owners ) {
            System.out.println("id   = " + owner.getId());
            System.out.println("name = " + owner.getName());            
        }
        
        tx.commit();
        session.close();
        
        shutdown();
        
        System.out.println("done. " + (new Date()).toString() );        
    }
}