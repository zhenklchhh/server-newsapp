-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255)
);

-- Таблица сообществ
CREATE TABLE IF NOT EXISTS community (
                           id SERIAL PRIMARY KEY,
                           iduser BIGINT NOT NULL,
                           namecommunity VARCHAR(255) NOT NULL UNIQUE,
                           FOREIGN KEY (iduser) REFERENCES users (id) ON DELETE CASCADE
);

-- Промежуточная таблица для связи "многие ко многим" между пользователями и сообществами
CREATE TABLE IF NOT EXISTS user_community (
                                userid BIGINT NOT NULL,
                                communityid BIGINT NOT NULL,
                                PRIMARY KEY (userid, communityid),
                                FOREIGN KEY (userid) REFERENCES users (id) ON DELETE CASCADE,
                                FOREIGN KEY (communityid) REFERENCES community (id) ON DELETE CASCADE
);

-- Таблица новостей
CREATE TABLE IF NOT EXISTS news (
                      id SERIAL PRIMARY KEY,
                      communityid BIGINT NOT NULL,
                      title VARCHAR(255),
                      content TEXT,
                      publishdate TIMESTAMP WITHOUT TIME ZONE,
                      source VARCHAR(255),
                      FOREIGN KEY (communityid) REFERENCES community (id) ON DELETE CASCADE
);

-- Таблица комментариев
CREATE TABLE IF NOT EXISTS comments (
                          id SERIAL PRIMARY KEY,
                          iduser BIGINT NOT NULL,
                          idnews BIGINT NOT NULL,
                          comment TEXT,
                          FOREIGN KEY (iduser) REFERENCES users (id) ON DELETE CASCADE,
                          FOREIGN KEY (idnews) REFERENCES news (id) ON DELETE CASCADE
);