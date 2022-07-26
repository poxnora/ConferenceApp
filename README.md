**Aplikacja do obsługi konferencji IT.**

**Założenia**
1.	Konferencja trwa 1 dzień: 1 czerwca 2021. 
2.	Rozpoczyna się o godzinie 10:00 a kończy o godzinie 15:45.
3.	Każda prelekcja trwa 1h 45m (15 minut to przerwa na kawę):
	- pierwsza prelekcja rozpoczyna się o 10:00 i trwa do 11:45. 
	- druga rozpoczyna się o 12:00 i kończy o 13:45
	- trzecia rozpoczyna się o 14:00 i kończy o 15:45
4.	W ramach konferencji obsługiwane są 3 różne ścieżki tematyczne prowadzone równolegle. Jeśli użytkownik zapisze się w danej ścieżce na daną godzinę, to nie może już uczęszczać w tym okresie w innej ścieżce, natomiast o innej godzinie najbardziej może wybrać inną ścieżkę. 
5.	Każda prelekcja może pomieścić maksymalnie 5 słuchaczy. 

**Aplikacja wykorzystuje**

Java 17, Spring Boot(web, security, jpa, test), H2, Junit, Mockito, Maven, Rest Assured.  

**Baza danych**

http://localhost:8080/h2-console (admin,admin) (dostęp do konsoli: user, root)

**Uruchomienie**

.\mvnw spring-boot:run

**Role**

(ROLA) - oznacza, że do wykonania danego zapytania wymagana jest odpowiednia rola

* "user1", "password1" (USER)

* "admin", "admin" (ADMIN)

* "prof", "prof" (PROF)

**Konferencja**

* GET "/conference" - Plan konferencji ("USER", "PROF", "ADMIN")

* POST "/conference" - Dodaj godzine wykładu ("ADMIN")

* PUT "/conference" - Edytuj konferencje("ADMIN")
 
  { 
  "start_time":   "31/07/2023 10:00:00" ,
  "end_time": "31/08/2023 10:00:00",
   "themes": 3
  }

* DELETE "/conference/{number}" - Usuń godzine wykładu ("ADMIN")

**Wykłady**

* GET "/lectures" - Lista wykładów ("USER", "PROF", "ADMIN")

* GET "/lectures/{id}" - Wykład po id ("ADMIN")

* GET "/lectures/titles" - Lista wykładów (tytuły i data rozpoczęcia) ("ADMIN")

* GET "/lectures/popularity" - Popularność wykładów ("PROF", "ADMIN")

* GET "/lectures/theme_popularity" - Popularność tematów ("PROF", "ADMIN")

* POST "/lectures" - Wykład w body, dodanie nowego wykładu ("ADMIN")

* PUT "/lectures/{id}" - Wykład w body, edycja lub dodanie nowego wykładu ("ADMIN")

* PUT "/lectures/{id}/users" - Login i email w body, dodanie użytkownika do danego wykładu ("USER", "ADMIN")

  {
        "username": "user1",
        "email": "user1@emaiddasdasdl.com"
 }

* PUT "/lectures/{id}/users/cancellation" - Login i email w body, usunięcie użytkownika z danego wykładu  ("USER", "PROF", "ADMIN")

  {
"username": "user1",
"email": "user1@emaiddasdasdl.com"
  }

**Użytkownik**

* GET "/users" - Lista użytkowników (login i email) ("PROF", "ADMIN")

* GET "/users/details" - Lista użytkowników ("ADMIN")

* GET "/users/details/{id}" - Użytkownik po id ("ADMIN")

* GET "/users/lectures?username="  lista wykładów danego użytkownika ("USER", "ADMIN")

* POST "/users" - User w body, dodanie nowego użytkownika ("ADMIN")

* PUT "/users/{id}" - User w body, edycja lub dodanie nowego użytkownika ("ADMIN")

* PUT "users/change_email" - Edycja emaila ("USER",ADMIN")

  { 
"old_email": "user1@email.com",
"new_email": "user@email.com"
}