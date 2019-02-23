create table fax_fax (
	id binary(16) not null,
	description varchar(200),
	executed_id varchar(100),
	executed_count integer not null,
	executed_date datetime,
	requested_date datetime,
	requester_id varchar(50),
	terminated bit not null,
	primary key (id)
) engine=InnoDB;

create index IDXfw228votptbut42y6qgp9eil7
	on fax_fax (requested_date);
