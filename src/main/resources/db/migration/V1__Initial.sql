-- ----------------------------
-- initial tables
-- ----------------------------


CREATE TABLE IF NOT EXISTS courses
(
    uid  uuid        not null,
    code varchar(10) not null,
    name varchar DEFAULT ''
);
comment on table courses is 'academic courses';

create unique index courses_code_uindex
    on courses (code);

alter table courses
    add constraint courses_pk
        primary key (uid);


CREATE TABLE IF NOT EXISTS students
(
    uid        uuid        not null,
    code       varchar(10) not null,
    first_name varchar DEFAULT '',
    last_name  varchar DEFAULT ''
);
comment on table students is 'college students';

create unique index students_code_uindex
    on students (code);
alter table students
    add constraint students_pk
        primary key (uid);


CREATE TABLE IF NOT EXISTS professors
(
    uid        uuid        not null,
    code       varchar(10) not null,
    first_name varchar DEFAULT '',
    last_name  varchar DEFAULT ''
);
comment on table professors is 'college professors';

create unique index professors_code_uindex
    on professors (code);
alter table professors
    add constraint professors_pk
        primary key (uid);


CREATE TABLE IF NOT EXISTS courses_students
(
    course_uid  uuid not null REFERENCES courses (uid) ON UPDATE CASCADE ON DELETE CASCADE,
    student_uid uuid not null REFERENCES students (uid) ON UPDATE CASCADE,
    constraint courses_students_pk
        primary key (course_uid, student_uid)
);
comment on table courses_students is 'm2m relationship between courses and students';

CREATE TABLE IF NOT EXISTS courses_professors
(
    course_uid    uuid not null REFERENCES courses (uid) ON UPDATE CASCADE ON DELETE CASCADE,
    professor_uid uuid not null REFERENCES professors (uid) ON UPDATE CASCADE,
    constraint courses_professors_pk
        primary key (course_uid, professor_uid)
);
comment on table courses_students is 'm2m relationship between courses and professors';