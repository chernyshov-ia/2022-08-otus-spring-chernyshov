<h3>Домашнее задание №16</h3>

Использовать метрики, healthchecks и logfile

Цель:  
реализовать production-grade мониторинг и прозрачность в приложении


Результат:  
приложение с применением Spring Boot Actuator


Описание/Пошаговая инструкция выполнения домашнего задания:  

* Данное задание выполняется на основе одного из реализованных Web-приложений
* Подключить Spring Boot Actuator в приложение.
* Включить метрики, healthchecks и logfile.
* Реализовать свой собственный HealthCheck индикатор
* UI для данных от Spring Boot Actuator реализовывать не нужно.
* Опционально: переписать приложение на HATEOAS принципах с помощью Spring Data REST Repository

__Заметки по выполнению__

http://localhost:8080/actuator/metrics/disk.free
http://localhost:8080/actuator
http://localhost:8080/actuator/health/books
