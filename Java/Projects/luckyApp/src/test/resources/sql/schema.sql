-- Drop tables if they exist to ensure clean state
DROP TABLE IF EXISTS graduate CASCADE;
DROP TABLE IF EXISTS unique_code CASCADE;
DROP TABLE IF EXISTS contract_types CASCADE;
DROP TABLE IF EXISTS employment_types CASCADE;
DROP TABLE IF EXISTS employment_regimes CASCADE;
DROP TABLE IF EXISTS preferred_roles CASCADE;
DROP TABLE IF EXISTS work_regions CASCADE;
DROP TABLE IF EXISTS cv_data CASCADE; -- Drop cv_data as well

-- Create table: unique_code
CREATE TABLE unique_code (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             code VARCHAR(255) UNIQUE NOT NULL
);

-- Create table: contract_types
CREATE TABLE contract_types (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                hpp BOOLEAN DEFAULT FALSE,
                                dpp_dpc BOOLEAN DEFAULT FALSE,
                                ico BOOLEAN DEFAULT FALSE
);

-- Create table: employment_types
CREATE TABLE employment_types (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  full_time BOOLEAN DEFAULT FALSE,
                                  half_time BOOLEAN DEFAULT FALSE,
                                  part_time BOOLEAN DEFAULT FALSE,
                                  part_time_hours VARCHAR(50) -- Changed to VARCHAR to match DTO/Entity if needed, or keep INT if always number
);

-- Create table: employment_regimes
CREATE TABLE employment_regimes (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    on_site BOOLEAN DEFAULT FALSE,
                                    hybrid BOOLEAN DEFAULT FALSE,
                                    remote BOOLEAN DEFAULT FALSE
);

-- Create table: preferred_roles
CREATE TABLE preferred_roles (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 tester BOOLEAN DEFAULT FALSE,
                                 it_support BOOLEAN DEFAULT FALSE,
                                 automation_tester BOOLEAN DEFAULT FALSE,
                                 test_analyst BOOLEAN DEFAULT FALSE,
                                 test_manager BOOLEAN DEFAULT FALSE,
                                 frontend_developer BOOLEAN DEFAULT FALSE,
                                 backend_developer BOOLEAN DEFAULT FALSE,
                                 data_analyst BOOLEAN DEFAULT FALSE,
                                 scrum_master BOOLEAN DEFAULT FALSE,
                                 product_owner BOOLEAN DEFAULT FALSE
);

-- Create table: work_regions
CREATE TABLE work_regions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              praha BOOLEAN DEFAULT FALSE,
                              stredocesky_kraj BOOLEAN DEFAULT FALSE,
                              jihocesky_kraj BOOLEAN DEFAULT FALSE,
                              plzensky_kraj BOOLEAN DEFAULT FALSE,
                              karlovarsky_kraj BOOLEAN DEFAULT FALSE,
                              ustecky_kraj BOOLEAN DEFAULT FALSE,
                              liberecky_kraj BOOLEAN DEFAULT FALSE,
                              kralovehradecky_kraj BOOLEAN DEFAULT FALSE,
                              pardubicky_kraj BOOLEAN DEFAULT FALSE,
                              kraj_vysocina BOOLEAN DEFAULT FALSE,
                              jihomoravsky_kraj BOOLEAN DEFAULT FALSE,
                              zlinsky_kraj BOOLEAN DEFAULT FALSE,
                              olomoucky_kraj BOOLEAN DEFAULT FALSE,
                              moravskoslezsky_kraj BOOLEAN DEFAULT FALSE
);

-- Create table: cv_data (NEW)
CREATE TABLE cv_data (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         data BLOB,
                         file_name VARCHAR(255),
                         content_type VARCHAR(100)
);


-- Create table: graduate (MODIFIED)
CREATE TABLE graduate (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          unique_code_id BIGINT UNIQUE,
                          created TIMESTAMP,
                          modified TIMESTAMP,
                          role VARCHAR(50),
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          languages VARCHAR(150) NOT NULL,
                          availability VARCHAR(50) NOT NULL,
                          lucky_course VARCHAR(50),
                          motivation_letter VARCHAR(1000),
                          gdpr_consent BOOLEAN NOT NULL,
                          other_courses VARCHAR(1000),
                          profile_visible BOOLEAN DEFAULT TRUE,
                          contract_types_id BIGINT,
                          employment_types_id BIGINT,
                          employment_regimes_id BIGINT,
                          preferred_roles_id BIGINT,
                          work_regions_id BIGINT,
                          cv_data_id BIGINT UNIQUE, -- Added CV data foreign key (UNIQUE ensures OneToOne)

                          FOREIGN KEY (unique_code_id) REFERENCES unique_code(id) ON DELETE SET NULL,
                          FOREIGN KEY (contract_types_id) REFERENCES contract_types(id) ON DELETE CASCADE,
                          FOREIGN KEY (employment_types_id) REFERENCES employment_types(id) ON DELETE CASCADE,
                          FOREIGN KEY (employment_regimes_id) REFERENCES employment_regimes(id) ON DELETE CASCADE,
                          FOREIGN KEY (preferred_roles_id) REFERENCES preferred_roles(id) ON DELETE CASCADE,
                          FOREIGN KEY (work_regions_id) REFERENCES work_regions(id) ON DELETE CASCADE,
                          FOREIGN KEY (cv_data_id) REFERENCES cv_data(id) ON DELETE CASCADE -- Added foreign key constraint
);