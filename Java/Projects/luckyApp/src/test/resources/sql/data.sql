-- Unique Codes (Keep IDs for consistency)
INSERT INTO unique_code (id, code) VALUES
                                       (5, 'testcode123'),
                                       (6, 'testcode456'),
                                       (7, 'newcode789'),
                                       (8, 'grgrgreggre'),
                                       (9, 'unassigned123'),
                                       (10, 'filter-code-1'),
                                       (11, 'filter-code-2'),
                                       (12, 'filter-code-invisible'),
                                       (13, 'filter-code-nogdpr');

-- Contract Types
INSERT INTO contract_types (id, hpp, dpp_dpc, ico) VALUES
                                                       (5, true, true, true),
                                                       (6, false, false, false),
                                                       (7, true, false, false),
                                                       (10, false, false, true),
                                                       (11, true, false, false),
                                                       (12, true, false, false),
                                                       (13, false, true, false);

-- Employment Types
INSERT INTO employment_types (id, full_time, half_time, part_time, part_time_hours) VALUES
                                                                                        (5, true, true, true, '20'),
                                                                                        (6, false, false, false, NULL),
                                                                                        (7, true, false, false, NULL),
                                                                                        (10, true, false, false, NULL),
                                                                                        (11, false, true, true, '15'),
                                                                                        (12, true, false, false, NULL),
                                                                                        (13, true, false, false, NULL);

-- Employment Regimes
INSERT INTO employment_regimes (id, on_site, hybrid, remote) VALUES
                                                                 (5, true, true, true),
                                                                 (6, false, false, false),
                                                                 (7, true, false, true),
                                                                 (10, false, false, true),
                                                                 (11, false, true, false),
                                                                 (12, true, false, false),
                                                                 (13, false, false, true);

-- Preferred Roles
INSERT INTO preferred_roles (
    id,
    tester, it_support, automation_tester,
    test_analyst, test_manager, frontend_developer,
    backend_developer, data_analyst, scrum_master,
    product_owner
) VALUES
      (5,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true),
      (6,  false, false, false, false, false, false, false, false, false, false),
      (7,  true,  false, false, false, false, false, false, false, false, false),
      (10, false, false, false, false, false, false, true,  false, false, false),
      (11, true,  false, true,  false, false, false, false, false, false, false),
      (12, false, false, false, false, false, true,  false, false, false, false),
      (13, false, false, false, false, false, false, false, true,  false, false);

-- Work Regions
INSERT INTO work_regions (
    id,
    praha, stredocesky_kraj, jihocesky_kraj,
    plzensky_kraj, karlovarsky_kraj, ustecky_kraj,
    liberecky_kraj, kralovehradecky_kraj, pardubicky_kraj,
    kraj_vysocina, jihomoravsky_kraj, zlinsky_kraj,
    olomoucky_kraj, moravskoslezsky_kraj
) VALUES
      (5,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true),
      (6,  false, false, false, false, false, false, false, false, false, false, false, false, false, false),
      (7,  false, false, false, false, false, false, false, false, false, true,  false, false, false, false),
      (10, false, false, false, false, false, false, false, false, false, false, true,  false, false, false),
      (11, true,  true,  false, false, false, false, false, false, false, false, false, false, false, false),
      (12, false, false, false, false, false, false, false, false, false, false, false, false, false, true),
      (13, false, false, false, false, false, false, false, false, false, true,  false, false, false, false);

-- CV Data
INSERT INTO cv_data (id, data, file_name, content_type) VALUES
                                                            (5, X'deadbeef', 'cv1.pdf', 'application/pdf'),
                                                            (6, X'deadbeef', 'cv2.pdf', 'application/pdf'),
                                                            (7, X'deadbeef', 'cv3.pdf', 'application/pdf'),
                                                            (10, X'deadbeef', 'cv4.pdf', 'application/pdf'),
                                                            (11, X'deadbeef', 'cv5.pdf', 'application/pdf'),
                                                            (12, X'deadbeef', 'cv6.pdf', 'application/pdf'),
                                                            (13, X'deadbeef', 'cv7.pdf', 'application/pdf');

-- Graduate
INSERT INTO graduate (
    id,
    unique_code_id,
    created,
    modified,
    role,
    first_name,
    last_name,
    languages,
    availability,
    lucky_course,
    motivation_letter,
    gdpr_consent,
    profile_visible,
    contract_types_id,
    employment_types_id,
    employment_regimes_id,
    preferred_roles_id,
    work_regions_id,
    cv_data_id
) VALUES
      (5,  5,  '2024-01-10 10:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'Test',     'UserOne',      'Czech, English',   'IMMEDIATELY', 'COMPLETE_TESTER_PREPARATION', 'Motivation for User One',       true,  true,  5,  5,  5,  5,  5,  5),
      (6,  6,  '2024-01-11 11:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'Test',     'UserTwo',      'Slovak',           'ONE_MONTH',   'MANUAL_IT_TESTER_ACADEMY',    'Motivation for User Two',       true,  false, 6,  6,  6,  6,  6,  6),
      (7,  7,  '2024-01-12 12:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'NonRemote','User',         'German',           'TWO_MONTHS',  'MANUAL_IT_TESTER_ACADEMY',    'Motivation for Non-Remote',     true,  true,  7,  7,  7,  7,  7,  7),
      (10, 10, '2024-01-13 13:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'Backend',  'RemoteDev',    'English',          'IMMEDIATELY', 'COMPLETE_TESTER_PREPARATION', 'Wants to code backend remotely in Brno', true, true,  10, 10, 10, 10, 10, 10),
      (11, 11, '2024-01-14 14:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'Tester',   'HybridMulti',  'Czech',            'ONE_MONTH',   'COMPLETE_TESTER_PREPARATION', 'Likes testing, hybrid, near Prague', true, true,  11, 11, 11, 11, 11, 11),
      (12, 12, '2024-01-15 15:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'Frontend', 'Invisible',    'Polish',           'IMMEDIATELY', 'MANUAL_IT_TESTER_ACADEMY',    'Invisible profile motivation',  true,  false, 12, 12, 12, 12, 12, 12),
      (13, 13, '2024-01-16 16:00:00', CURRENT_TIMESTAMP, 'GRADUATE', 'Data',     'NoGdpr',       'English',          'THREE_MONTHS','COMPLETE_TESTER_PREPARATION', 'Motivation without GDPR consent', false, true,  13, 13, 13, 13, 13, 13);
