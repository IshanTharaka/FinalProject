SHOW DATABASES;
DROP DATABASE IF EXISTS POS;
CREATE DATABASE IF NOT EXISTS POS;
SHOW DATABASES;
USE POS;

#===============
CREATE TABLE IF NOT EXISTS Customer(
    id VARCHAR(45),
    name VARCHAR(45),
    address TEXT,
    salary DOUBLE,
    CONSTRAINT PRIMARY KEY (id)
);

#===============
CREATE TABLE IF NOT EXISTS Product(
    code VARCHAR(45),
    description VARCHAR(50),
    unitPrice DOUBLE,
    qtyOnHand INT,
    CONSTRAINT PRIMARY KEY (code)
);

#===============
CREATE TABLE IF NOT EXISTS `Order`(
    orderId VARCHAR(45),
    placeDate VARCHAR(250),
    total DOUBLE,
    customer VARCHAR(45),
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (customer) REFERENCES Customer (id)
                                  ON DELETE CASCADE ON UPDATE CASCADE
);

#===============
CREATE TABLE IF NOT EXISTS `Order Details`(
    productCode VARCHAR(45),
    orderID VARCHAR(45),
    unitPrice DOUBLE,
    qty INT,
    CONSTRAINT PRIMARY KEY (productCode,orderId),
    CONSTRAINT FOREIGN KEY (productCode) REFERENCES Product (code)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (orderID) REFERENCES `Order` (orderId)
        ON DELETE CASCADE ON UPDATE CASCADE
);