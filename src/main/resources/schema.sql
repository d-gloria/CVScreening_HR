CREATE TABLE perdorues (
  id_perdorues INT AUTO_INCREMENT PRIMARY KEY,
  emri VARCHAR(100),
  mbiemri VARCHAR(100),
  adresa_email VARCHAR(150) UNIQUE,
  fjalekalimi VARCHAR(255),
  roli VARCHAR(50),
  data_regjistrimit DATE,
  gjendja VARCHAR(30)
);
CREATE TABLE kandidat (
  id_kandidat INT AUTO_INCREMENT PRIMARY KEY,
  id_perdorues INT UNIQUE,
  data_lindjes DATE,
  gjinia VARCHAR(20),
  vendbanimi VARCHAR(150),
  numri_celular VARCHAR(50),
  niveli_arsimor VARCHAR(50),
  fusha_studimit VARCHAR(100),
  vite_eksperience INT,
  gjuhet_e_huaja VARCHAR(200),
  aftesite_profesionale VARCHAR(500),
  CONSTRAINT fk_kandidat_perdorues FOREIGN KEY (id_perdorues)
    REFERENCES perdorues(id_perdorues)
);

CREATE TABLE cv (
  id_cv INT AUTO_INCREMENT PRIMARY KEY,
  id_kandidat INT NOT NULL,
  emri_skedarit VARCHAR(255),
  formati_skedarit VARCHAR(20),
  madhesia_kb INT,
  data_ngarkimit DATE,
  version VARCHAR(20),
  perputhshmeria_me_pozicionin DOUBLE,
  CONSTRAINT fk_cv_kandidat FOREIGN KEY (id_kandidat)
    REFERENCES kandidat(id_kandidat)
);

CREATE TABLE pozicion_pune (
  id_pozicion INT AUTO_INCREMENT PRIMARY KEY,
  titulli_pozicionit VARCHAR(200),
  pershkrimi_detyra VARCHAR(1000),
  departamenti VARCHAR(200),
  data_hapjes DATE,
  data_aplikimit DATE,
  kriteret VARCHAR(1000),
  gjendja VARCHAR(30),
  krijuar_nga INT,
  CONSTRAINT fk_pozicion_perdorues FOREIGN KEY (krijuar_nga)
    REFERENCES perdorues(id_perdorues)
);

CREATE TABLE aplikim (
  id_aplikimi INT AUTO_INCREMENT PRIMARY KEY,
  id_kandidat INT NOT NULL,
  id_pozicion INT NOT NULL,
  id_cv INT NOT NULL,
  data_aplikimit DATE,
  vleresimi DOUBLE,
  gjendja VARCHAR(30),
  komentet_rekrutuesit VARCHAR(1000),
  CONSTRAINT fk_aplikim_kandidat FOREIGN KEY (id_kandidat)
    REFERENCES kandidat(id_kandidat),
  CONSTRAINT fk_aplikim_pozicion FOREIGN KEY (id_pozicion)
    REFERENCES pozicion_pune(id_pozicion),
  CONSTRAINT fk_aplikim_cv FOREIGN KEY (id_cv)
    REFERENCES cv(id_cv)
);

CREATE TABLE interviste (
  id_interviste INT AUTO_INCREMENT PRIMARY KEY,
  id_aplikimi INT NOT NULL,
  data_intervistes DATE,
  ora VARCHAR(10),
  vendi VARCHAR(200),
  vleresimi_rekrutuesit INT,
  pershtypja VARCHAR(1000),
  rezultati VARCHAR(30),
  CONSTRAINT fk_interviste_aplikim FOREIGN KEY (id_aplikimi)
    REFERENCES aplikim(id_aplikimi)
);

CREATE TABLE njoftim (
  id_njoftim INT AUTO_INCREMENT PRIMARY KEY,
  id_perdorues INT NOT NULL,
  menyra_dergimit VARCHAR(30),
  permbajtja VARCHAR(2000),
  data_dergimit DATE,
  statusi_leximit VARCHAR(30),
  CONSTRAINT fk_njoftim_perdorues FOREIGN KEY (id_perdorues)
    REFERENCES perdorues(id_perdorues)
);

CREATE TABLE raport (
  id_raport INT AUTO_INCREMENT PRIMARY KEY,
  id_rekrutues INT NOT NULL,
  data_gjenerimit DATE,
  lloji_raportit VARCHAR(50),
  pershkrimi VARCHAR(2000),
  formati_raportit VARCHAR(20),
  CONSTRAINT fk_raport_perdorues FOREIGN KEY (id_rekrutues)
    REFERENCES perdorues(id_perdorues)
);

CREATE TABLE regjister_auditi (
  id_veprimi INT AUTO_INCREMENT PRIMARY KEY,
  id_perdorues INT NOT NULL,
  objekti VARCHAR(100),
  veprimi VARCHAR(100),
  data_veprimit DATE,
  ora_veprimit VARCHAR(10),
  pershkrimi_detajuar VARCHAR(2000),
  CONSTRAINT fk_audit_perdorues FOREIGN KEY (id_perdorues)
    REFERENCES perdorues(id_perdorues)
);