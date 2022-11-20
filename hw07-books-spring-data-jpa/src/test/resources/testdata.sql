insert into genres(id, name) values ( 1, 'fantasy' );
insert into genres(id, name) values ( 2, 'novel' );

insert into authors(id, name) values ( 1, 'Arthur Conan Doyle' );
insert into authors(id, name) values ( 2, 'Jack London' );
insert into authors(id, name) values ( 3, 'John Ronald Reuel Tolkien' );

insert into books(name, genre_id, author_id) values ( 'Hobbit', 1, 3 );
insert into books(name, genre_id, author_id) values ( 'The Lord of the Rings', 1, 3 );


insert into comments(text, book_id) values ( 'Comment text 1_1', 1 );
insert into comments(text, book_id) values ( 'Comment text 1_2', 1 );
