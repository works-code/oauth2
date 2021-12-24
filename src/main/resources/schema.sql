-- 사용자 테이블
CREATE TABLE IF NOT EXISTS user(
    id varchar(20),
    password varchar(100),
    nickname varchar(100),
    state char(1)
);

-- access token 테이블 > jwt 아닐때 유효 토큰 검증시 사용
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
-- refresh token 테이블 > jwt 아닐때 유효 토큰 검증시 사용
CREATE TABLE IF NOT EXISTS `oauth_refresh_token`
(
    `token_id`       VARCHAR(256) NULL,
    `token`          BLOB         NULL,
    `authentication` BLOB         NULL
);

-- 클라이언트 정보 테이블
CREATE TABLE IF NOT EXISTS `oauth_client_details`
(
    `client_id`               VARCHAR(256)  NOT NULL,
    `resource_ids`            VARCHAR(256)  NULL,
    `client_secret`           VARCHAR(256)  NULL,
    `scope`                   VARCHAR(256)  NULL,
    `authorized_grant_types`  VARCHAR(256)  NULL,
    `web_server_redirect_uri` VARCHAR(256)  NULL,
    `authorities`             VARCHAR(256)  NULL,
    `access_token_validity`   INT           NULL,
    `refresh_token_validity`  INT           NULL,
    `additional_information`  VARCHAR(4096) NULL,
    `autoapprove`             VARCHAR(256)  NULL,
    PRIMARY KEY (`client_id`)
);

-- 권한 관리 테이블
create table oauth_approvals
(
    userId         VARCHAR(256),
    clientId       VARCHAR(256),
    scope          VARCHAR(256),
    status         VARCHAR(10),
    expiresAt      TIMESTAMP,
    lastModifiedAt TIMESTAMP
);