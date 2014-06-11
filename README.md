Instalacja
==========

uruchamiamy skrypt activator (activator.bat dla Windows)

w konsoli sbt -run - i aplikacja rusza na localhost:9000


Uruchomienie całego systemu
==========

Przed uruchomieniem dobrze jest wygenerować dane: POST na  http://immense-refuge-2812.herokuapp.com/sample/test?config=1

Na adress http://immense-refuge-2812.herokuapp.com/push/test?config=1 wysyłamy JSONa

```javascrpit
[{
    "host": "integracja.herokuapp.com",
    "path": "/rest/events"
},
{
    "host": "ztis-statystyczna.herokuapp.com",
    "path": "/generator"
}]
```

który wskazuje na komponenty które mają być uruchomione.

Czekamy krótką chwile. Po czym podziwiamy wyniki pod adresem: 

http://immense-refuge-2812.herokuapp.com/results

Dla czytelności możemy wcześniej usuąc poprzednie wyniki za pomocą DELETE'a na adress 

http://immense-refuge-2812.herokuapp.com/results
