ALTER TABLE EMPLOYEE ADD  MANAGING SMALLINT DEFAULT 1;
ALTER TABLE SECTION ADD  SHOW_IN_MENU VARCHAR(1) DEFAULT 'N';
ALTER TABLE SECTION ADD  LANGUAGE VARCHAR(20) ;
ALTER TABLE USERS ADD   FIRST_TIME_LOGIN VARCHAR(1) DEFAULT 'Y';
ALTER TABLE VISIT ADD APPOINTMENT_ID BIGINT;
ALTER TABLE VISIT ADD CONSTRAINT FOREIGN KEY FK_V_APT_1(APPOINTMENT_ID)  
REFERENCES APPOINTMENT(APPOINTMENT_ID);
--- 06/30/2019   --- 
INSERT INTO CATEGORY(CREATE_DATE, MOD_DATE, MOD_BY, CATEGORY_ID, NAME) VALUES (now(), now(), 1, 7, 'CONFIG_DATA_TYPE');
INSERT INTO CATEGORY(CREATE_DATE, MOD_DATE, MOD_BY, PARENT_CATEGORY_ID, CATEGORY_ID, NAME) VALUES (now(), now(), 1, 7, 7000, 'INT');
INSERT INTO CATEGORY(CREATE_DATE, MOD_DATE, MOD_BY, PARENT_CATEGORY_ID, CATEGORY_ID, NAME) VALUES (now(), now(), 1, 7, 7001, 'LONG');
INSERT INTO CATEGORY(CREATE_DATE, MOD_DATE, MOD_BY, PARENT_CATEGORY_ID, CATEGORY_ID, NAME) VALUES (now(), now(), 1, 7, 7002, 'STRING');
INSERT INTO CATEGORY(CREATE_DATE, MOD_DATE, MOD_BY, PARENT_CATEGORY_ID, CATEGORY_ID, NAME) VALUES (now(), now(), 1, 7, 7003, 'DECIMAL');

ALTER TABLE WEEK_DAY ADD  LANGUAGE VARCHAR(2) ;

ALTER TABLE ADMISSION MODIFY ADMISSION_DATETIME TIMESTAMP;
ALTER TABLE BED_ASSIGNMENT MODIFY START_DATE TIMESTAMP; 
ALTER TABLE BED_ASSIGNMENT MODIFY END_DATE TIMESTAMP; 

--- 07/31/2019    --- 
ALTER TABLE BILL_SERVICE MODIFY PAYER_AMOUNT DOUBLE NULL;

ALTER TABLE LAB_TEST ADD COLUMN PAYER_PRICE DOUBLE NOT NULL;
ALTER TABLE LAB_TEST ADD COLUMN PRICE DOUBLE NOT NULL;

ALTER TABLE SERVICE ADD COLUMN LAB_TEST_ID BIGINT NULL;
ALTER TABLE SERVICE ADD COLUMN MEDICINE_ID BIGINT NULL;


CREATE TABLE PATIENT_SERVICE (
	   PATIENT_SERVICE_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , SERVICE_DATE DATE NOT NULL
	 , ADMISSION_ID BIGINT NULL 
	 , VISIT_ID BIGINT NULL 
	 , SERVICE_ID BIGINT NOT NULL 
	 , NOTES VARCHAR(500)
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (PATIENT_SERVICE_ID)
     , CONSTRAINT FK_PATIENT_SERVICE_1 FOREIGN KEY (ADMISSION_ID)
                  REFERENCES ADMISSION (ADMISSION_ID)
     , CONSTRAINT FK_PATIENT_SERVICE_2 FOREIGN KEY (VISIT_ID)
                  REFERENCES VISIT (VISIT_ID)
     , CONSTRAINT FK_PATIENT_SERVICE_3 FOREIGN KEY (SERVICE_ID)
                  REFERENCES SERVICE (SERVICE_ID)
);


CREATE TABLE PATIENT_PACKAGE (
	   PATIENT_PACKAGE_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , PACKAGE_DATE DATE NOT NULL
	 , ADMISSION_ID BIGINT NULL 
	 , VISIT_ID BIGINT NULL 
	 , PACKAGE_ID BIGINT NOT NULL 
	 , NOTES VARCHAR(500)
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (PATIENT_PACKAGE_ID)
     , CONSTRAINT FK_PATIENT_PACKAGE_1 FOREIGN KEY (ADMISSION_ID)
                  REFERENCES ADMISSION (ADMISSION_ID)
     , CONSTRAINT FK_PATIENT_PACKAGE_2 FOREIGN KEY (VISIT_ID)
                  REFERENCES VISIT (VISIT_ID)
     , CONSTRAINT FK_PATIENT_PACKAGE_3 FOREIGN KEY (PACKAGE_ID)
                  REFERENCES PACKAGE (PACKAGE_ID)
);

