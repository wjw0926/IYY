# IYY

## Pre-requisite
Android 5.0.0, API 21 is recommended.

## Install MySQL
You need to create two tables in one local database.

```
create database iyydb;
```

```
create table test(
    phoneID varchar(50) not null
    sns varchar(20) not null
    date date not null
    primary key(phoneID, date));
```

```
create table stest(
    phoneID varchar(50) not null
    sns varchar(20) not null
    status varchar(30) not null
    date date not null
    primary key(phoneID, date));
```

## Start Apache server
```
sudo apachectl start/stop/restart
```

## Inside code
You need to modify IP addresses in MainActivity.java and ChartActivity.java to your local server.
Put four php query files(Sites directory) in suitable location.



