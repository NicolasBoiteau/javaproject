-- Table pour stocker les informations sur les plats au menu
CREATE DATABASE IF NOT EXISTS RestaurantDB;
USE RestaurantDB;

CREATE TABLE Menu (
    ItemID INT PRIMARY KEY,
    ItemName VARCHAR(255) NOT NULL,
    Description TEXT,
    Price DECIMAL(10, 2) NOT NULL,
    Type VARCHAR(20) NOT NULL,
    Bread INT NOT NULL,
    Meat INT NOT NULL,
    Cheese INT NOT NULL,
    Salad INT NOT NULL,
    Tomato INT NOT NULL,
    Onions INT NOT NULL
);

-- Insérer des exemples de burgers dans la table Menu avec la décomposition des ingrédients
INSERT INTO Menu (ItemID,ItemName, Description, Price, Type, Bread, Meat, Cheese, Salad, Tomato, Onions)
VALUES
    (1,'Simple Burger', 'Classic single patty burger', 5.99, 'Burger', 1, 1, 1, 1, 1, 1),
    (2,'Double Burger', 'Hearty double patty burger', 7.99, 'Burger', 1, 1, 1, 1, 1, 1),
    (3,'Mystery 4T4', 'A mysterious and unique 4-tier burger', 9.99, 'Burger', 1, 1, 1, 1, 1, 1);

-- Table pour stocker les informations sur les clients
CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    PhoneNumber VARCHAR(20),
    MembershipStatus BOOLEAN
);

-- Table pour stocker les informations sur les commandes
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY,
    CustomerID INT,
    OrderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TotalAmount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

-- Table pour stocker les détails des commandes (les plats commandés)
CREATE TABLE OrderDetails (
    OrderDetailID INT PRIMARY KEY,
    OrderID INT,
    ItemID INT,
    Quantity INT NOT NULL,
    Subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ItemID) REFERENCES Menu(ItemID)
);

-- Table pour gérer les stocks des matières premières
CREATE TABLE Stock (
    ItemID INT PRIMARY KEY,
    Quantity INT NOT NULL
);
