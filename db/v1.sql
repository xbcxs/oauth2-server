DROP table IF EXISTS auth_client ;
CREATE TABLE auth_client (
  ID INT PRIMARY KEY,
  client_id VARCHAR(255),
  client_name varchar(255),
  client_secret varchar(100),
  redirect_uri varchar(255),
  state varchar(20)
);