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

