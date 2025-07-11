create database eduverse;
use eduverse;

CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    user_email VARCHAR(100) NOT NULL UNIQUE,
    user_password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Course (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(200) NOT NULL,
    description TEXT,
    category ENUM('Technical', 'Non-Technical') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    instructor_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserting technical courses
INSERT INTO Course (course_name, description, category, price, instructor_name)
VALUES
('Java Programming for Beginners', 'Learn the fundamentals of Java programming language, including object-oriented concepts and core libraries.', 'Technical', 199.99, 'Ravi Kumar'),
('Advanced Python Programming', 'Master Python programming with advanced topics like decorators, generators, and threading.', 'Technical', 299.99, 'Aarti Sharma'),
('Data Science with Python', 'Get hands-on experience in data science using Python libraries like Pandas, NumPy, and Matplotlib.', 'Technical', 249.99, 'Suresh Patel'),
('Web Development Bootcamp', 'Learn web development using HTML, CSS, JavaScript, and modern frameworks like React and Node.js.', 'Technical', 349.99, 'Vikram Singh');

-- Inserting non-technical courses
INSERT INTO Course (course_name, description, category, price, instructor_name)
VALUES
('Personal Growth and Motivation', 'This course covers techniques for improving personal growth, time management, and motivation.', 'Non-Technical', 99.99, 'Priya Desai'),
('Photography for Beginners', 'Learn the basics of photography, including camera settings, lighting, and composition.', 'Non-Technical', 149.99, 'Rohit Iyer'),
('Introduction to Mindfulness and Meditation', 'A beginner-friendly course on mindfulness, meditation, and stress management.', 'Non-Technical', 79.99, 'Sneha Gupta'),
('Public Speaking and Communication Skills', 'Master the art of public speaking and improve your communication skills for both professional and personal growth.', 'Non-Technical', 129.99, 'Amit Verma');

select * from course;

CREATE TABLE Enrollment (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    course_id INT,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('Pending', 'Completed') DEFAULT 'Pending',
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (course_id) REFERENCES Course(course_id)
);

CREATE TABLE Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT,
    payment_method ENUM('COD', 'Credit Card', 'Debit Card', 'Coupon') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollment_id) REFERENCES Enrollment(enrollment_id)
);

select * from user;
select * from payment;
select * from enrollment;
desc payment;
SET SQL_SAFE_UPDATES = 0;
DELETE FROM Payment;
DELETE FROM Enrollment;

delete from user;
delete from payment;
delete from enrollment;
delete from course;


ALTER TABLE User 
ADD COLUMN is_admin BOOLEAN DEFAULT FALSE;

INSERT INTO User (user_name, user_email, user_password, is_admin) 
VALUES ('Admin', 'admin@eduverse.com', 'admin123', TRUE);


-- Trigger to update enrollment status when payment is inserted
DELIMITER $$
CREATE TRIGGER after_payment_insert
AFTER INSERT ON Payment
FOR EACH ROW
BEGIN
    UPDATE Enrollment
    SET payment_status = 'Completed'
    WHERE enrollment_id = NEW.enrollment_id;
END$$
DELIMITER ;

-- Optional: Trigger to prevent modification of completed payments
DELIMITER $$
CREATE TRIGGER before_payment_update
BEFORE UPDATE ON Payment
FOR EACH ROW
BEGIN
    IF OLD.payment_status = 'Completed' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot modify completed payments';
    END IF;
END$$
DELIMITER ;

ALTER TABLE payment MODIFY COLUMN payment_method VARCHAR(50);
ALTER TABLE enrollment
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id)
REFERENCES user(user_id)
ON DELETE CASCADE;

ALTER TABLE enrollment
ADD CONSTRAINT fk_course
FOREIGN KEY (course_id)
REFERENCES course(course_id)
ON DELETE CASCADE;


ALTER TABLE enrollment
DROP FOREIGN KEY enrollment_ibfk_2;

ALTER TABLE enrollment
ADD CONSTRAINT enrollment_ibfk_2
FOREIGN KEY (course_id) REFERENCES course(course_id)
ON DELETE CASCADE;


select * from course;
SELECT * FROM USER;

ALTER TABLE enrollment
DROP FOREIGN KEY enrollment_ibfk_1;

ALTER TABLE enrollment
ADD CONSTRAINT enrollment_ibfk_1
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE CASCADE;

ALTER TABLE payment
DROP FOREIGN KEY payment_ibfk_1;

ALTER TABLE payment
ADD CONSTRAINT payment_ibfk_1
FOREIGN KEY (enrollment_id) REFERENCES enrollment(enrollment_id)
ON DELETE CASCADE;

desc enrollment;
desc payment;

show triggers;

-- foreign keys
-- enrollment table : userid, courseid
-- payment : enrollment id