create table scape (
	content text not null default ''
);

create table category (
	name varchar(255) not null default '',
	that varchar(255) not null default '',
	topic varchar(255) not null default '',
	goto varchar(255) not null default '',
	link varchar(255) not null default '',
	execute varchar(255) not null default '',
	primary key (name)
);

create table pattern (
	category varchar(255) not null default '',
	content text not null default ''
);

create table template (
	category varchar(255) not null default '',
	conditional varchar(255) not null default '',
	content text not null
);

create table client (
	email varchar(255) not null default '',
	primary key (email)
);

create table variable (
	client varchar(255) not null default '',
	name varchar(255) not null default '',
	value varchar(255) not null default '',
	primary key (client, name)
);

