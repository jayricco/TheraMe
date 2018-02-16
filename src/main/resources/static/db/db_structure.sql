--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS role;

CREATE TABLE role (
  role_id INTEGER NOT NULL,
  role varchar(255) DEFAULT NULL,
  PRIMARY KEY (role_id)
);

--
-- Table structure for table users
--
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  user_id INTEGER NOT NULL,
  active INTEGER DEFAULT NULL,
  email VARCHAR NOT NULL UNIQUE,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  address VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  PRIMARY KEY (user_id)
);
--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS users_role;

CREATE TABLE users_role (
  user_id INTEGER NOT NULL,
  role_id INTEGER NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (role_id) REFERENCES role(role_id)
);
