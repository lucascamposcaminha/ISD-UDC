-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Reserva;
DROP TABLE Excursion;

--------------------------------- Excursion ------------------------------------
CREATE TABLE Excursion (idExcursion BIGINT NOT NULL AUTO_INCREMENT,
                        city VARCHAR(200) NOT NULL,
                        description VARCHAR(500) NOT NULL,
                        startDateTime DATETIME NOT NULL,
                        registerDateTime DATETIME NOT NULL,
                        price FLOAT NOT NULL,
                        maxPlaces INTEGER NOT NULL,
                        freePlaces INTEGER NOT NULL,
                        CONSTRAINT ExcursionPK PRIMARY KEY(idExcursion)) ENGINE = InnoDB;

--------------------------------- Reserva ------------------------------------
CREATE TABLE Reserva (idReserva BIGINT NOT NULL AUTO_INCREMENT,
                      idExcursion BIGINT NOT NULL,
                      userEmail VARCHAR(100) NOT NULL,
                      numPlaces INTEGER NOT NULL,
                      numCreditCard VARCHAR(16) NOT NULL,
                      registerDateTime DATETIME NOT NULL,
                      cancelDateTime DATETIME,
                      price FLOAT NOT NULL,
                      CONSTRAINT ReservaPK PRIMARY KEY (idReserva),
                      CONSTRAINT ExcursionFK FOREIGN KEY (idExcursion) REFERENCES Excursion(idExcursion)) ENGINE = InnoDB;