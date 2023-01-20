insert into authors(id, name)
select id, name from tmp_authors_migrate;


insert into genres(id, name)
select id, name from tmp_authors_migrate;


insert into books(id, name, author_id, genre_id)
select b.id
     , b.name
     , a.id
     , g.id
  from tmp_books_migrate b
  join tmp_authors_migrate a on a.old_id = b.author_id
  join tmp_genres_migrate g on g.old_id = b.genre_id;
