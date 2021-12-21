CREATE TABLE IF NOT EXISTS user(
    id varchar(20),
    password varchar(100),
    nickname varchar(100),
    state char(1)
);

-- access token 저장 > jwt 아닐때 유효 토큰 검증시 사용
CREATE TABLE IF NOT EXISTS `oauth_access_token`
(
    `token_id`          VARCHAR(256) NULL,
    `token`             BLOB         NULL,
    `authentication_id` VARCHAR(256) NOT NULL,
    `user_name`         VARCHAR(256) NULL,
    `client_id`         VARCHAR(256) NULL,
    `authentication`    BLOB         NULL,
    `refresh_token`     VARCHAR(256) NULL,
    PRIMARY KEY (`authentication_id`)
);
-- refresh token 저장 > jwt 아닐때 유효 토큰 검증시 사용
CREATE TABLE IF NOT EXISTS `oauth_refresh_token`
(
    `token_id`       VARCHAR(256) NULL,
    `token`          BLOB         NULL,
    `authentication` BLOB         NULL
);