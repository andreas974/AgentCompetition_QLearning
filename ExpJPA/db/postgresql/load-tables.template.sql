SET search_path TO exp;

--   Voraussetzungen fuer COPY:
--   1) Superuser der Datenbank
--   2) Absoluter Pfadname fuer Datei oder relativ zu %PGDATA%

COPY experiment
	FROM '@BASEDIR@/csv/experiment.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY session
	FROM '@BASEDIR@/csv/session.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY treatment_block
	FROM '@BASEDIR@/csv/treatment_block.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY period
	FROM '@BASEDIR@/csv/period.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY treatment
	FROM '@BASEDIR@/csv/treatment.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY sequence_element
	FROM '@BASEDIR@/csv/sequence_element.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY pause
	FROM '@BASEDIR@/csv/pause.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY quiz
	FROM '@BASEDIR@/csv/quiz.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY cohort
	FROM '@BASEDIR@/csv/cohort.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY protocol
	FROM '@BASEDIR@/csv/protocol.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY subject
	FROM '@BASEDIR@/csv/subject.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY membership
	FROM '@BASEDIR@/csv/membership.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY trial
	FROM '@BASEDIR@/csv/trial.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY treatment_block_has_treatment
	FROM '@BASEDIR@/csv/treatment_block_has_treatment.csv'
	DELIMITER ';' CSV HEADER;
	
	COPY subject_group
	FROM '@BASEDIR@/csv/subject_group.csv'
	DELIMITER ';' CSV HEADER;
	
	


	