# In-N-Out Management System

## Overview

The In-N-Out Management System is a project developed in Java and SQL by a team of three members. It serves as a comprehensive management solution for handling stock, customer orders, and providing estimated wait times.

## Prerequisites

Before launching the application, ensure you have the following:

- [Java](https://www.java.com/en/download/) installed
- [MariaDB](https://mariadb.org/download/) installed
- Database initialized using `database.sql`

## Getting Started

1. **Launch the Launcher:**

    Execute the launcher to access the dashboard and create a new burger.

    ```bash
    java -jar launcher.jar
    ```

2. **Database Setup:**

    - Make sure MariaDB is running.
    - Execute the `database.sql` script to initialize the required database structure.

        ```bash
        mysql -u your_username -p < database.sql
        ```

3. **Dashboard:**

    The dashboard provides a centralized view for managing stock, customer orders, and menu.

4. **Client Section - Order a Burger:**

    Navigate to the "Order a Burger" section to place customer orders.

## Features

- **Stock Management:** Track and manage the inventory of ingredients.
  
- **Order Processing:** Handle customer orders efficiently.

- **Wait Time Estimation:** Provide estimated wait times for customer orders.

## Team Members

- Member 1: [Nicolas B]
- Member 2: [Marwan B]
- Member 3: [Doriane F]

## Technologies Used

- Java
- SQL (MariaDB)
