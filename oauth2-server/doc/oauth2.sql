create database oauth;
use oauth;

drop table if exists oauth_client_details;
create table oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);

create table if not exists oauth_client_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);

create table if not exists oauth_access_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication LONG VARBINARY,
  refresh_token VARCHAR(255)
);

create table if not exists oauth_refresh_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication LONG VARBINARY
);

create table if not exists oauth_code (
  code VARCHAR(255), authentication LONG VARBINARY
);

create table if not exists oauth_approvals (
	userId VARCHAR(255),
	clientId VARCHAR(255),
	scope VARCHAR(255),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);

create table if not exists ClientDetails (
  appId VARCHAR(255) PRIMARY KEY,
  resourceIds VARCHAR(255),
  appSecret VARCHAR(255),
  scope VARCHAR(255),
  grantTypes VARCHAR(255),
  redirectUrl VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(255)
);


INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('client1', 'secret', 'foo,read,write',
	'password,authorization_code,refresh_token', 'http://www.baidu.com', null, 3600, 36000, null, 'true');
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('client2', 'secret', 'read,write,foo,bar',
	'implicit', 'http://www.baidu.com', null, 3600, 36000, null, 'false');
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('client3', 'secret', 'bar,read,write',
	'client_credentials', 'http://www.baidu.com', null, 3600, 36000, null, 'true');

INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('client4', 'secret', 'read,write',
	'password,authorization_code,refresh_token', 'http://127.0.0.1:8081/index', null, 3600, 36000, null, 'true');	
	
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sso-client1', 'secret', 'read,write',
	'password,authorization_code,refresh_token', 'http://127.0.0.1:9091/login', null, 3600, 36000, null, 'true');	
	
	
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sso-client2', 'secret', 'read,write',
	'password,authorization_code,refresh_token', 'http://127.0.0.1:9092/login', null, 3600, 36000, null, 'true');	
	

drop table if exists t_user;
CREATE TABLE t_user (
  id VARCHAR(32) PRIMARY KEY,
  name VARCHAR(255),
  username VARCHAR(255),
  password VARCHAR(255),
  phone VARCHAR(11)
);

INSERT INTO t_user (id, name, username, password, phone) 
VALUES ('1', '管理员', 'admin', '123456', '15805058080');