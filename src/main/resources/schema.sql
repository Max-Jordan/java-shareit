CREATE TYPE IF NOT EXISTS STATUS AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS USERS
(
    user_id BIGINT generated always as identity,
    name    CHARACTER VARYING(50)  not null,
    email   CHARACTER VARYING(200) not null,
    constraint USERS_pk
        primary key (user_id)
);

create unique index if not exists "USERS_index"
    on USERS (email);

create table if not exists REQUESTS
(
    requests_id  BIGINT generated always as identity,
    description  varchar(500) not null,
    requestor_id BIGINT       not null,
    CONSTRAINT requests_pk
        primary key (requests_id),
    constraint requests_fk
        foreign key (requestor_id) references USERS (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    item_id      BIGINT generated always as identity,
    name         varchar(100) not null,
    description  varchar(500),
    is_available boolean      not null,
    owner_id     BIGINT       not null,
    request_id   BIGINT,
    constraint ITEMS_pk
        primary key (item_id),
    constraint ITEMS_USERS_id_fk
        foreign key (owner_id) references USERS (user_id) ON DELETE CASCADE,
    constraint ITEMS_REQUEST_fk
        foreign key (request_id) references REQUESTS (requests_id) ON DELETE CASCADE
);


create table if not exists BOOKINGS
(
    booking_id BIGINT generated always as identity,
    start_date TIMESTAMP WITHOUT TIME ZONE not null,
    end_date   TIMESTAMP WITHOUT TIME ZONE not null,
    status     STATUS                      not null,
    booker_id  BIGINT                      not null,
    item_id    BIGINT                      not null,
    constraint bookings_pk
        primary key (booking_id),
    constraint bookings_ITEMS_ID_fk
        foreign key (item_id) references ITEMS (item_id) ON DELETE CASCADE,
    constraint bookings_users_fk
        foreign key (booker_id) references USERS (user_id) ON DELETE CASCADE
);

create table if not exists COMMENTS
(
    comment_id   BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id      BIGINT,
    item_id      BIGINT,
    comment_text varchar(1000),
    created      timestamp,
    CONSTRAINT COMMENTS_pk
        primary key (comment_id),
    CONSTRAINT COMMENTS_USER_fk
        foreign key (user_id) REFERENCES USERS (user_id),
    CONSTRAINT COMMENTS_ITEM_fk
        foreign key (item_id) REFERENCES ITEMS (item_id)
);
