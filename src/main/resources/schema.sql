drop table if exists user;

drop table if exists post;

create table user(
                     id bigint auto_increment primary key,
                     username varchar(255) not null,
                     password varchar(255) not null,
                     enabled boolean not null,
                     role varchar(255) not null
);

CREATE TABLE post(
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     version BIGINT,
                     title VARCHAR(255) NOT NULL,
                     content TEXT NOT NULL,
                     author_id BIGINT NOT NULL,
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     FOREIGN KEY (author_id) REFERENCES user(id)
);
