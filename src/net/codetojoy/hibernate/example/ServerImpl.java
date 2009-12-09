
package net.codetojoy.hibernate.example;

import org.hibernate.*;
import org.hibernate.cfg.*;
import java.util.*;
import java.util.concurrent.*;

class MyTask implements Runnable {
    
    private int taskId;
    private CyclicBarrier barrier;
    private SessionFactory sessionFactory;
    
    public MyTask(int taskId, CyclicBarrier barrier, SessionFactory sessionFactory) {
        this.barrier = barrier;
        this.taskId = taskId;
        this.sessionFactory = sessionFactory;
    }
    
    private synchronized void emit(String msg) {
        System.out.println("task " + taskId + " : " + msg);
    }
    
    public void run() {
        try {
            emit("waiting to start");
            
            // begin Txn
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
                        
            // create new persistent object            
            Owner owner = new Owner();
            owner.setName("CodeToJoy");
            session.saveOrUpdate(owner);
            long id = owner.getId();
            
            // wait for others
            emit("waiting...");
            barrier.await();
            
            // unleash the hounds!
            
            tx.commit();
            session.close();
            
            emit("done for pk = " + id);
        } catch(Exception ex) {
            emit("caught : " + ex.toString() );
        }
    }
}

public class ServerImpl {
    
    private static SessionFactory sessionFactory;
    
    static {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    
    static SessionFactory getSessionFactory() { return sessionFactory; }
    static void shutdown() { getSessionFactory().close(); }
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("starting....");
        
        long n = 1000L;
        int poolSize = 5;
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize, 50000L, TimeUnit.MILLISECONDS,
                                                        new LinkedBlockingQueue<Runnable>());

        final int NUM_TASKS = 5;
        
        CyclicBarrier barrier = new CyclicBarrier(NUM_TASKS, 
                                                    new Runnable() { public void run() { System.out.println("barrier reached"); } } ) ; 
        
        executor.execute( new MyTask(1, barrier, getSessionFactory() ) );
        executor.execute( new MyTask(2, barrier, getSessionFactory() ) );
        executor.execute( new MyTask(3, barrier, getSessionFactory() ) );
        executor.execute( new MyTask(4, barrier, getSessionFactory() ) );
        executor.execute( new MyTask(5, barrier, getSessionFactory() ) );
               
        try { Thread.sleep( 10 * 1000 ); } catch(Exception ex) {} 
                    
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
        
        executor.shutdown();
        
        System.out.println("done. " + (new Date()).toString() );        
    }
}