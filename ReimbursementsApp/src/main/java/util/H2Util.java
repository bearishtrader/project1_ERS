package util;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2Util {
    public static String url = "jdbc:h2:./h2/db";
    public static String username = "sa";
    public static String password = "sa";
    static Logger logger = Logger.getLogger(H2Util.class);

    public static void createErsUserRolesTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "CREATE TABLE ers_user_roles (\n" +
                    "\ters_user_role_id serial,\n" +
                    "\tuser_role varchar(10),\n" +
                    "\tCONSTRAINT ers_user_roles_pk PRIMARY KEY (ers_user_role_id)\n" +
                    ");";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void createErsUsersTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "CREATE TABLE ers_users (\n" +
                    "\ters_users_id serial,\n" +
                    "\ters_username varchar(50),\n" +
                    "\ters_password varchar(50),\n" +
                    "\tuser_first_name varchar(100),\n" +
                    "\tuser_last_name varchar(100),\n" +
                    "\tuser_email varchar(150),\n" +
                    "\tuser_role_id integer,\n" +
                    "\tCONSTRAINT ers_users_pk PRIMARY KEY (ers_users_id),\n" +
                    "\tCONSTRAINT ers_users__UNv1 UNIQUE (ers_username, user_email),\n" +
                    "\tCONSTRAINT ers_user_roles_fk FOREIGN KEY (user_role_id) REFERENCES ers_user_roles(ers_user_role_id)\n" +
                    ");";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }
    public static void createErsReimbursementStatusTable() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "CREATE TABLE ers_reimbursement_status (\n" +
                    "\treimb_status_id serial,\n" +
                    "\treimb_status varchar(10),\n" +
                    "\tCONSTRAINT reimb_status_pk PRIMARY KEY (reimb_status_id)\n" +
                    ");";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void createErsReimbursementTypeTable() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "CREATE TABLE ers_reimbursement_type (\n" +
                    "\treimb_type_id serial,\n" +
                    "\treimb_type varchar(10),\n" +
                    "\tCONSTRAINT reimb_type_pk PRIMARY KEY (reimb_type_id)\n" +
                    ");";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void createErsReimbursementTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "CREATE TABLE ers_reimbursement (\n" +
                    "\treimb_id serial,\n" +
                    "\treimb_amount double precision DEFAULT 0.0,\n" +
                    "\treimb_submitted timestamp,\n" +
                    "\treimb_resolved timestamp,\n" +
                    "\treimb_description varchar(250),\n" +
                    "\treimb_receipt bytea,\n" +
                    "\treimb_author integer,\n" +
                    "\treimb_resolver integer,\n" +
                    "\treimb_status_id integer,\n" +
                    "\treimb_type_id integer,\n" +
                    "\tCONSTRAINT ers_reimbursement_pk PRIMARY KEY (reimb_id),\n" +
                    "\tCONSTRAINT ers_users_fk_auth FOREIGN KEY (reimb_resolver) REFERENCES ers_users(ers_users_id),\n" +
                    "\tCONSTRAINT ers_users_fk_reslvr FOREIGN KEY (reimb_resolver) REFERENCES ers_users(ers_users_id),\n" +
                    "\tCONSTRAINT ers_reimbursement_status_fk FOREIGN KEY (reimb_status_id) REFERENCES ers_reimbursement_status(reimb_status_id),\n" +
                    "\tCONSTRAINT ers_reimbursement_type_fk FOREIGN KEY (reimb_type_id) REFERENCES ers_reimbursement_type(reimb_type_id)\n" +
                    ");";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void populateErsReimbursementStatusTable(){
        try (Connection conn = DriverManager.getConnection(url, username, password);) {
            String [] sqlArr = {
                    "INSERT INTO ers_reimbursement_status VALUES (1, 'Pending');",
                    "INSERT INTO ers_reimbursement_status VALUES (2, 'Approved');",
                    "INSERT INTO ers_reimbursement_status VALUES (3, 'Denied');"
            };
            for (int i=0; i<sqlArr.length; i++) {
                String sql = sqlArr[i];
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void populateErsReimbursementTypeTable(){
        try (Connection conn = DriverManager.getConnection(url, username, password);) {
            String [] sqlArr = {
                "INSERT INTO ers_reimbursement_type VALUES (1, 'LODGING');",
                "INSERT INTO ers_reimbursement_type VALUES (2, 'TRAVEL');",
                "INSERT INTO ers_reimbursement_type VALUES (3, 'FOOD');",
                "INSERT INTO ers_reimbursement_type VALUES (4, 'OTHER');"
            };
            for (int i=0; i<sqlArr.length; i++) {
                String sql = sqlArr[i];
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void populateErsUserRolesTable(){
        try (Connection conn = DriverManager.getConnection(url, username, password);) {
            String [] sqlArr = {
            "INSERT INTO ers_user_roles VALUES (1, 'Employee');",
            "INSERT INTO ers_user_roles VALUES (2, 'FinManager');"
            };
            for (int i=0; i<sqlArr.length; i++) {
                String sql = sqlArr[i];
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void dropErsReimbursementTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "DROP TABLE ers_reimbursement;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void dropErsReimbursementTypeTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "DROP TABLE ers_reimbursement_type;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void dropErsReimbursementStatusTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "DROP TABLE ers_reimbursement_status;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void dropErsUsersTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "DROP TABLE ers_users;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }

    public static void dropErsUserRolesTable(){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "DROP TABLE ers_user_roles;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.close();
        }catch(SQLException e) {
            logger.error(e);
        }
    }
}