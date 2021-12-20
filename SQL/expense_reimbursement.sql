CREATE TABLE ers_user_roles (
	ers_user_role_id serial,
	user_role varchar(10),
	CONSTRAINT ers_user_roles_pk PRIMARY KEY (ers_user_role_id)
);
CREATE TABLE ers_users (
	ers_users_id serial,
	ers_username varchar(50),
	ers_password varchar(50),
	user_first_name varchar(100),
	user_last_name varchar(100),
	user_email varchar(150),
	user_role_id integer,
	CONSTRAINT ers_users_pk PRIMARY KEY (ers_users_id),
	CONSTRAINT ers_users__UNv1 UNIQUE (ers_username, user_email),
	CONSTRAINT ers_user_roles_fk FOREIGN KEY (user_role_id) REFERENCES ers_user_roles(ers_user_role_id)
);
CREATE TABLE ers_reimbursement_status (
	reimb_status_id serial,
	reimb_status varchar(10),
	CONSTRAINT reimb_status_pk PRIMARY KEY (reimb_status_id)
);
CREATE TABLE ers_reimbursement_type (
	reimb_type_id serial,
	reimb_type varchar(10),
	CONSTRAINT reimb_type_pk PRIMARY KEY (reimb_type_id)
);
CREATE TABLE ers_reimbursement (
	reimb_id serial,
	reimb_amount double precision DEFAULT 0.0,
	reimb_submitted timestamp,
	reimb_resolved timestamp,
	reimb_description varchar(250),
	reimb_receipt bytea,
	reimb_author integer,
	reimb_resolver integer,
	reimb_status_id integer,
	reimb_type_id integer,
	CONSTRAINT ers_reimbursement_pk PRIMARY KEY (reimb_id),
	CONSTRAINT ers_users_fk_auth FOREIGN KEY (reimb_resolver) REFERENCES ers_users(ers_users_id),
	CONSTRAINT ers_users_fk_reslvr FOREIGN KEY (reimb_resolver) REFERENCES ers_users(ers_users_id),
	CONSTRAINT ers_reimbursement_status_fk FOREIGN KEY (reimb_status_id) REFERENCES ers_reimbursement_status(reimb_status_id),
	CONSTRAINT ers_reimbursement_type_fk FOREIGN KEY (reimb_type_id) REFERENCES ers_reimbursement_type(reimb_type_id)
);
INSERT INTO ers_reimbursement_status VALUES (1, 'Pending');
INSERT INTO ers_reimbursement_status VALUES (2, 'Approved');
INSERT INTO ers_reimbursement_status VALUES (3, 'Denied');
INSERT INTO ers_reimbursement_type VALUES (1, 'LODGING');
INSERT INTO ers_reimbursement_type VALUES (2, 'TRAVEL');
INSERT INTO ers_reimbursement_type VALUES (3, 'FOOD');
INSERT INTO ers_reimbursement_type VALUES (4, 'OTHER');
INSERT INTO ers_user_roles VALUES (1, 'Employee');
INSERT INTO ers_user_roles VALUES (2, 'FinManager');