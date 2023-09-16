create table provider(
    provider_code varchar(255) primary key,
    name varchar(255),
    status int,
    city varchar(255)
);

create table detail(
    detail_code varchar(255) primary key ,
    name varchar(255),
    color varchar(255),
    size int,
    city varchar(255)
);

create table project(
    project_code varchar(255) primary key ,
    name varchar(255),
    city varchar(255)
);

create table number_of_details(
    provider_code varchar(255) references provider(provider_code),
    detail_code varchar(255) references detail(detail_code),
    project_code varchar(255) references project(project_code),
    number int
);

insert into provider (provider_code, name, status, city)
VALUES ('P1', 'Petrov', 20, 'Moscow'),
       ('P2', 'Sinicin', 10, 'Tallin'),
       ('P3', 'Federov', 30, 'Tallin'),
       ('P4', 'Chaianov', 20, 'Minsk'),
       ('P5', 'Krykov', 30, 'Kiev');

insert into detail (detail_code, name, color, size, city)
VALUES ('D1', 'Bolt', 'Red', 12, 'Moscow'),
       ('D2', 'Gaika', 'Green', 17, 'Minsk'),
       ('D3', 'Disk', 'Black', 17, 'Vilnus'),
       ('D4', 'Disk', 'Black', 14, 'Moscow'),
       ('D5', 'Korpus', 'Red', 12, 'Minsk'),
       ('D6', 'Krishki', 'Red', 19, 'Moscow');

insert into project (project_code, name, city)
VALUES ('PR1', 'IPR1', 'Minsk'),
       ('PR2', 'IPR2', 'Tallin'),
       ('PR3', 'IPR3', 'Pskov'),
       ('PR4', 'IPR4', 'Pskov'),
       ('PR5', 'IPR4', 'Moscow'),
       ('PR6', 'IPR6', 'Saratov'),
       ('PR7', 'IPR7', 'Moscow');

insert into number_of_details (provider_code, detail_code, project_code, number)
VALUES ('P1', 'D1', 'PR1', 200),
       ('P1', 'D1', 'PR2', 700),
       ('P2', 'D3', 'PR1', 400),
       ('P2', 'D2', 'PR2', 200),
       ('P2', 'D3', 'PR3', 200),
       ('P2', 'D3', 'PR4', 500),
       ('P2', 'D3', 'PR5', 600),
       ('P2', 'D3', 'PR6', 400),
       ('P2', 'D3', 'PR7', 800),
       ('P2', 'D5', 'PR2', 100),
       ('P3', 'D3', 'PR1', 200),
       ('P3', 'D4', 'PR2', 500),
       ('P4', 'D6', 'PR3', 300),
       ('P4', 'D6', 'PR7', 300),
       ('P5', 'D2', 'PR2', 200),
       ('P5', 'D2', 'PR4', 100),
       ('P5', 'D5', 'PR5', 500),
       ('P5', 'D5', 'PR7', 100),
       ('P5', 'D6', 'PR2', 200),
       ('P5', 'D1', 'PR2', 100),
       ('P5', 'D3', 'PR4', 200),
       ('P5', 'D4', 'PR4', 800),
       ('P5', 'D5', 'PR4', 400),
       ('P5', 'D6', 'PR4', 500);

-- 21.	Получить номера деталей, поставляемых для какого-либо проекта в Лондоне. --

SELECT d.detail_code FROM detail d
JOIN number_of_details nod ON nod.detail_code = d.detail_code
JOIN project p ON nod.project_code = p.project_code
WHERE p.city = 'London';

-- 22.	Получить номера проектов, использующих по крайней мере одну деталь, имеющуюся у поставщика П1. --

SELECT p.project_code FROM project p
JOIN number_of_details nod ON p.project_code = nod.project_code
JOIN provider pr ON pr.provider_code = nod.provider_code
WHERE pr.provider_code = 'P1';

-- 30.	Получить номера деталей, поставляемых для лондонских проектов. --

SELECT nod.detail_code FROM number_of_details nod
JOIN project p ON p.project_code = nod.project_code
WHERE p.city = 'London';

-- 3.	Получить номера поставщиков, которые обеспечивают проект ПР1.

SELECT provider_code FROM number_of_details
WHERE project_code = 'PR1';

-- 8. Получить все такие тройки "номера поставщиков-номера деталей-номера проектов",
-- для которых никакие из двух выводимых поставщиков, деталей и проектов не размещены в одном городе.

SELECT DISTINCT nod.provider_code, nod.detail_code, nod.project_code FROM number_of_details nod
JOIN provider p ON nod.provider_code = p.provider_code
JOIN detail d ON d.detail_code = nod.detail_code
JOIN project pr ON pr.project_code = nod.project_code
WHERE NOT (p.city = d.city OR p.city = pr.city OR d.city = pr.city);

-- 12.	Получить номера деталей, поставляемых для всех проектов, обеспечиваемых поставщиком 
-- из того же города, где размещен проект.

SELECT nod.detail_code FROM number_of_details nod
JOIN provider prov ON prov.provider_code = nod.provider_code
JOIN project proj ON proj.project_code = nod.project_code
WHERE prov.city = proj.city;

-- 16.	Получить общее количество деталей Д1, поставляемых поставщиком П1.

SELECT count(*) FROM number_of_details
WHERE detail_code = 'D1' AND provider_code = 'P1';

-- 32.	Получить номера проектов, обеспечиваемых по крайней мере всеми деталями поставщика П1.

SELECT proj.project_code FROM project proj
WHERE NOT EXISTS (
	SELECT d.detail_code FROM detail d
	WHERE d.detail_code NOT IN (
		SELECT nod.detail_code FROM number_of_details nod
		WHERE nod.provider_code = 'P1'
	)
)

-- 26.	Получить номера проектов, для которых среднее количество поставляемых деталей Д1 больше, 
-- чем наибольшее количество любых деталей, поставляемых для проекта ПР1.

SELECT proj.project_code FROM project proj
WHERE 
(
	SELECT AVG(nod.number) FROM number_of_details nod
	WHERE nod.detail_code = 'D1' AND nod.project_code = proj.project_code
) > (
	SELECT MAX(nod.number) FROM number_of_details nod
	WHERE nod.project_code = 'PR1'
);

-- 17.	Для каждой детали, поставляемой для проекта, получить номер детали, номер проекта и соответствующее общее количество.

select detail_code, project_code, number from number_of_details;

