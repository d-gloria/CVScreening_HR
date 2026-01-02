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
