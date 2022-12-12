let books = [];
let genres = [];
let authors = [];
let card = null;
let list = null;
let currentBook = null;

let authorsSelect = null;
let genresSelect = null;

function onBookOpenClick(obj) {
    openCard(obj)
}

function closeCard() {
    card.style.display = "none";
    let cellId = document.getElementById("book-card-book-id");
    cellId.innerText = "";
    cellId.innerHTML = "";

    let cellName = document.getElementById("book-card-book-id");
    cellName.innerText = "";
    cellName.innerHTML = "";

    let cellAuthor = document.getElementById("book-card-book-author");
    cellAuthor.innerText = "";
    cellAuthor.innerHTML = "";

    let cellGenre = document.getElementById("book-card-book-genre");
    cellGenre.innerText = "";
    cellGenre.innerHTML = "";

    currentBook = null;

    redrawList();
    list.style.display = "block";
}

function openCard(obj) {
    console.log("Opening card");
    if (obj == null || obj == undefined) {
        return;
    }
    const bookId = parseInt(obj.getAttribute("book-id"));
    console.log("Opening card - bookId: " + bookId);
    currentBook = books.get(bookId);

    console.log("book: " + JSON.stringify(currentBook));

    viewBook();
}

function viewBook() {
    if (currentBook == null || currentBook == undefined) {
        return;
    }

    card.style.display = "none";

    let cellId = document.getElementById("book-card-book-id");
    cellId.innerHTML = "";
    cellId.innerText = currentBook.id;

    let cellName = document.getElementById("book-card-book-name");
    cellName.innerHTML = "";
    cellName.innerText = currentBook.name;

    let cellAuthor = document.getElementById("book-card-book-author");
    cellAuthor.innerHTML = "";
    cellAuthor.innerText = currentBook.author.name;

    let cellGenre = document.getElementById("book-card-book-genre");
    cellGenre.innerHTML = "";
    cellGenre.innerText = currentBook.genre.name;

    document.getElementById("book-card-button-delete").style.display = "inline";
    document.getElementById("book-card-button-edit").style.display = "inline";
    document.getElementById("book-card-button-cancel").style.display = "none";
    document.getElementById("book-card-button-save").style.display = "none";

    list.style.display = "none";
    card.style.display = "block";
}

function createSelectElement(dictionary, selected) {
    const el = document.createElement("select");
    for (const p of dictionary) {
        const op = document.createElement("option");
        op.id = p.id;
        op.text = p.name;
        op.selected = selected == p.id;
        el.add(op);
    }
    return el;
}

function addBook() {
    currentBook = null;

    document.getElementById("book-card-button-delete").style.display = "none";
    document.getElementById("book-card-button-edit").style.display = "none";
    document.getElementById("book-card-button-cancel").style.display = "inline";
    document.getElementById("book-card-button-save").style.display = "inline";

    currentBook = {
        id: null,
        name: "Название книги",
        author: {
            id: authors[0].id,
            name: null
        },
        genre: {
            id: genres[0].id,
            name: null
        }
    };

    let el;

    let cellId = document.getElementById("book-card-book-id");
    cellId.innerHTML = "";
    cellId.innerText = currentBook.id;

    let cellName = document.getElementById("book-card-book-name");
    cellName.innerHTML = "";
    el = document.createElement("input");
    el.id = "book-card-book-name-input";
    el.type = "text";
    el.value = currentBook.name;
    cellName.appendChild(el);

    let cellAuthor = document.getElementById("book-card-book-author");
    cellAuthor.innerHTML = "";
    el = createSelectElement(authors, currentBook.author.id);
    el.id = "book-card-book-author-select";
    cellAuthor.appendChild(el);

    let cellGenre = document.getElementById("book-card-book-genre");
    cellGenre.innerHTML = "";
    el = createSelectElement(genres, currentBook.genre.id);
    el.id = "book-card-book-genre-select";
    cellGenre.appendChild(el);

    list.style.display = "none";
    card.style.display = "block";
}

function editBook() {
    if (currentBook == null || currentBook == undefined) {
        return;
    }

    card.style.display = "none";

    document.getElementById("book-card-button-delete").style.display = "none";
    document.getElementById("book-card-button-edit").style.display = "none";
    document.getElementById("book-card-button-cancel").style.display = "inline";
    document.getElementById("book-card-button-save").style.display = "inline";

    let el;

    let cellId = document.getElementById("book-card-book-id");
    cellId.innerHTML = "";
    cellId.innerText = currentBook.id;

    let cellName = document.getElementById("book-card-book-name");
    cellName.innerHTML = "";
    el = document.createElement("input");
    el.id = "book-card-book-name-input";
    el.type = "text";
    el.value = currentBook.name;
    cellName.appendChild(el);

    let cellAuthor = document.getElementById("book-card-book-author");
    cellAuthor.innerHTML = "";
    el = createSelectElement(authors, currentBook.author.id);
    el.id = "book-card-book-author-select";
    cellAuthor.appendChild(el);

    let cellGenre = document.getElementById("book-card-book-genre");
    cellGenre.innerHTML = "";
    el = createSelectElement(genres, currentBook.genre.id);
    el.id = "book-card-book-genre-select";
    cellGenre.appendChild(el);

    card.style.display = "block";
}