INSERT INTO DOCTOR_ORDER_TYPE(CREATE_DATE, MOD_DATE, MOD_BY, DOCTOR_ORDER_TYPE_ID, NAME) VALUES (now(), now(), 1, 3, 'MEDICAL');

INSERT INTO DOCTOR_ORDER_TYPE(CREATE_DATE, MOD_DATE, MOD_BY, DOCTOR_ORDER_TYPE_ID, NAME) VALUES (now(), now(), 1, 4, 'BED');

ALTER TABLE BILL_SERVICE MODIFY COLUMN SERVICE_ID BIGINT NULL;

ALTER TABLE BILL_SERVICE ADD COLUMN PACKAGE_ID BIGINT NULL;

ALTER TABLE PACKAGE ADD COLUMN RATE DOUBLE NOT NULL;

ALTER TABLE BILL_SERVICE ADD COLUMN DOCTOR_ORDER_TYPE_ID BIGINT NOT NULL;

ALTER TABLE BED ADD COLUMN RATE DOUBLE NOT NULL;

ALTER TABLE BILL_SERVICE ADD COLUMN PRODUCT_ID BIGINT NULL;

ALTER TABLE BILL_SERVICE ADD COLUMN LAB_TEST_ID BIGINT NULL;

ALTER TABLE BILL_SERVICE ADD COLUMN BED_ID BIGINT NULL;



-------------      08/14/2019            ----------------

CREATE TABLE MODALITY (
	   MODALITY_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , NAME VARCHAR(50) NOT NULL
	 , DESCRIPTION VARCHAR(500) NOT NULL
	 , STATUS SMALLINT DEFAULT 0
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (MODALITY_ID)
);

CREATE TABLE BODY_PART (
	   BODY_PART_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , NAME VARCHAR(50) NOT NULL
	 , DESCRIPTION VARCHAR(500) NOT NULL
	 , STATUS SMALLINT DEFAULT 0
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (BODY_PART_ID)
);

CREATE TABLE LATERALITY (
	   LATERALITY_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , NAME VARCHAR(50) NOT NULL
	 , DESCRIPTION VARCHAR(500) NOT NULL
	 , STATUS SMALLINT DEFAULT 0
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (LATERALITY_ID)
);

CREATE TABLE RAD_EXAM (
	   EXAM_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , MODALITY_ID BIGINT NOT NULL 
	 , NAME VARCHAR(50) NOT NULL
	 , DESCRIPTION VARCHAR(500)
	 , RATE DOUBLE NOT NULL DEFAULT 0
	 , STATUS SMALLINT DEFAULT 0
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (EXAM_ID)
     , CONSTRAINT FK_RAD_EXAM_1 FOREIGN KEY (MODALITY_ID)
                  REFERENCES MODALITY (MODALITY_ID)
);

