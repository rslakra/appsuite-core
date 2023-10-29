# Core
The ```Core``` project contains the common re-usable components.

****
How to set up
---

#### 1. Clone the repository in your GIT account or local machine

> ```
>   git clone https://github.com/rslakra/AppSuite.git
> ```

#### 2. Build the project

> ```
>   cd Core
>   ./build.sh
> ```
>

#### 3. Run the program

Run the program with the following command
  
>   ```./run.sh```

## Create MySQL User

- Login to MySQL with ```root``` user
```shell
mysql -u root -p
```

- Create new User
```shell
USE mysql;
CREATE USER 'user'@'localhost' IDENTIFIED BY 'P@ssW0rd';
GRANT ALL ON *.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
```

OR
```shell
USE mysql;
CREATE USER 'sql-user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL ON *.* TO 'sql-user'@'localhost';
FLUSH PRIVILEGES;
```

- Login with new User
```shell
mysql -u sql-user -ppassword
```

## To Login Automatically to MySQL
```shell
rlakra@C02XG1R2JGH8 ~ % cat /usr/local/etc/my.cnf   
# Default Homebrew MySQL server config
[client]
# The following password is sent to all standard MySQL clients
user=root
password=


[mysqld]
port=3306
socket=/tmp/mysql.sock

# Only allow connections from localhost
bind-address = 127.0.0.1
mysqlx-bind-address = 127.0.0.1
```


## Built With

* [Java](https://www.java.com/en/download/mac_download.jsp) - The Java Download Location
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Rohtash Lakra** - [Core](https://github.com/rslakra/AppSuite.git/Core)


See also the list of [contributors](https://github.com/rslakra/AppSuite.git/contributors) who participated in 
this project.
