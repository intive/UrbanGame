# --- !Ups

create table "ABCTASKS" ("gameId" INTEGER NOT NULL,"taskId" INTEGER NOT NULL,"character" VARCHAR NOT NULL,"option" VARCHAR NOT NULL,"points" INTEGER NOT NULL);
alter table "ABCTASKS" add constraint "ABCTASKS_PK" primary key("gameId","taskId","character");
create table "GPSTASKS" ("gameId" INTEGER NOT NULL,"taskId" INTEGER NOT NULL,"pointId" INTEGER NOT NULL,"lat" DOUBLE NOT NULL,"lon" DOUBLE NOT NULL,"range" INTEGER NOT NULL);
alter table "GPSTASKS" add constraint "GPSTASKS_PK" primary key("gameId","taskId","pointId");
create table "GAMES" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"version" INTEGER DEFAULT 1 NOT NULL,"description" VARCHAR NOT NULL,"location" VARCHAR NOT NULL,"operatorId" INTEGER NOT NULL,"created" TIMESTAMP NOT NULL,"updated" TIMESTAMP NOT NULL,"startTime" TIMESTAMP NOT NULL,"endTime" TIMESTAMP NOT NULL,"started" TIMESTAMP,"ended" TIMESTAMP,"winning" VARCHAR DEFAULT 'max_points' NOT NULL,"nWins" INTEGER DEFAULT 1 NOT NULL,"difficulty" VARCHAR DEFAULT 'easy' NOT NULL,"maxPlayers" INTEGER DEFAULT 1000000 NOT NULL,"awards" VARCHAR NOT NULL,"status" VARCHAR DEFAULT 'project' NOT NULL,"image" VARCHAR DEFAULT 'games/gameicon.png' NOT NULL,"tasksNo" INTEGER DEFAULT 0 NOT NULL,"numberOfPlayers" INTEGER DEFAULT 0 NOT NULL);
create unique index "g_idx1" on "GAMES" ("name");
create table "NOTIFICATIONS" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,"gameId" INTEGER NOT NULL,"version" INTEGER DEFAULT 1 NOT NULL,"name" VARCHAR NOT NULL,"deadline" TIMESTAMP NOT NULL);
create table "OPERATORS" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"email" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"name" VARCHAR NOT NULL,"logo" VARCHAR DEFAULT 'users/logo.png' NOT NULL,"description" VARCHAR,"permission" VARCHAR NOT NULL,"created" TIMESTAMP NOT NULL,"modified" TIMESTAMP NOT NULL,"validated" BOOLEAN DEFAULT false NOT NULL,"token" VARCHAR);
create table "SKINS" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"gameId" INTEGER NOT NULL,"icon" VARCHAR DEFAULT 'games/gameicon.png' NOT NULL);
create table "TASKS" ("id" INTEGER NOT NULL,"gameId" INTEGER NOT NULL,"version" INTEGER DEFAULT 1 NOT NULL,"type" VARCHAR NOT NULL,"name" VARCHAR NOT NULL,"description" VARCHAR NOT NULL,"maxpoints" INTEGER NOT NULL,"minToAccept" INTEGER DEFAULT 1 NOT NULL,"maxattempts" INTEGER NOT NULL,"timeLimit" TIMESTAMP,"lat" DOUBLE,"lon" DOUBLE,"rangeLimit" INTEGER,"active" BOOLEAN DEFAULT true NOT NULL,"penalty" INTEGER DEFAULT 0 NOT NULL);
alter table "TASKS" add constraint "TASKS_PK" primary key("gameId","id");
create table "TOKENS" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"opId" INTEGER NOT NULL,"token" VARCHAR NOT NULL,"series" VARCHAR NOT NULL,"created" TIMESTAMP NOT NULL,"expires" TIMESTAMP NOT NULL,"rememberme" BOOLEAN NOT NULL);
create table "USERGAMES" ("userId" INTEGER NOT NULL,"gameId" INTEGER NOT NULL,"joined" TIMESTAMP DEFAULT {ts '2013-07-06 00:06:01.687'} NOT NULL,"left" TIMESTAMP,"points" INTEGER DEFAULT 0 NOT NULL);
alter table "USERGAMES" add constraint "USERSGAMES_PK" primary key("userId","gameId");
create table "USERTASKS" ("userId" INTEGER NOT NULL,"gameId" INTEGER NOT NULL,"taskId" INTEGER NOT NULL,"status" VARCHAR DEFAULT 'not sent' NOT NULL,"points" INTEGER DEFAULT 0 NOT NULL,"attempts" INTEGER DEFAULT 0 NOT NULL,"time" TIMESTAMP);
alter table "USERTASKS" add constraint "USERTASKS_PK" primary key("userId","gameId","taskId");
create table "USERS" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"login" VARCHAR NOT NULL,"hash" VARCHAR NOT NULL);
alter table "ABCTASKS" add constraint "ABCTASKS_GAMES_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "ABCTASKS" add constraint "ABCTASKS_TASKS_FK" foreign key("gameId","taskId") references "TASKS"("gameId","id") on update NO ACTION on delete NO ACTION;
alter table "GPSTASKS" add constraint "GPSTASKS_GAMES_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "GPSTASKS" add constraint "GPSTASKS_TASKS_FK" foreign key("gameId","taskId") references "TASKS"("gameId","id") on update NO ACTION on delete NO ACTION;
alter table "GAMES" add constraint "OP_FK" foreign key("operatorId") references "OPERATORS"("id") on update NO ACTION on delete NO ACTION;
alter table "NOTIFICATIONS" add constraint "GMN_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "SKINS" add constraint "GM_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "TASKS" add constraint "GMT_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "USERGAMES" add constraint "USERGAMES_GAMES_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "USERGAMES" add constraint "USERGAMES_USERS_FK" foreign key("userId") references "USERS"("id") on update NO ACTION on delete NO ACTION;
alter table "USERTASKS" add constraint "USERTASKS_GAMES_FK" foreign key("gameId") references "GAMES"("id") on update NO ACTION on delete NO ACTION;
alter table "USERTASKS" add constraint "USERTASKS_TASKS_FK" foreign key("gameId","taskId") references "TASKS"("gameId","id") on update NO ACTION on delete NO ACTION;
alter table "USERTASKS" add constraint "USERTASKS_USERS_FK" foreign key("userId") references "USERS"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs
