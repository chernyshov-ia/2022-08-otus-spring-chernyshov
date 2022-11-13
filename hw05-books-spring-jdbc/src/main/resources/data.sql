insert into genres(name) values ( 'fantasy' );
insert into genres(name) values ( 'novel' );

insert into authors(name) values ( 'Arthur Conan Doyle' );
insert into authors(name) values ( 'Jack London' );
insert into authors(name) values ( 'John Ronald Reuel Tolkien' );

insert into books(name, genre_id, author_id) values ( 'Hobbit', 1, 3 );
insert into books(name, genre_id, author_id) values ( 'The Lord of the Rings', 1, 3 );
insert into books(name, genre_id, author_id) values ( 'White Fang', 2, 2 );
