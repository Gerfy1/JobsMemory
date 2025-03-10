CREATE TABLE IF NOT EXISTS Users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS JobApplications (
                                               id SERIAL PRIMARY KEY,
                                               user_id INT,
                                               job_name VARCHAR(255) NOT NULL,
    job_description VARCHAR(255) NOT NULL,
    job_link VARCHAR(255),
    stage VARCHAR(255),
    status VARCHAR(50),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id)
    );

CREATE TABLE IF NOT EXISTS Reminders (
                                         id SERIAL PRIMARY KEY,
                                         job_application_id INT,
                                         reminder_date DATE,
                                         description TEXT,
                                         FOREIGN KEY (job_application_id) REFERENCES JobApplications(id)
    );

CREATE TABLE IF NOT EXISTS Notifications (
                                             id SERIAL PRIMARY KEY,
                                             user_id INT,
                                             message TEXT,
                                             read BOOLEAN DEFAULT FALSE,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (user_id) REFERENCES Users(id)
    );