function cancelEditBook() {
    if (currentBook == null || currentBook == undefined) {
        card.style.display = "none";
        list.style.display = "block";
        return;
    }
    viewBook(currentBook);
}

function saveBook() {
    if (currentBook == null || currentBook == undefined) {
        return;
    }

    const obj = {};

    const cellName = document.getElementById("book-card-book-name");
    obj.name = cellName.getElementsByTagName("input")[0].value;

    const cellAuthor = document.getElementById("book-card-book-author");
    const authorSelect = cellAuthor.getElementsByTagName("select")[0];
    obj.authorId = authorSelect.options[authorSelect.selectedIndex].id;

    let cellGenre = document.getElementById("book-card-book-genre");
    const genreSelect = cellGenre.getElementsByTagName("select")[0];
    obj.genreId = genreSelect.options[genreSelect.selectedIndex].id;

    // alert(JSON.stringify(obj));

    if (!confirm("Сохранить изменения?")) {
        return;
    }

    if (currentBook.id == null || currentBook.id == undefined) {
        fetch("/api/v1/books",
            {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            })
            .then(function(response) {
                return  response.json().then(data => ({ 'status': response.status, 'data': data }));
            })
            .then(function (data) {
                console.log('Request with JSON response', data);
                if(data.status==400) {
                    alert(JSON.stringify(data.data));
                    return;
                }
                let upd = data.data;
                books.set(upd.id, upd);
                currentBook = upd;
                viewBook();
            })
            .catch(function (error) {
                console.log('Request failed', error);
                alert("Ошибка сохранения")
            })
    } else {
        fetch(
        "/api/v1/books/" + currentBook.id,
        {
                method: 'put',
                body: JSON.stringify(obj),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            })
            .then(function(response) {
                return  response.json().then(data => ({ 'status': response.status, 'data': data }));
            })
            .then(function (data) {
                console.log('Request with JSON response', data);
                if(data.status==400) {
                    alert(JSON.stringify(data.data));
                    return;
                }
                let upd = data.data;
                books.set(upd.id, upd);
                currentBook = upd;
                viewBook();
            })
            .catch(function (error) {
                console.log('Request failed', error);
                alert("Ошибка сохранения")
            });
    }


}

function deleteBook() {
    if (currentBook == null || currentBook == undefined) {
        return;
    }

    if (!confirm('Удалить книгу "' + currentBook.name + '"')) {
        return;
    }

    fetch("/api/v1/books/" + currentBook.id, {method: 'delete'})
        .then(function (data) {
            console.log('Delete succeeded with JSON response', data);
            closeCard();
            reload();
        })
        .catch(function (error) {
            console.log('Delete failed', error);
        });
}

function redrawList() {
    const body = document.getElementById('books-table-body');
    body.innerHTML = '';

    let i = 0;
    for (const entry of books) {
        const book = entry[1];
        console.log(book);
        let newRow = body.insertRow(i);
        newRow.insertCell(0).appendChild(document.createTextNode(book.id));
        newRow.insertCell(1).appendChild(document.createTextNode(book.name));
        newRow.insertCell(2).appendChild(document.createTextNode(book.author.name));
        newRow.insertCell(3).appendChild(document.createTextNode(book.genre.name));
        let f = document.createElement("button");
        f.innerText = "Открыть";
        f.setAttribute("book-id", book.id);
        f.setAttribute("onclick", 'onBookOpenClick(this)');
        f.className = "book-button-open";

        newRow.insertCell(4).appendChild(f);
        i++;
    }
}

function reload() {
    fetch("/api/v1/books", {method: 'get'})
        .then(response => response.json())
        .then(function (data) {
            books = new Map();
            data.forEach(book => books.set(book.id, book));
            redrawList();
        })
        .catch(function (error) {
            console.log('Request failed', error);
        });
}

function loadAuthors() {
    fetch("/api/v1/authors", {method: 'get'})
        .then(response => response.json())
        .then(function (data) {
            console.log('Request succeeded with JSON response', data);
            authors = data;
        })
        .catch(function (error) {
            console.log('Request failed', error);
        });
}

function loadGenres() {
    fetch("/api/v1/genres", {method: 'get'})
        .then(response => response.json())
        .then(function (data) {
            console.log('Request succeeded with JSON response', data);
            genres = data;
        })
        .catch(function (error) {
            console.log('Request failed', error);
        });
}

function _onload() {
    reload();
    card = document.getElementById("book-card");
    list = document.getElementById("book-list");
    loadAuthors();
    loadGenres()
}
