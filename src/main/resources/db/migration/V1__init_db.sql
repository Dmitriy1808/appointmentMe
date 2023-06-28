CREATE TABLE usr (
    id bigserial primary key,
    name varchar(128) not null,
    telegram_nick varchar(128) not null,
    telegram_id bigserial not null
);

CREATE TABLE appointment (
    id bigserial primary key,
    user_id bigserial references usr(id),
    client_name varchar(128) not null,
    appoint_date timestamp with time zone,
    draft boolean,
    create_timestamp timestamp with time zone
);

CREATE TABLE cities (
    id bigserial primary key,
    city varchar(128) not null,
    timezoneOffset integer not null,
);

INSERT INTO cities VALUES (1, 'Калининград', 2);
INSERT INTO cities VALUES (2, 'Москва', 3);
INSERT INTO cities VALUES (3, 'Санкт-Петербург', 3);
INSERT INTO cities VALUES (4, 'Нижний Новгород', 3);
INSERT INTO cities VALUES (5, 'Казань', 3);
INSERT INTO cities VALUES (6, 'Ростов-на-Дону', 3);
INSERT INTO cities VALUES (7, 'Воронеж', 3);
INSERT INTO cities VALUES (8, 'Краснодар', 3);
INSERT INTO cities VALUES (9, 'Саратов', 3);
INSERT INTO cities VALUES (10, 'Ярославль', 3);
INSERT INTO cities VALUES (11, 'Махачкала', 3);
INSERT INTO cities VALUES (12, 'Рязань', 3);
INSERT INTO cities VALUES (13, 'Набережные Челны', 3);
INSERT INTO cities VALUES (14, 'Пенза', 3);
INSERT INTO cities VALUES (15, 'Липецк', 3);
INSERT INTO cities VALUES (16, 'Киров', 3);
INSERT INTO cities VALUES (17, 'Чебоксары', 3);
INSERT INTO cities VALUES (18, 'Тула', 3);
INSERT INTO cities VALUES (19, 'Курск', 3);
INSERT INTO cities VALUES (20, 'Ставрополь', 3);
INSERT INTO cities VALUES (21, 'Сочи', 3);
INSERT INTO cities VALUES (22, 'Тверь', 3);
INSERT INTO cities VALUES (23, 'Иваново', 3);
INSERT INTO cities VALUES (24, 'Брянск', 3);
INSERT INTO cities VALUES (25, 'Белгород', 3);
INSERT INTO cities VALUES (26, 'Владимир', 3);
INSERT INTO cities VALUES (27, 'Архангельск', 3);
INSERT INTO cities VALUES (28, 'Калуга', 3);
INSERT INTO cities VALUES (29, 'Смоленск', 3);
INSERT INTO cities VALUES (30, 'Череповец', 3);
INSERT INTO cities VALUES (31, 'Саранск', 3);
INSERT INTO cities VALUES (32, 'Орёл', 3);
INSERT INTO cities VALUES (33, 'Вологда', 3);
INSERT INTO cities VALUES (34, 'Владикавказ', 3);
INSERT INTO cities VALUES (35, 'Грозный', 3);
INSERT INTO cities VALUES (36, 'Мурманск', 3);
INSERT INTO cities VALUES (37, 'Тамбов', 3);
INSERT INTO cities VALUES (38, 'Петрозаводск', 3);
INSERT INTO cities VALUES (39, 'Кострома', 3);
INSERT INTO cities VALUES (40, 'Новороссийск', 3);
INSERT INTO cities VALUES (41, 'Йошкар-Ола', 3);
INSERT INTO cities VALUES (42, 'Таганрог', 3);
INSERT INTO cities VALUES (43, 'Сыктывкар', 3);
INSERT INTO cities VALUES (44, 'Нальчик', 3);
INSERT INTO cities VALUES (45, 'Нижнекамск', 3);
INSERT INTO cities VALUES (46, 'Шахты', 3);
INSERT INTO cities VALUES (47, 'Дзержинск', 3);
INSERT INTO cities VALUES (48, 'Старый Оскол', 3);
INSERT INTO cities VALUES (49, 'Великий Новгород', 3);
INSERT INTO cities VALUES (50, 'Псков', 3);
INSERT INTO cities VALUES (51, 'Минеральные Воды', 3);
INSERT INTO cities VALUES (52, 'Севастополь', 3);
INSERT INTO cities VALUES (53, 'Симферопль', 3);
INSERT INTO cities VALUES (54, 'Самара', 4);
INSERT INTO cities VALUES (55, 'Волгоград', 4);
INSERT INTO cities VALUES (56, 'Тольятти', 4);
INSERT INTO cities VALUES (57, 'Ижевск', 4);
INSERT INTO cities VALUES (58, 'Ульяновск', 4);
INSERT INTO cities VALUES (59, 'Астрахань', 4);
INSERT INTO cities VALUES (60, 'Екатеринбург', 5);
INSERT INTO cities VALUES (61, 'Челябинск', 5);
INSERT INTO cities VALUES (62, 'Уфа', 5);
INSERT INTO cities VALUES (63, 'Пермь', 5);
INSERT INTO cities VALUES (64, 'Тюмень', 5);
INSERT INTO cities VALUES (65, 'Оренбург', 5);
INSERT INTO cities VALUES (66, 'Магнитогорск', 5);
INSERT INTO cities VALUES (67, 'Сургут', 5);
INSERT INTO cities VALUES (68, 'Нижний Тагил', 5);
INSERT INTO cities VALUES (69, 'Курган', 5);
INSERT INTO cities VALUES (70, 'Стерлитамак', 5);
INSERT INTO cities VALUES (71, 'Нижневартовск', 5);
INSERT INTO cities VALUES (72, 'Орск', 5);
INSERT INTO cities VALUES (73, 'Омск', 6);
INSERT INTO cities VALUES (74, 'Новосибирск', 7);
INSERT INTO cities VALUES (75, 'Красноярск', 7);
INSERT INTO cities VALUES (76, 'Барнаул', 7);
INSERT INTO cities VALUES (77, 'Томск', 7);
INSERT INTO cities VALUES (78, 'Кемерово', 7);
INSERT INTO cities VALUES (79, 'Новокузнецк', 7);
INSERT INTO cities VALUES (80, 'Бийск', 7);
INSERT INTO cities VALUES (81, 'Прокопьевск', 7);
INSERT INTO cities VALUES (82, 'Норильск', 7);
INSERT INTO cities VALUES (83, 'Иркутск', 8);
INSERT INTO cities VALUES (84, 'Улан-Удэ', 8);
INSERT INTO cities VALUES (85, 'Братск', 8);
INSERT INTO cities VALUES (86, 'Ангарск', 8);
INSERT INTO cities VALUES (87, 'Чита', 9);
INSERT INTO cities VALUES (88, 'Якутск', 9);
INSERT INTO cities VALUES (89, 'Благовещенск', 9);
INSERT INTO cities VALUES (90, 'Хабаровск', 10);
INSERT INTO cities VALUES (91, 'Владивосток', 10);
INSERT INTO cities VALUES (92, 'Комсомольск-на-Амуре', 10);