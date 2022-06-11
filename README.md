Aplikacja do obsługi konferencji IT.

Założenia:
1.	Konferencja trwa 1 dzień: 1 czerwca 2021. 
2.	Rozpoczyna się o godzinie 10:00 a kończy o godzinie 15:45.
3.	Każda prelekcja trwa 1h 45m (15 minut to przerwa na kawę):
	- pierwsza prelekcja rozpoczyna się o 10:00 i trwa do 11:45. 
	- druga rozpoczyna się o 12:00 i kończy o 13:45
	- trzecia rozpoczyna się o 14:00 i kończy o 15:45
4.	W ramach konferencji obsługiwane są 3 różne ścieżki tematyczne prowadzone równolegle. Jeśli użytkownik zapisze się w danej ścieżce na daną godzinę, to nie może już uczęszczać w tym okresie w innej ścieżce, natomiast o innej godzinie najbardziej może wybrać inną ścieżkę. 
5.	Każda prelekcja może pomieścić maksymalnie 5 słuchaczy. 

Aplikacja wykorzystuje: Java 18, Spring Boot(web, security, jpa, test), H2, Junit, Maven, REST.  

Baza danych: http://localhost:8080/h2-console

Uruchomienie: spring-boot:run

Testy: mvn test



Funkcjonalność:

(ROLA) - oznacza, że do wykonania danego zapytania wymagana jest odpowiednia rola


KONFERENCJA



GET "/" - Plan konferencji ("USER", "PROF", "ADMIN")



WYKŁADY



GET "/lectures/" - Lista wykładów (tytuły) ("USER", "PROF", "ADMIN")

GET "/lectures/show/" - Lista wykładów  ("ADMIN")

GET "/lectures/show/{id}" - Lista wykładów  ("ADMIN")

GET "/lectures/popularity" - Popularność wykładów ("PROF", "ADMIN")

GET "/lectures/theme_popularity" - Popularność tematów ("PROF", "ADMIN")

GET "/lectures/user/" - Login w body, lista wykładów danego użytkownika ("USER", "ADMIN")

POST "/lectures/add/" - Lektura w body, dodanie nowej lektury ("ADMIN")

PUT "/lectures/add/{id}" - Lektura w body, edycja lub dodanie nowej wykładu ("ADMIN")

PUT "/lectures/{id}/add_user" - Login i email w body, dodanie użytkownika do danego wykładu ("USER", "ADMIN")

PUT "/lectures/{id}/cancel_user" - Login i email w body, usuniecie użytkownika z danego wykładu  ("USER", "PROF", "ADMIN")

DELETE "lectures/delete/{id}" - Usunięcie wykładów ("ADMIN")




USER

GET "/users/" - Lista użytkowników (login i email) ("PROF", "ADMIN")

GET "/users/show/" - Lista użytkowników ("ADMIN")

GET "/users/show/{id}" - Lista użytkowników ("ADMIN")

PUT "/users/email/" - Stary email i nowy w body, zmiana emaila ("USER", "ADMIN")

POST "/users/add/" - User w body, dodanie nowego użytkownika ("ADMIN")

PUT "/users/add/{id}" - User w body, edycja lub dodanie nowego użytkownika ("ADMIN")

DELETE "users/delete/{id}" - Usunięcie użytkownika ("ADMIN")
