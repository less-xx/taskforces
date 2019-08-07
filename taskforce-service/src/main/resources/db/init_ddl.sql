-- Drop table
-- DROP TABLE taskforce.tf_group

CREATE TABLE IF NOT EXISTS taskforce.tf_group (
	id varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	description varchar(255) NULL,
	last_updated timestamp NULL,
	updated_by varchar(255) NULL,
	CONSTRAINT tf_group_pkey PRIMARY KEY (id)
);

-- Drop table
-- DROP TABLE taskforce.tf_entity

CREATE TABLE IF NOT EXISTS taskforce.tf_entity (
	id varchar(255) NOT NULL,
	description varchar(255) NULL,
	last_updated timestamp NULL,
	"name" varchar(255) NULL,
	updated_by varchar(255) NULL,
	tf_config text NOT NULL,
	"version" int4 NOT NULL DEFAULT 0,
	group_id varchar(255) NULL,
	CONSTRAINT tf_entity_pkey PRIMARY KEY (id),
	CONSTRAINT fk_tf_entity_group_id FOREIGN KEY (group_id) REFERENCES taskforce.tf_group(id)
);

-- Drop table
-- DROP TABLE taskforce.tf_task_execution

CREATE TABLE IF NOT EXISTS taskforce.tf_task_execution (
	id serial NOT NULL,
	start_time timestamp NULL,
	end_time timestamp NULL,
	start_by varchar(255) NULL,
	end_by varchar(255) NULL,
	last_updated timestamp NOT NULL,
	status varchar(40) NOT NULL,
	message varchar(255) NULL,
	"version" int4 NOT NULL DEFAULT 0,
	taskforce_id varchar(255) NULL,
	CONSTRAINT tf_task_execution_pkey PRIMARY KEY (id),
	CONSTRAINT fk_task_exec_entity_id FOREIGN KEY (taskforce_id) REFERENCES taskforce.tf_entity(id)
);

-- Drop table
-- DROP TABLE taskforce.tf_credentials

CREATE TABLE IF NOT EXISTS taskforce.tf_credentials (
	id varchar(255) NOT NULL,
	"catalog" varchar(255) NOT NULL,
	credentials text NOT NULL,
	last_updated timestamp NULL,
	"name" varchar(255) NOT NULL,
	updated_by varchar(255) NULL,
	CONSTRAINT tf_credentials_pkey PRIMARY KEY (id)
);

-- Drop table
-- DROP TABLE taskforce.tf_cust_res_location

CREATE TABLE IF NOT EXISTS taskforce.tf_cust_res_location (
	id varchar(255) NOT NULL,
	access_type varchar(31) NOT NULL,
	"name" varchar(255) NOT NULL,
	description varchar(255) NULL,
	last_updated timestamp NULL,
	updated_by varchar(255) NULL,
	file_path text NOT NULL,
	CONSTRAINT tf_cust_res_location_pkey PRIMARY KEY (id),
	CONSTRAINT uk_cust_res_location_name UNIQUE (name)
);




-- Drop table
-- DROP TABLE taskforce.tf_user

CREATE TABLE IF NOT EXISTS taskforce.tf_user (
	id varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT tf_user_pkey PRIMARY KEY (id),
	CONSTRAINT uk_tf_user_name UNIQUE (name)
);

-- DROP SEQUENCE taskforce.task_execution_id_seq;
CREATE SEQUENCE IF NOT EXISTS taskforce.task_execution_id_seq
	INCREMENT BY 3
	MAXVALUE 9223372036854775807
	START 10000;

	

CREATE OR REPLACE VIEW taskforce.v_taskforce_entity_execution AS 
  SELECT DISTINCT ON (t.id) t.id, t.name, t.description, t.last_updated, t.group_id, t.updated_by, 
	te.id as task_exec_id, te.start_time, te.end_time, te.start_by, te.end_by, te.status, te.message  
  FROM taskforce.tf_entity t
  LEFT JOIN taskforce.tf_task_execution te ON t.id=te.taskforce_id
  ORDER BY t.id, te.last_updated DESC; 
  
  
  