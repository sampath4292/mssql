1. Add new column to products table that stores tax rate for the products update tax rate to 12% to all products.

ALTER TABLE products ADD tax_rate DECIMAL(5,2);

UPDATE products SET tax_rate = 12.00;

2. Display titles that End with 's'/'t'.

SELECT title FROM titles
WHERE title LIKE '%s' OR title LIKE '%t';
3. Display books of type business, psychology & undecided.

SELECT title, type FROM titles
WHERE type IN ('business', 'psychology', 'undecided');
4. Display titles where the sales > 5,000 & royalty < 20.

SELECT title FROM titles
WHERE ytd_sales > 5000 AND royalty < 20;
5. Display titles in the ascending of sales for publisher 0736.

SELECT title, ytd_sales FROM titles
WHERE pub_id = '0736'
ORDER BY ytd_sales ASC;
6. Display the difference between maximum & minimum royalty of books published by publisher 0877.

SELECT (MAX(royalty) - MIN(royalty)) AS RoyaltyDifference
FROM titles
WHERE pub_id = '0877';
7. Display author_id & no of books written by Author.

SELECT au_id, COUNT(title_id) AS no_of_books
FROM titleauthor
GROUP BY au_id;
8. Display how many authors are there for each title.

SELECT title_id, COUNT(au_id) AS no_of_authors
FROM titleauthor
GROUP BY title_id;
9. Display average royalty % for authors with order 1.

SELECT AVG(royaltyper) AS avg_royalty_percentage
FROM titleauthor
WHERE au_ord = 1;
10. Display titles in the order of price if sales are in the range 10k to 20k.

SELECT title, price, ytd_sales FROM titles
WHERE ytd_sales BETWEEN 10000 AND 20000
ORDER BY price;
11. Display how many authors are in the city Menlo park.

SELECT COUNT(au_id) AS no_of_authors
FROM authors
WHERE city = 'Menlo Park';
12. Display state and no of authors we have in the state in the order of state.

SELECT state, COUNT(au_id) AS no_of_authors
FROM authors
GROUP BY state
ORDER BY state;
13. Display States in which we have more than 2 authors, with 1st name starting with 's'.

SELECT state
FROM authors
WHERE au_fname LIKE 'S%'
GROUP BY state
HAVING COUNT(au_id) > 2;
14. Display title after replacing all spaces with (dots) and (hyphens) with (stars).

SELECT REPLACE(REPLACE(title, ' ', '.'), '-', '*') AS modified_title
FROM titles;
15. Display title by Removing all spaces.

SELECT REPLACE(title, ' ', '') AS title_without_spaces
FROM titles;
16. Display first word in the title.

SELECT
    CASE
        WHEN CHARINDEX(' ', title) > 0 THEN SUBSTRING(title, 1, CHARINDEX(' ', title) - 1)
        ELSE title
    END AS first_word
FROM titles;
17. Display month and no of books published.

SELECT DATENAME(month, pubdate) AS published_month, COUNT(title_id) AS no_of_books
FROM titles
GROUP BY DATENAME(month, pubdate);
18. Display title publisher name for titles where the publisher is in USA.

SELECT t.title, p.pub_name
FROM titles t
INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE p.country = 'USA';
19. Display publisher name and average price of books.

SELECT p.pub_name, AVG(t.price) AS avg_price
FROM publishers p
INNER JOIN titles t ON p.pub_id = t.pub_id
GROUP BY p.pub_name;
20. Display City of author and then no of books written by authors in the City.

SELECT a.city, COUNT(ta.title_id) AS no_of_books
FROM authors a
INNER JOIN titleauthor ta ON a.au_id = ta.au_id
GROUP BY a.city;
21. Display author name, title for all authors including the ones without a title.

SELECT a.au_fname, a.au_lname, t.title
FROM authors a
LEFT OUTER JOIN titleauthor ta ON a.au_id = ta.au_id
LEFT OUTER JOIN titles t ON ta.title_id = t.title_id;
22. Display title publisher name and author name of the primary author.

SELECT t.title, p.pub_name, a.au_fname, a.au_lname
FROM titles t
INNER JOIN publishers p ON t.pub_id = p.pub_id
INNER JOIN titleauthor ta ON t.title_id = ta.title_id
INNER JOIN authors a ON ta.au_id = a.au_id
WHERE ta.au_ord = 1;
23. Display City of publisher and maximum price of all titles.

SELECT p.city, MAX(t.price) AS max_price
FROM publishers p
INNER JOIN titles t ON p.pub_id = t.pub_id
GROUP BY p.city;
24. Display titles written by any author in City (menlo park).

SELECT DISTINCT t.title
FROM titles t
INNER JOIN titleauthor ta ON t.title_id = ta.title_id
INNER JOIN authors a ON ta.au_id = a.au_id
WHERE a.city = 'Menlo Park';
25. Display publishers who published a title in 1991.

SELECT DISTINCT p.pub_name
FROM publishers p
INNER JOIN titles t ON p.pub_id = t.pub_id
WHERE YEAR(t.pubdate) = 1991;
26. Display titles not published in USA.

SELECT t.title
FROM titles t
INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE p.country != 'USA';
27. Display titles either published in USA (or) having price < 5.

SELECT t.title
FROM titles t
INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE p.country = 'USA' OR t.price < 5;
28. Create a view to contain title, publisher, year of publishing, price and type. Make sure when price is null display Zero and type is null display Unknown.

CREATE VIEW vw_TitleDetails AS
SELECT
    t.title,
    p.pub_name AS publisher,
    YEAR(t.pubdate) AS year_of_publishing,
    ISNULL(t.price, 0) AS price,
    ISNULL(t.type, 'Unknown') AS type
FROM titles t
INNER JOIN publishers p ON t.pub_id = p.pub_id;

29. Display publishers who published books by author who wrote more than 2 titles.

SELECT DISTINCT p.pub_name
FROM publishers p
INNER JOIN titles t ON p.pub_id = t.pub_id
INNER JOIN titleauthor ta ON t.title_id = ta.title_id
WHERE ta.au_id IN (
    SELECT au_id
    FROM titleauthor
    GROUP BY au_id
    HAVING COUNT(title_id) > 2
);
30. Delete rows from title author for author with first_name as dean.

DELETE FROM titleauthor
WHERE au_id IN (
    SELECT au_id FROM authors WHERE au_fname = 'Dean'
);
31. Update the price of the book BU1111 with the Price of book MC2222.

UPDATE titles
SET price = (
    SELECT price FROM titles WHERE title_id = 'MC2222'
)
WHERE title_id = 'BU1111';
32. Display titles published in last 25 years.

SELECT title, pubdate FROM titles
WHERE pubdate >= DATEADD(year, -25, GETDATE());
33. Display titles published by any publisher who published a title in 2021.

SELECT title, pub_id FROM titles
WHERE pub_id IN (
    SELECT pub_id
    FROM titles
    WHERE YEAR(pubdate) = 2021
);
34. Create a view to display publisher name, city, and no of books published.

CREATE VIEW vw_PublisherBookCount AS
SELECT
    p.pub_name,
    p.city,
    COUNT(t.title_id) AS no_of_books
FROM publishers p
LEFT OUTER JOIN titles t ON p.pub_id = t.pub_id
GROUP BY p.pub_name, p.city;
