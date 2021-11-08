SET search_path TO exp;
SET default_tablespace = exp_ts;

CREATE TABLE cohort (
  id_cohort BIGSERIAL  NOT NULL ,
  session_id_session INTEGER   NOT NULL ,
  size INTEGER      ,
PRIMARY KEY(id_cohort)  );


CREATE INDEX kohorte_FKIndex1 ON cohort (session_id_session);



CREATE TABLE experiment (
  id_experiment BIGSERIAL  NOT NULL ,
  name VARCHAR(45)    ,
  description TEXT    ,
  done BOOL      ,
PRIMARY KEY(id_experiment));



CREATE TABLE pause (
  sequence_element_idsequence_element INTEGER   NOT NULL ,
  time BIGINT    ,
  message VARCHAR(255)      ,
PRIMARY KEY(sequence_element_idsequence_element)  );


CREATE INDEX break_FKIndex1 ON pause (sequence_element_idsequence_element);



CREATE TABLE period (
  id_period BIGSERIAL  NOT NULL ,
  treatment_id_treatment INTEGER   ,
  treatment_block_sequence_element_idsequence_element INTEGER   NOT NULL ,
  sequence_number INTEGER    ,
  done BOOL    ,
  practice BOOL      ,
PRIMARY KEY(id_period));


CREATE INDEX period_FKIndex1 ON period (treatment_block_sequence_element_idsequence_element);
CREATE INDEX period_FKIndex2 ON period (treatment_id_treatment);



CREATE TABLE protocol (
  idprotocol BIGSERIAL  NOT NULL ,
  quiz_sequence_element_idsequence_element INTEGER   NOT NULL ,
  subject_id_subject INTEGER   NOT NULL ,
  solution TEXT    ,
  passed BOOL      ,
PRIMARY KEY(idprotocol)    );


CREATE INDEX protocol_FKIndex1 ON protocol (subject_id_subject);
CREATE INDEX protocol_FKIndex2 ON protocol (quiz_sequence_element_idsequence_element);



CREATE TABLE quiz (
  sequence_element_idsequence_element INTEGER   NOT NULL ,
  quiz_factory_key VARCHAR(255)      ,
PRIMARY KEY(sequence_element_idsequence_element)    );


CREATE INDEX quiz_FKIndex1 ON quiz (sequence_element_idsequence_element);



CREATE TABLE sequence_element (
  idsequence_element BIGSERIAL  NOT NULL ,
  session_id_session INTEGER   NOT NULL ,
  sequence_number INTEGER    ,
  done BOOL    ,
  type_2 VARCHAR(45)      ,
PRIMARY KEY(idsequence_element,session_id_session)  );


CREATE INDEX sequence_element_FKIndex1 ON sequence_element (session_id_session);



CREATE TABLE session (
  id_session BIGSERIAL  NOT NULL ,
  experiment_id_experiment INTEGER   NOT NULL ,
  name VARCHAR(45)    ,
  description TEXT    ,
  planned_date TIMESTAMP    ,
  done BOOL      ,
  started BOOL      ,
PRIMARY KEY(id_session , experiment_id_experiment)  );


CREATE INDEX session_FKIndex1 ON session (experiment_id_experiment);



CREATE TABLE subject (
  id_subject BIGSERIAL  NOT NULL ,
  cohort_id_cohort INTEGER   NOT NULL ,
  id_client VARCHAR(60)    ,
  payoff DOUBLE PRECISION      ,
PRIMARY KEY(id_subject)  );


CREATE INDEX subject_FKIndex1 ON subject (cohort_id_cohort);



CREATE TABLE subject_group (
  id_subject_group BIGSERIAL  NOT NULL ,
  period_id_period BIGINT   NOT NULL   ,
PRIMARY KEY(id_subject_group)  );


CREATE INDEX Game_FKIndex2 ON subject_group (period_id_period);



CREATE TABLE membership (
  id_membership BIGSERIAL  NOT NULL ,
  subject_group_id_subject_group BIGINT   NOT NULL ,
  subject_id_subject INTEGER   NOT NULL ,
  role VARCHAR(255)      ,
PRIMARY KEY(id_membership)    );


CREATE INDEX membership_FKIndex1 ON membership (subject_id_subject);
CREATE INDEX membership_FKIndex2 ON membership (subject_group_id_subject_group);



CREATE TABLE treatment (
  id_treatment BIGSERIAL  NOT NULL ,
  name VARCHAR(45)    ,
  description TEXT    ,
  institution_factory_key VARCHAR(200)   NOT NULL ,
  environment_factory_key VARCHAR(200)   NOT NULL   ,
PRIMARY KEY(id_treatment)    );


CREATE TABLE treatment_block (
  sequence_element_idsequence_element INTEGER   NOT NULL ,
  name VARCHAR(45)    ,
  description TEXT    ,
  practice BOOL      ,
  randomization VARCHAR(45)      ,
PRIMARY KEY(sequence_element_idsequence_element)  );


CREATE INDEX treatment_block_FKIndex2 ON treatment_block (sequence_element_idsequence_element);



CREATE TABLE treatment_block_has_treatment (
  treatment_block_sequence_element_idsequence_element INTEGER   NOT NULL ,
  treatment_id_treatment INTEGER   NOT NULL   ,
PRIMARY KEY(treatment_block_sequence_element_idsequence_element, treatment_id_treatment)    );


CREATE INDEX treatment_block_has_treatment_FKIndex1 ON treatment_block_has_treatment (treatment_block_sequence_element_idsequence_element);
CREATE INDEX treatment_block_has_treatment_FKIndex2 ON treatment_block_has_treatment (treatment_id_treatment);



CREATE TABLE trial (
  id_trial BIGSERIAL  NOT NULL ,
  subject_group_id_subject_group BIGINT   NOT NULL ,
  subject_id_subject INTEGER    ,
  screen_name VARCHAR(255)    ,
  event VARCHAR(255)   NOT NULL ,
  value VARCHAR(1024)    ,
  client_time BIGINT    ,
  server_time BIGINT      ,
PRIMARY KEY(id_trial)    );


CREATE INDEX trial_FKIndex1 ON trial (subject_group_id_subject_group);
CREATE INDEX trial_FKIndex2 ON trial (subject_id_subject);





































