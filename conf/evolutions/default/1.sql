
# --- !Ups
CREATE TABLE users (
    uid          int PRIMARY KEY,
    login        varchar(32) NOT NULL,
    pass_hash    varchar(100) NOT NULL
);

CREATE TABLE operators (
    oid         int PRIMARY KEY,
    login       varchar(32) NOT NULL,
	pass_hash	varchar(100) NOT NULL
);

CREATE DOMAIN winStrategy AS char(13) 
    CHECK (VALUE IN ('max_points', 'shortest_time'));

CREATE DOMAIN diffLvl AS int 
    CHECK (VALUE IN (1, 2, 3));
	
CREATE TABLE games (
    gid         int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    version     int NOT NULL,
    name        varchar(100) NOT NULL,
    description varchar(1000) NOT NULL,
    operator    int REFERENCES operators(oid),
    started     timestamp NOT NULL DEFAULT NOW(),
    ended       timestamp,
	winning		winStrategy NOT NULL DEFAULT 'max_points',
	winPlayers	int NOT NULL DEFAULT 1,
	difficulty	diffLvl NOT NULL DEFAULT 1,
    maxPlayers  int NOT NULL DEFAULT 1000000,
	awards		varchar(1000),
    currPlayers int NOT NULL DEFAULT 0
);

CREATE TABLE tasks (
    gid         int REFERENCES games(gid),
    tid         int,
    version     int NOT NULL,
    name        varchar(100) NOT NULL,
    description varchar(1000) NOT NULL,
    deadline    timestamp NOT NULL,
    maxpoints   int NOT NULL,
    maxattempts int NOT NULL,
    PRIMARY KEY (gid, tid)
);

CREATE TABLE usergames (
    uid         int REFERENCES users(uid),
    gid         int REFERENCES games(gid),
    joined      timestamp NOT NULL,
    PRIMARY KEY (uid, gid)
);

CREATE DOMAIN taskStatus AS char(10) 
    CHECK (VALUE IN ('not send', 'pending', 'rejected', 'accepted'));

CREATE TABLE usertasks (
    uid         int REFERENCES users(uid),
    gid         int REFERENCES games(gid),
    tid         int NOT NULL,
    attempts    int DEFAULT 0,
    status      taskstatus NOT NULL DEFAULT 'not send',
    points      int,
    PRIMARY KEY (uid, gid, tid),
    FOREIGN KEY (gid, tid) REFERENCES tasks(gid, tid)
);

# --- !Downs
