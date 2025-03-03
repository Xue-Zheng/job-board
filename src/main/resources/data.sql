-- Create users table
CREATE TABLE IF NOT EXISTS users (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
  );

-- Create jobs table
CREATE TABLE IF NOT EXISTS jobs (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  employer_id BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL,
  post_date DATE NOT NULL,
  deadline_date DATE,
  FOREIGN KEY (employer_id) REFERENCES users(id)
  );

-- Create applications table
CREATE TABLE IF NOT EXISTS applications (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          job_id BIGINT NOT NULL,
                                          applicant_id BIGINT NOT NULL,
                                          status VARCHAR(50) NOT NULL,
  application_date DATE NOT NULL,
  cover_letter TEXT,
  FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
  FOREIGN KEY (applicant_id) REFERENCES users(id),
  CONSTRAINT unique_job_applicant UNIQUE (job_id, applicant_id)
  );

-- Initial user data
-- Password is 'password' for all users (using BCrypt encryption)
INSERT INTO users (name, email, password, role) VALUES
                                                  ('Admin User', 'admin@etu.nz', '$2a$10$yiUGnzZpkNeXP4KFHttijeg7v3jhnJBtbi6So.OoG4pYehLBKYaBG', 'ADMIN'),
                                                  ('Test Employer', 'employer@etu.nz', '$2a$10$yiUGnzZpkNeXP4KFHttijeg7v3jhnJBtbi6So.OoG4pYehLBKYaBG', 'EMPLOYER'),
                                                  ('Job Seeker One', 'jobseeker1@etu.nz', '$2a$10$yiUGnzZpkNeXP4KFHttijeg7v3jhnJBtbi6So.OoG4pYehLBKYaBG', 'JOB_SEEKER'),
                                                  ('Job Seeker Two', 'jobseeker2@etu.nz', '$2a$10$yiUGnzZpkNeXP4KFHttijeg7v3jhnJBtbi6So.OoG4pYehLBKYaBG', 'JOB_SEEKER');

-- Initial job data
INSERT INTO jobs (title, description, employer_id, status, post_date, deadline_date) VALUES
                                                                                       ('Senior Java Developer', 'We are looking for an experienced Java engineer to join our team.\n\nRequirements:\n- At least 3 years of Java development experience\n- Proficient in Spring Boot framework\n- Good teamwork abilities\n\nSalary range: $90,000 - $120,000',
                                                                                        2, 'ACTIVE', CURRENT_DATE() - 5, CURRENT_DATE() + 25),

                                                                                       ('Web Frontend Developer', 'Looking for a skilled frontend developer to improve our user interface.\n\nRequirements:\n- Proficient in HTML, CSS, JavaScript\n- Familiar with React or Vue framework\n- Experience with responsive design\n\nSalary range: $75,000 - $95,000',
                                                                                        2, 'ACTIVE', CURRENT_DATE() - 3, CURRENT_DATE() + 27),

                                                                                       ('DevOps Engineer', 'Help us build and maintain cloud infrastructure.\n\nRequirements:\n- Familiar with AWS or Azure\n- Experience with containerization (Docker, Kubernetes)\n- Experience implementing CI/CD processes\n\nSalary range: $85,000 - $115,000',
                                                                                        2, 'CLOSED', CURRENT_DATE() - 30, CURRENT_DATE() - 5);

-- Initial application data
INSERT INTO applications (job_id, applicant_id, status, application_date, cover_letter) VALUES
                                                                                          (1, 3, 'PENDING', CURRENT_DATE() - 2, 'Dear Hiring Manager,\n\nI am very interested in the Senior Java Developer position at your company. I have 5 years of Java development experience, am proficient in Spring Boot framework, and have participated in the development of multiple large-scale projects.\n\nLooking forward to your reply!\n\nJob Seeker One'),
                                                                                          (2, 3, 'ACCEPTED', CURRENT_DATE() - 1, 'Dear Hiring Manager,\n\nI am a frontend development expert with extensive experience in React projects. I believe my skills will bring value to your company.\n\nLooking forward to discussing this opportunity with you!\n\nJob Seeker One'),
                                                                                          (1, 4, 'REJECTED', CURRENT_DATE() - 3, 'Dear Hiring Manager,\n\nI am a Java developer hoping to join your team. Although my experience is limited, I have a strong ability to learn and am willing to take on challenges.\n\nThank you!\n\nJob Seeker Two');