CREATE TABLE RAD_INVESTIGATION (
  	INVESTIGATION_ID BIGINT NOT NULL AUTO_INCREMENT
  , NAME VARCHAR(64) NOT NULL
  , DESCRIPTION VARCHAR(250)
  , INVESTIGATION_DATETIME DATE NOT NULL
  , ADMISSION_ID BIGINT
  , VISIT_ID BIGINT
  , DOCTOR_ORDER_ID BIGINT
  , REFERRING_PHYSICIAN_ID BIGINT
  , ASSIGN_TO BIGINT
  , URGENT VARCHAR(1)
  , START_DATETIME TIMESTAMP NULL
  , COMPLETE_DATETIME TIMESTAMP NULL
  , REJECTION_DATETIME TIMESTAMP NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , EXAM_STATUS_ID TINYINT DEFAULT 0
  , PRIMARY KEY (INVESTIGATION_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_1 FOREIGN KEY (ADMISSION_ID)
                  REFERENCES ADMISSION (ADMISSION_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_2 FOREIGN KEY (VISIT_ID)
                  REFERENCES VISIT (VISIT_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_3 FOREIGN KEY (EXAM_STATUS_ID)
                  REFERENCES EXAM_STATUS (EXAM_STATUS_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_4 FOREIGN KEY (REFERRING_PHYSICIAN_ID)
                  REFERENCES USERS (USER_ID)
);


CREATE TABLE RAD_INVESTIGATION_EXAM (
  	INVESTIGATION_EXAM_ID BIGINT NOT NULL AUTO_INCREMENT
  , INVESTIGATION_ID BIGINT NOT NULL 
  , EXAM_ID BIGINT NOT NULL
  , ACCESSION_NUMBER VARCHAR(20)
  , COMMENTS VARCHAR(1000)
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (INVESTIGATION_EXAM_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_EXAM_1 FOREIGN KEY (INVESTIGATION_ID)
                  REFERENCES RAD_INVESTIGATION (INVESTIGATION_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_EXAM_2 FOREIGN KEY (EXAM_ID)
                  REFERENCES RAD_EXAM (EXAM_ID)
);

CREATE TABLE RAD_INVESTIGATION_COMMENT (
  	INVESTIGATION_COMMENT_ID BIGINT NOT NULL AUTO_INCREMENT
  , INVESTIGATION_ID BIGINT NOT NULL 
  , TITLE VARCHAR(50)
  , COMMENTS VARCHAR(1000)
  , COMMENT_DATETIME TIMESTAMP NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (INVESTIGATION_COMMENT_ID)
  , CONSTRAINT FK_RAD_INVESTIGATION_COMMENT_1 FOREIGN KEY (INVESTIGATION_ID)
                  REFERENCES RAD_INVESTIGATION (INVESTIGATION_ID)
);


CREATE TABLE EXAM_STATUS (
  	EXAM_STATUS_ID TINYINT NOT NULL
  , NAME VARCHAR(20) NOT NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (EXAM_STATUS_ID)
);

INSERT INTO EXAM_STATUS(EXAM_STATUS_ID, NAME) VALUES (1, 'Ordonné');
INSERT INTO EXAM_STATUS(EXAM_STATUS_ID, NAME) VALUES (2, 'Assigné');
INSERT INTO EXAM_STATUS(EXAM_STATUS_ID, NAME) VALUES (3, 'Rejeté');
INSERT INTO EXAM_STATUS(EXAM_STATUS_ID, NAME) VALUES (4, 'En cours');
INSERT INTO EXAM_STATUS(EXAM_STATUS_ID, NAME) VALUES (5, 'Terminé');

INSERT INTO USER_GROUP(CREATE_DATE, MOD_DATE, MOD_BY, USER_GROUP_ID, NAME) VALUES (now(), now(), 1, 4, 'Radiology Tech');

--- 09/08/2019

CREATE TABLE ROLE (
  	ROLE_ID BIGINT NOT NULL AUTO_INCREMENT
  , NAME VARCHAR(20) NOT NULL
  , DESCRIPTION VARCHAR(50)
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (ROLE_ID)
);

CREATE TABLE USER_ROLE (
  	USER_ROLE_ID BIGINT NOT NULL AUTO_INCREMENT
  , USER_ID BIGINT NOT NULL
  , ROLE_ID BIGINT NOT NULL
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (USER_ROLE_ID)
  , CONSTRAINT FK_USER_ROLE_1 FOREIGN KEY (USER_ID)
                  REFERENCES USERS (USER_ID)
  , CONSTRAINT FK_USER_ROLE_2 FOREIGN KEY (ROLE_ID)
                  REFERENCES ROLE (ROLE_ID)
);


CREATE TABLE RESOURCE (
  	RESOURCE_ID BIGINT NOT NULL AUTO_INCREMENT
  , PARENT_ID BIGINT 
  , NAME VARCHAR(30) NOT NULL
  , URL_PATH VARCHAR(50)
  , DESCRIPTION VARCHAR(50)
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (RESOURCE_ID)
);

CREATE TABLE MENU_ITEM (
  	MENU_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT
  , PARENT_ID BIGINT 
  , RESOURCE_ID BIGINT
  , LANGUAGE VARCHAR(2) NOT NULL
  , LABEL VARCHAR(40) NOT NULL
  , ICON VARCHAR(40) 
  , LEVEL TINYINT NOT NULL
  , MI_ORDER SMALLINT NOT NULL
  , DESCRIPTION VARCHAR(100)
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (MENU_ITEM_ID)
);


CREATE TABLE PERMISSION (
  	PERMISSION_ID BIGINT NOT NULL AUTO_INCREMENT
  , ROLE_ID BIGINT NOT NULL
  , RESOURCE_ID BIGINT NOT NULL
  , CAN_ADD VARCHAR(1) NOT NULL DEFAULT 'N'
  , CAN_VIEW VARCHAR(1) NOT NULL DEFAULT 'N'
  , CAN_EDIT VARCHAR(1) NOT NULL DEFAULT 'N'
  , CAN_DELETE VARCHAR(1) NOT NULL DEFAULT 'N'
  , DESCRIPTION VARCHAR(100)
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (PERMISSION_ID)
  , CONSTRAINT FK_PERMISSION_1 FOREIGN KEY (ROLE_ID)
                  REFERENCES ROLE (ROLE_ID)
  , CONSTRAINT FK_PERMISSION_2 FOREIGN KEY (RESOURCE_ID)
                  REFERENCES RESOURCE (RESOURCE_ID)
);

ALTER TABLE VISIT MODIFY   DOCTOR_ID BIGINT NULL;
ALTER TABLE RESOURCE ADD   PARENT_ID BIGINT ;

--10/14/2019
ALTER TABLE PACKAGE ADD COLUMN GRAND_TOTAL DOUBLE NOT NULL;
ALTER TABLE PACKAGE_SERVICE ADD COLUMN DESCRIPTION VARCHAR(500);

--10/26/2019
ALTER TABLE BILL_SERVICE DROP FOREIGN KEY FK_BILL_SERVICE_2;
ALTER TABLE BILL_SERVICE DROP COLUMN SERVICE_ID;
ALTER TABLE BILL_SERVICE ADD COLUMN PATIENT_SERVICE_ID BIGINT;
ALTER TABLE BILL_SERVICE DROP COLUMN PACKAGE_ID;
ALTER TABLE BILL_SERVICE ADD COLUMN PATIENT_PACKAGE_ID BIGINT;
ALTER TABLE BILL_SERVICE DROP COLUMN PRODUCT_ID;
ALTER TABLE BILL_SERVICE ADD COLUMN PATIENT_SALE_PRODUCT_ID BIGINT;
ALTER TABLE BILL_SERVICE DROP COLUMN LAB_TEST_ID;
ALTER TABLE BILL_SERVICE ADD COLUMN INVESTIGATION_ID BIGINT;
ALTER TABLE BILL_SERVICE DROP COLUMN BED_ID;
ALTER TABLE BILL_SERVICE ADD COLUMN BED_ASSIGNMENT_ID BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN SYSTEM_GENERATED VARCHAR(1) DEFAULT 'N';

--10/30/2019
CREATE TABLE PATIENT_VACCINE (
	   PATIENT_VACCINE_ID BIGINT NOT NULL AUTO_INCREMENT 
     , PATIENT_ID BIGINT NOT NULL
     , VACCINE_ID BIGINT NOT NULL
     , GIVEN_DATE DATE NOT NULL
     , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , STATUS SMALLINT DEFAULT 0
     , PRIMARY KEY (PATIENT_VACCINE_ID)
     , CONSTRAINT FK_PATIENT_VACCINE_1 FOREIGN KEY (PATIENT_ID)
                  REFERENCES PATIENT (PATIENT_ID)
     , CONSTRAINT FK_PATIENT_VACCINE_2 FOREIGN KEY (VACCINE_ID)
                  REFERENCES VACCINE (VACCINE_ID)
);
CREATE TABLE PATIENT_MEDICALHISTORY (
	   PATIENT_MEDICALHISTORY_ID BIGINT NOT NULL AUTO_INCREMENT 
     , PATIENT_ID BIGINT NOT NULL
     , MEDICALHISTORY_ID BIGINT NOT NULL
     , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , STATUS SMALLINT DEFAULT 0
     , PRIMARY KEY (PATIENT_MEDICALHISTORY_ID)
     , CONSTRAINT FK_PATIENT_MEDICALHISTORY_1 FOREIGN KEY (PATIENT_ID)
                  REFERENCES PATIENT (PATIENT_ID)
     , CONSTRAINT FK_PATIENT_MEDICALHISTORY_2 FOREIGN KEY (MEDICALHISTORY_ID)
                  REFERENCES MEDICALHISTORY (MEDICALHISTORY_ID)
);
CREATE UNIQUE INDEX UNIQUE_PATIENT_MEDICALHIST ON PATIENT_MEDICALHISTORY (PATIENT_ID,  MEDICALHISTORY_ID);
CREATE TABLE PATIENT_SOCIALHISTORY (
	   PATIENT_SOCIALHISTORY_ID BIGINT NOT NULL AUTO_INCREMENT 
     , PATIENT_ID BIGINT NOT NULL
     , SOCIALHISTORY_ID BIGINT NOT NULL
     , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , STATUS SMALLINT DEFAULT 0
     , PRIMARY KEY (PATIENT_SOCIALHISTORY_ID)
     , CONSTRAINT FK_PATIENT_SOCIALHISTORY_1 FOREIGN KEY (PATIENT_ID)
                  REFERENCES PATIENT (PATIENT_ID)
     , CONSTRAINT FK_PATIENT_SOCIALHISTORY_2 FOREIGN KEY (SOCIALHISTORY_ID)
                  REFERENCES SOCIALHISTORY (SOCIALHISTORY_ID)
);
CREATE UNIQUE INDEX UNIQUE_PATIENT_SOCIALHIST ON PATIENT_SOCIALHISTORY (PATIENT_ID,  SOCIALHISTORY_ID);
CREATE TABLE PATIENT_ALLERGY (
	   PATIENT_ALLERGY_ID BIGINT NOT NULL AUTO_INCREMENT 
     , PATIENT_ID BIGINT NOT NULL
     , ALLERGY_ID BIGINT NOT NULL
     , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , STATUS SMALLINT DEFAULT 0
     , PRIMARY KEY (PATIENT_ALLERGY_ID)
     , CONSTRAINT FK_PATIENT_ALLERGY_1 FOREIGN KEY (PATIENT_ID)
                  REFERENCES PATIENT (PATIENT_ID)
     , CONSTRAINT FK_PATIENT_ALLERGY_2 FOREIGN KEY (ALLERGY_ID)
                  REFERENCES ALLERGY (ALLERGY_ID)
);
CREATE UNIQUE INDEX UNIQUE_PATIENT_ALLERGY ON PATIENT_ALLERGY (PATIENT_ID,  ALLERGY_ID);
ALTER TABLE VISIT MODIFY PACKAGE_ID BIGINT;
ALTER TABLE VISIT ADD COLUMN SERVICE_ID BIGINT;


--  11/03/2019
ALTER TABLE BILL_SERVICE ADD COLUMN SERVICE_ID BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN PACKAGE_ID BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN PRODUCT_ID BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN LAB_TEST_ID BIGINT;


ALTER TABLE PATIENT_SALE_PRODUCT ADD COLUMN STATUS TINYINT NOT NULL DEFAULT 0;

CREATE TABLE SALE_STATUS (
	   SALE_STATUS_ID BIGINT NOT NULL 
	 , NAME VARCHAR(30)
	 , DESCRIPTION VARCHAR(500)
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
     , PRIMARY KEY (SALE_STATUS_ID)
);

--11/03/2019
ALTER TABLE BILL_SERVICE ADD COLUMN SERVICE_ID  BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN PACKAGE_ID  BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN PRODUCT_ID  BIGINT;
ALTER TABLE BILL_SERVICE ADD COLUMN LAB_TEST_ID BIGINT;


-- 11/23/2019
CREATE TABLE PURCHASE_ORDER_STATUS (
  	PURCHASE_ORDER_STATUS_ID TINYINT NOT NULL
  , NAME VARCHAR(20) NOT NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (PURCHASE_ORDER_STATUS_ID)
);

INSERT INTO PURCHASE_ORDER_STATUS(PURCHASE_ORDER_STATUS_ID, NAME) VALUES (1, 'Draft');
INSERT INTO PURCHASE_ORDER_STATUS(PURCHASE_ORDER_STATUS_ID, NAME) VALUES (2, 'Pending');
INSERT INTO PURCHASE_ORDER_STATUS(PURCHASE_ORDER_STATUS_ID, NAME) VALUES (3, 'Approved');
INSERT INTO PURCHASE_ORDER_STATUS(PURCHASE_ORDER_STATUS_ID, NAME) VALUES (4, 'Partially Received');
INSERT INTO PURCHASE_ORDER_STATUS(PURCHASE_ORDER_STATUS_ID, NAME) VALUES (5, 'Received');
INSERT INTO PURCHASE_ORDER_STATUS(PURCHASE_ORDER_STATUS_ID, NAME) VALUES (6, 'Cancelled');

ALTER TABLE PURCHASE_ORDER ADD COLUMN PURCHASE_ORDER_STATUS_ID TINYINT NOT NULL DEFAULT 1;
ALTER TABLE PURCHASE_ORDER ADD  FOREIGN KEY  FK_PURCHASE_ORDER_4 (PURCHASE_ORDER_STATUS_ID) REFERENCES PURCHASE_ORDER_STATUS (PURCHASE_ORDER_STATUS_ID);
ALTER TABLE PURCHASE_ORDER_PRODUCT ADD COLUMN RECEIVED_DATETIME TIMESTAMP;
ALTER TABLE PURCHASE_ORDER_PRODUCT ADD COLUMN RECEIVED_QUANTITY BIGINT DEFAULT 0;

CREATE TABLE PATIENT_SALE_STATUS (
  	PATIENT_SALE_STATUS_ID TINYINT NOT NULL
  , NAME VARCHAR(20) NOT NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (PATIENT_SALE_STATUS_ID)
);

INSERT INTO PATIENT_SALE_STATUS(PATIENT_SALE_STATUS_ID, NAME) VALUES (1, 'Draft');
INSERT INTO PATIENT_SALE_STATUS(PATIENT_SALE_STATUS_ID, NAME) VALUES (2, 'Pending');
INSERT INTO PATIENT_SALE_STATUS(PATIENT_SALE_STATUS_ID, NAME) VALUES (3, 'Approved');
INSERT INTO PATIENT_SALE_STATUS(PATIENT_SALE_STATUS_ID, NAME) VALUES (4, 'Partially Delivered');
INSERT INTO PATIENT_SALE_STATUS(PATIENT_SALE_STATUS_ID, NAME) VALUES (5, 'Delivered');
INSERT INTO PATIENT_SALE_STATUS(PATIENT_SALE_STATUS_ID, NAME) VALUES (6, 'Cancelled');

ALTER TABLE PATIENT_SALE ADD COLUMN PATIENT_SALE_STATUS_ID TINYINT NOT NULL DEFAULT 1;
ALTER TABLE PATIENT_SALE ADD  FOREIGN KEY  FK_PATIENT_SALE_4 (PATIENT_SALE_STATUS_ID) REFERENCES PATIENT_SALE_STATUS (PATIENT_SALE_STATUS_ID);
ALTER TABLE PATIENT_SALE_PRODUCT ADD COLUMN DELIVERY_DATETIME TIMESTAMP;
ALTER TABLE PATIENT_SALE_PRODUCT ADD COLUMN DELIVERY_QUANTITY BIGINT DEFAULT 0;

ALTER TABLE INVESTIGATION MODIFY INVESTIGATION_DATETIME TIMESTAMP NOT NULL;


--- 12/29/2019  -----

CREATE TABLE SUMMARY_TYPE (
  	SUMMARY_TYPE_ID TINYINT NOT NULL AUTO_INCREMENT
  , NAME VARCHAR(20) NOT NULL
  , USER_GROUP_ID BIGINT NOT NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (SUMMARY_TYPE_ID)
);

CREATE TABLE SUMMARY_STATUS (
  	SUMMARY_STATUS_ID TINYINT NOT NULL
  , NAME VARCHAR(20) NOT NULL
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (SUMMARY_STATUS_ID)
);

CREATE TABLE SUMMARY (
	   SUMMARY_ID BIGINT NOT NULL AUTO_INCREMENT 
	 , VISIT_ID BIGINT 
	 , ADMISSION_ID BIGINT 
	 , SUMMARY_TYPE_ID TINYINT NOT NULL 
	 , SUMMARY_STATUS_ID TINYINT NOT NULL 
	 , AUTHOR_ID BIGINT NOT NULL
	 , SUMMARY_DATETIME TIMESTAMP NOT NULL DEFAULT NOW()
	 , SUBJECT VARCHAR(500) NOT NULL
	 , DESCRIPTION VARCHAR(5000) NOT NULL
	 , PATIENT_LEVEL_DOC VARCHAR(1) NOT NULL DEFAULT 'N'
	 , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
	 , MOD_BY BIGINT NOT NULL DEFAULT 1	
   	 , PRIMARY KEY (SUMMARY_ID)
     , CONSTRAINT FK_SUMMARY_1 FOREIGN KEY (VISIT_ID)
               REFERENCES VISIT (VISIT_ID)
     , CONSTRAINT FK_SUMMARY_2 FOREIGN KEY (ADMISSION_ID)
               REFERENCES ADMISSION (ADMISSION_ID)
     , CONSTRAINT FK_SUMMARY_3 FOREIGN KEY (AUTHOR_ID)
               REFERENCES EMPLOYEE (EMPLOYEE_ID)
     , CONSTRAINT FK_SUMMARY_4 FOREIGN KEY (SUMMARY_TYPE_ID)
               REFERENCES SUMMARY_TYPE (SUMMARY_TYPE_ID)
     , CONSTRAINT FK_SUMMARY_5 FOREIGN KEY (SUMMARY_STATUS_ID)
               REFERENCES SUMMARY_STATUS (SUMMARY_STATUS_ID)
);


INSERT INTO SUMMARY_STATUS(SUMMARY_STATUS_ID, NAME) VALUES (1, 'Draft');
INSERT INTO SUMMARY_STATUS(SUMMARY_STATUS_ID, NAME) VALUES (2, 'Submitted');


INSERT INTO SUMMARY_TYPE(SUMMARY_TYPE_ID, USER_GROUP_ID, NAME) VALUES (1, 3, 'PIEP Note');
INSERT INTO SUMMARY_TYPE(SUMMARY_TYPE_ID, USER_GROUP_ID, NAME) VALUES (2, 3, 'Progress Note');
INSERT INTO SUMMARY_TYPE(SUMMARY_TYPE_ID, USER_GROUP_ID, NAME) VALUES (3, 2, 'H&P Note');


--  12/31/2019   ---
INSERT INTO CATEGORY(CREATE_DATE, MOD_DATE, MOD_BY, CATEGORY_ID, NAME) VALUES (now(), now(), 1, 8, 'PE_SYSTEM_TYPE');
DROP TABLE SYSTEM_REVIEW_RESULT;
DROP TABLE SYSTEM_REVIEW;
DROP TABLE SYSTEM_REVIEW_Q_ASSIGNMENT;
DROP TABLE SYSTEM_REVIEW_QUESTION;
DROP TABLE PHYSICAL_EXAM_RESULT;
DROP TABLE PHYSICAL_EXAM;
DROP TABLE PHYS_EXAM_TYPE_ASSIGNMENT;
DROP TABLE PHYSICAL_EXAM_SYSTEM;
DROP TABLE SUMMARY_TYPE_TEMPLATE;
CREATE TABLE SUMMARY_TYPE_TEMPLATE (
  	SUMMARY_TYPE_TEMPLATE_ID BIGINT NOT NULL AUTO_INCREMENT
  , SUMMARY_TYPE_ID TINYINT NOT NULL
  , TEMPLATE TEXT
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (SUMMARY_TYPE_TEMPLATE_ID)
);

CREATE TABLE PHYSICAL_EXAM_SYSTEM (
  PHYSICAL_EXAM_SYSTEM_ID BIGINT NOT NULL AUTO_INCREMENT
  , PARENT_ID BIGINT
  , NAME VARCHAR(64) NOT NULL
  , DESCRIPTION TEXT
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (PHYSICAL_EXAM_SYSTEM_ID)
  , CONSTRAINT FK_PHYSICAL_EXAM_SYSTEM_1 FOREIGN KEY (PARENT_ID)
                  REFERENCES PHYSICAL_EXAM_SYSTEM (PHYSICAL_EXAM_SYSTEM_ID)
);

CREATE TABLE PHYS_EXAM_TYPE_ASSIGNMENT (
  	PHYS_EXAM_TYPE_ASSIGNMENT_ID BIGINT NOT NULL AUTO_INCREMENT
  , PHYSICAL_EXAM_SYSTEM_ID BIGINT NOT NULL
  , SUMMARY_TYPE_ID TINYINT NOT NULL
  , DESCRIPTION TEXT
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (PHYS_EXAM_TYPE_ASSIGNMENT_ID)
  , CONSTRAINT FK_PHYS_EXAM_TYPE_ASSIGNMENT_1 FOREIGN KEY (PHYSICAL_EXAM_SYSTEM_ID)
                  REFERENCES PHYSICAL_EXAM_SYSTEM (PHYSICAL_EXAM_SYSTEM_ID)
);

CREATE TABLE PHYSICAL_EXAM (
  PHYSICAL_EXAM_ID BIGINT NOT NULL AUTO_INCREMENT
  , VISIT_ID BIGINT 
  , ADMISSION_ID BIGINT 
  , AUTHOR_ID BIGINT NOT NULL
  , PHYSICAL_EXAM_DATETIME TIMESTAMP NOT NULL DEFAULT NOW()
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (PHYSICAL_EXAM_ID)
  , CONSTRAINT FK_PHYSICAL_EXAM_1 FOREIGN KEY (VISIT_ID)
                  REFERENCES VISIT (VISIT_ID)
  , CONSTRAINT FK_PHYSICAL_EXAM_2 FOREIGN KEY (ADMISSION_ID)
                  REFERENCES ADMISSION (ADMISSION_ID)
  , CONSTRAINT FK_PHYSICAL_EXAM_3 FOREIGN KEY (AUTHOR_ID)
               REFERENCES EMPLOYEE (EMPLOYEE_ID)
);

CREATE TABLE PHYSICAL_EXAM_RESULT (
	   PHYSICAL_EXAM_RESULT_ID BIGINT NOT NULL AUTO_INCREMENT 
     , PHYSICAL_EXAM_ID BIGINT NOT NULL
     , PHYSICAL_EXAM_SYSTEM_ID BIGINT NOT NULL
     , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , STATUS SMALLINT DEFAULT 0
     , PRIMARY KEY (PHYSICAL_EXAM_RESULT_ID)
     , CONSTRAINT FK_PHYSICAL_EXAM_RESULT_1 FOREIGN KEY (PHYSICAL_EXAM_ID)
                  REFERENCES PHYSICAL_EXAM (PHYSICAL_EXAM_ID)
     , CONSTRAINT FK_PHYSICAL_EXAM_RESULT_2 FOREIGN KEY (PHYSICAL_EXAM_SYSTEM_ID)
                  REFERENCES PHYSICAL_EXAM_SYSTEM (PHYSICAL_EXAM_SYSTEM_ID)
);
CREATE UNIQUE INDEX UNIQUE_PHYSICAL_EXAM_RESULT ON PHYSICAL_EXAM_RESULT (PHYSICAL_EXAM_ID,  PHYSICAL_EXAM_SYSTEM_ID);

CREATE TABLE SYSTEM_REVIEW_QUESTION (
  SYSTEM_REVIEW_QUESTION_ID BIGINT NOT NULL AUTO_INCREMENT
  , PARENT_ID BIGINT
  , NAME VARCHAR(64) NOT NULL
  , DESCRIPTION TEXT
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (SYSTEM_REVIEW_QUESTION_ID)
  , CONSTRAINT FK_SYSTEM_REVIEW_QUESTION_1 FOREIGN KEY (PARENT_ID)
                  REFERENCES SYSTEM_REVIEW_QUESTION (SYSTEM_REVIEW_QUESTION_ID)
);

CREATE TABLE SYSTEM_REVIEW_Q_ASSIGNMENT (
  	SYSTEM_REVIEW_Q_ASSIGNMENT_ID BIGINT NOT NULL AUTO_INCREMENT
  , SYSTEM_REVIEW_QUESTION_ID BIGINT NOT NULL
  , SUMMARY_TYPE_ID TINYINT NOT NULL
  , DESCRIPTION TEXT
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (SYSTEM_REVIEW_Q_ASSIGNMENT_ID)
  , CONSTRAINT FK_SYSTEM_REVIEW_Q_ASSIGNMENT_1 FOREIGN KEY (SYSTEM_REVIEW_QUESTION_ID)
                  REFERENCES SYSTEM_REVIEW_QUESTION (SYSTEM_REVIEW_QUESTION_ID)
);

CREATE TABLE SYSTEM_REVIEW (
  SYSTEM_REVIEW_ID BIGINT NOT NULL AUTO_INCREMENT
  , VISIT_ID BIGINT 
  , ADMISSION_ID BIGINT 
  , AUTHOR_ID BIGINT NOT NULL
  , SYSTEM_REVIEW_DATETIME TIMESTAMP NOT NULL DEFAULT NOW()
  , STATUS SMALLINT DEFAULT 0
  , CREATE_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_DATE TIMESTAMP NOT NULL DEFAULT NOW()
  , MOD_BY BIGINT NOT NULL DEFAULT 1
  , PRIMARY KEY (SYSTEM_REVIEW_ID)
  , CONSTRAINT FK_SYSTEM_REVIEW_1 FOREIGN KEY (VISIT_ID)
                  REFERENCES VISIT (VISIT_ID)
  , CONSTRAINT FK_SYSTEM_REVIEW_2 FOREIGN KEY (ADMISSION_ID)
                  REFERENCES ADMISSION (ADMISSION_ID)
  , CONSTRAINT FK_SYSTEM_REVIEW_3 FOREIGN KEY (AUTHOR_ID)
               REFERENCES EMPLOYEE (EMPLOYEE_ID)
);

CREATE TABLE SYSTEM_REVIEW_RESULT (
	   SYSTEM_REVIEW_RESULT_ID BIGINT NOT NULL AUTO_INCREMENT 
     , SYSTEM_REVIEW_ID BIGINT NOT NULL
     , SYSTEM_REVIEW_QUESTION_ID BIGINT NOT NULL
     , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , STATUS SMALLINT DEFAULT 0
     , PRIMARY KEY (SYSTEM_REVIEW_RESULT_ID)
     , CONSTRAINT FK_SYSTEM_REVIEW_RESULT_1 FOREIGN KEY (SYSTEM_REVIEW_ID)
                  REFERENCES SYSTEM_REVIEW (SYSTEM_REVIEW_ID)
     , CONSTRAINT FK_SYSTEM_REVIEW_RESULT_2 FOREIGN KEY (SYSTEM_REVIEW_QUESTION_ID)
                  REFERENCES SYSTEM_REVIEW_QUESTION (SYSTEM_REVIEW_QUESTION_ID)
);
CREATE UNIQUE INDEX UNIQUE_SYSTEM_REVIEW_RESULT ON SYSTEM_REVIEW_RESULT (SYSTEM_REVIEW_ID,  SYSTEM_REVIEW_QUESTION_ID);


--01/05/2020
ALTER TABLE MENU_ITEM MODIFY LEVEL TINYINT NOT NULL DEFAULT 0;
ALTER TABLE MENU_ITEM MODIFY MI_ORDER SMALLINT NOT NULL DEFAULT 0;

--01/12/20
ALTER TABLE ROLE ADD COLUMN RESOURCE_ID BIGINT;
-- 01/12/2020
ALTER TABLE SUMMARY ADD COLUMN CHIEF_OF_COMPLAIN VARCHAR(500);
ALTER TABLE SUMMARY ADD COLUMN MEDICAL_HISTORY VARCHAR(1000)
