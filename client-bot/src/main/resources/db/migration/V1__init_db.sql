CREATE TABLE usr (
    id bigserial primary key,
    name varchar(128) not null,
    representation_name varchar(128) not null,
    telegram_nick varchar(128) not null,
    telegram_id bigserial not null,
    notified boolean not null default false
);

CREATE TABLE appointment (
    id bigserial primary key,
    user_id bigserial references usr(id),
    appointment_date timestamp with time zone,
    create_timestamp timestamp with time zone
);

CREATE TABLE city (
    id bigserial primary key,
    city varchar(128) not null,
    timezone_offset integer not null
);

CREATE TABLE work_time (
    id bigserial primary key,
    start_work_time timestamp with time zone,
    end_work_time timestamp with time zone,
    is_free boolean not null default false
);

INSERT INTO city VALUES (1, 'Калининград', 2);
INSERT INTO city VALUES (2, 'Москва', 3);
INSERT INTO city VALUES (3, 'Санкт-Петербург', 3);
INSERT INTO city VALUES (4, 'Нижний Новгород', 3);
INSERT INTO city VALUES (5, 'Казань', 3);
INSERT INTO city VALUES (6, 'Ростов-на-Дону', 3);
INSERT INTO city VALUES (7, 'Воронеж', 3);
INSERT INTO city VALUES (8, 'Краснодар', 3);
INSERT INTO city VALUES (9, 'Саратов', 3);
INSERT INTO city VALUES (10, 'Ярославль', 3);
INSERT INTO city VALUES (11, 'Махачкала', 3);
INSERT INTO city VALUES (12, 'Рязань', 3);
INSERT INTO city VALUES (13, 'Набережные Челны', 3);
INSERT INTO city VALUES (14, 'Пенза', 3);
INSERT INTO city VALUES (15, 'Липецк', 3);
INSERT INTO city VALUES (16, 'Киров', 3);
INSERT INTO city VALUES (17, 'Чебоксары', 3);
INSERT INTO city VALUES (18, 'Тула', 3);
INSERT INTO city VALUES (19, 'Курск', 3);
INSERT INTO city VALUES (20, 'Ставрополь', 3);
INSERT INTO city VALUES (21, 'Сочи', 3);
INSERT INTO city VALUES (22, 'Тверь', 3);
INSERT INTO city VALUES (23, 'Иваново', 3);
INSERT INTO city VALUES (24, 'Брянск', 3);
INSERT INTO city VALUES (25, 'Белгород', 3);
INSERT INTO city VALUES (26, 'Владимир', 3);
INSERT INTO city VALUES (27, 'Архангельск', 3);
INSERT INTO city VALUES (28, 'Калуга', 3);
INSERT INTO city VALUES (29, 'Смоленск', 3);
INSERT INTO city VALUES (30, 'Череповец', 3);
INSERT INTO city VALUES (31, 'Саранск', 3);
INSERT INTO city VALUES (32, 'Орёл', 3);
INSERT INTO city VALUES (33, 'Вологда', 3);
INSERT INTO city VALUES (34, 'Владикавказ', 3);
INSERT INTO city VALUES (35, 'Грозный', 3);
INSERT INTO city VALUES (36, 'Мурманск', 3);
INSERT INTO city VALUES (37, 'Тамбов', 3);
INSERT INTO city VALUES (38, 'Петрозаводск', 3);
INSERT INTO city VALUES (39, 'Кострома', 3);
INSERT INTO city VALUES (40, 'Новороссийск', 3);
INSERT INTO city VALUES (41, 'Йошкар-Ола', 3);
INSERT INTO city VALUES (42, 'Таганрог', 3);
INSERT INTO city VALUES (43, 'Сыктывкар', 3);
INSERT INTO city VALUES (44, 'Нальчик', 3);
INSERT INTO city VALUES (45, 'Нижнекамск', 3);
INSERT INTO city VALUES (46, 'Шахты', 3);
INSERT INTO city VALUES (47, 'Дзержинск', 3);
INSERT INTO city VALUES (48, 'Старый Оскол', 3);
INSERT INTO city VALUES (49, 'Великий Новгород', 3);
INSERT INTO city VALUES (50, 'Псков', 3);
INSERT INTO city VALUES (51, 'Минеральные Воды', 3);
INSERT INTO city VALUES (52, 'Севастополь', 3);
INSERT INTO city VALUES (53, 'Симферопль', 3);
INSERT INTO city VALUES (54, 'Самара', 4);
INSERT INTO city VALUES (55, 'Волгоград', 4);
INSERT INTO city VALUES (56, 'Тольятти', 4);
INSERT INTO city VALUES (57, 'Ижевск', 4);
INSERT INTO city VALUES (58, 'Ульяновск', 4);
INSERT INTO city VALUES (59, 'Астрахань', 4);
INSERT INTO city VALUES (60, 'Екатеринбург', 5);
INSERT INTO city VALUES (61, 'Челябинск', 5);
INSERT INTO city VALUES (62, 'Уфа', 5);
INSERT INTO city VALUES (63, 'Пермь', 5);
INSERT INTO city VALUES (64, 'Тюмень', 5);
INSERT INTO city VALUES (65, 'Оренбург', 5);
INSERT INTO city VALUES (66, 'Магнитогорск', 5);
INSERT INTO city VALUES (67, 'Сургут', 5);
INSERT INTO city VALUES (68, 'Нижний Тагил', 5);
INSERT INTO city VALUES (69, 'Курган', 5);
INSERT INTO city VALUES (70, 'Стерлитамак', 5);
INSERT INTO city VALUES (71, 'Нижневартовск', 5);
INSERT INTO city VALUES (72, 'Орск', 5);
INSERT INTO city VALUES (73, 'Омск', 6);
INSERT INTO city VALUES (74, 'Новосибирск', 7);
INSERT INTO city VALUES (75, 'Красноярск', 7);
INSERT INTO city VALUES (76, 'Барнаул', 7);
INSERT INTO city VALUES (77, 'Томск', 7);
INSERT INTO city VALUES (78, 'Кемерово', 7);
INSERT INTO city VALUES (79, 'Новокузнецк', 7);
INSERT INTO city VALUES (80, 'Бийск', 7);
INSERT INTO city VALUES (81, 'Прокопьевск', 7);
INSERT INTO city VALUES (82, 'Норильск', 7);
INSERT INTO city VALUES (83, 'Иркутск', 8);
INSERT INTO city VALUES (84, 'Улан-Удэ', 8);
INSERT INTO city VALUES (85, 'Братск', 8);
INSERT INTO city VALUES (86, 'Ангарск', 8);
INSERT INTO city VALUES (87, 'Чита', 9);
INSERT INTO city VALUES (88, 'Якутск', 9);
INSERT INTO city VALUES (89, 'Благовещенск', 9);
INSERT INTO city VALUES (90, 'Хабаровск', 10);
INSERT INTO city VALUES (91, 'Владивосток', 10);
INSERT INTO city VALUES (92, 'Комсомольск-на-Амуре', 10);