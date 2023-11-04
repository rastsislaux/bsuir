create table teacher
(
    code_number varchar(255),
    surname varchar(255),
    position varchar(255),
    department varchar(255),
    speciality varchar(255),
    phone varchar(255)
);

create table subject
(
    code_number varchar(255),
    name varchar(255),
    hours integer,
    speciality varchar(255),
    semester integer
);

create table student_group
(
    code_number varchar(255),
    name varchar(255),
    student_count integer,
    speciality varchar(255),
    headmen_surname varchar(255)
);

create table teacher_teaches_in_group
(
    group_code varchar(255),
    subject_code varchar(255),
    teacher_code varchar(255),
    room_number integer
);

insert into teacher(code_number, surname, position, department, speciality, phone)
values ('221L', 'Frolov', 'Docent', 'EVM', 'ASOI, EVM', '487'),
       ('222L', 'Kostin', 'Docent', 'EVM', 'EVM', '543'),
       ('225L', 'Boyko', 'Professor', 'ASU', 'ASOI, EVM', '112'),
       ('430L', 'Glazov', 'Assistant', 'TF', 'SD', '421'),
       ('110L', 'Petrov', 'Assistant', 'Economics', 'Int. Economics', '324');

insert into subject(code_number, name, hours, speciality, semester)
values ('12P', 'MiniEVM', 36, 'EVM', 1),
       ('14P', 'PEVM', 72, 'EVM', 2),
       ('17P', 'SUBD PK', 48, 'ASOI', 4),
       ('18P', 'VKSS', 52, 'ASOI', 6),
       ('34P', 'Physics', 30, 'SD', 6),
       ('22P', 'Audit', 24, 'Buchuchet', 3);

insert into student_group(code_number, name, student_count, speciality, headmen_surname)
values ('8G', 'E-12', 18, 'EVM', 'Ivanova'),
       ('7G', 'E-15', 22, 'EVM', 'Setkin'),
       ('4G', 'AS-9', 24, 'ASOI', 'Balabanov'),
       ('3G', 'AS-8', 20, 'ASOI', 'Chizhov'),
       ('17G', 'C-14', 29, 'SD', 'Amrosov'),
       ('12G', 'M-6', 16, 'Int. Economics', 'Trubin'),
       ('10G', 'B-4', 21, 'Buchuchet', 'Zyzazutkin');

insert into teacher_teaches_in_group(group_code, subject_code, teacher_code, room_number)
values ('8G', '12P', '222L', 112),
       ('8G', '14P', '221L', 220),
       ('8G', '17P', '222L', 112),
       ('7G', '14P', '221L', 220),
       ('7G', '17P', '222L', 241),
       ('7G', '18P', '225L', 210),
       ('4G', '12P', '222L', 112),
       ('4G', '18P', '225L', 210),
       ('3G', '12P', '222L', 112),
       ('3G', '17P', '221L', 241),
       ('3G', '18P', '225L', 210),
       ('17G', '12P', '222L', 112),
       ('17G', '22P', '110L', 241),
       ('17G', '34P', '430L', 210),
       ('12G', '12P', '222L', 112),
       ('12G', '22P', '110L', 220),
       ('10G', '12P', '222L', 118),
       ('10G', '22P', '110L', 112);

-- 12 --

SELECT DISTINCT t.surname FROM teacher_teaches_in_group ttig
JOIN student_group sg ON ttig.group_code = sg.code_number
JOIN teacher t ON ttig.teacher_code = t.code_number
WHERE sg.speciality = 'EVM';
