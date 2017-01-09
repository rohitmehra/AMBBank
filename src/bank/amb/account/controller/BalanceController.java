package bank.amb.account.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class BalanceController {

	public void analyze(String email_id, String commit_id) {
		System.out.println("Analysis Violations Started For: " + email_id + " @ " + UsefulMethods.currentTime());

		Jenkins jenkins = new Jenkins();

		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		int build_number = jenkins.startJob(UsefulMethods.getProperty("server_url")+":"+UsefulMethods.getProperty("jenkins_port"), UsefulMethods.getProperty("project_name"));
		System.out.println("Jenkins Job Started @ " + UsefulMethods.currentTime());
		
		ViolationsDeveloper violation = new ViolationsDeveloper();
		violation.setCommit_id(commit_id);
		violation.setBuild_number(build_number);
		violation.setEmail_id(email_id);
		
		session.persist(violation);// persisting the object
		
		t.commit();// transaction is committed
		session.close();

	}
}
