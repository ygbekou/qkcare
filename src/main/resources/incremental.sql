ALTER TABLE EMPLOYEE ADD  MANAGING SMALLINT DEFAULT 1;
ALTER TABLE SECTION ADD  SHOW_IN_MENU VARCHAR(1) DEFAULT 'N';
ALTER TABLE SECTION ADD  LANGUAGE VARCHAR(20) ;
ALTER TABLE SECTION ADD  STATUS SMALLINT DEFAULT 0;
ALTER TABLE USERS ADD   FIRST_TIME_LOGIN VARCHAR(1) DEFAULT 'Y';
CREATE TABLE SECTION_ITEM (
	   SECTION_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT
	 , SECTION_ID BIGINT NOT NULL
	 , TITLE VARCHAR(50) NOT NULL
	 , DESCRIPTION VARCHAR(1000) NOT NULL
	 , PICTURE VARCHAR(150)
	 , LANGUAGE VARCHAR(20) 
	 , STATUS SMALLINT DEFAULT 0
	 , SHOW_IN_MENU VARCHAR(1) DEFAULT 'N'
	 , CREATE_DATE TIMESTAMP NOT NULL
     , MOD_DATE TIMESTAMP NOT NULL
     , MOD_BY BIGINT NOT NULL
	 , PRIMARY KEY (SECTION_ITEM_ID)
	 , CONSTRAINT FK_SECTION_ITEM_1 FOREIGN KEY (SECTION_ID)
                  REFERENCES SECTION (SECTION_ID)
);