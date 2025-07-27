# Домашнее задание 3 - Разработка Backend Java

- [README.md](./homework_3/README.md)

---

- [source code](./homework_3/)
- [output (logs)](https://github.com/UmbrellaLeaf5/debut_camp_t1_homework/tree/output/homework_3/logs)

## Секретное задание для инженера Wayland-Yutani

**Код доступа:** OMEGA  
**Тема:** Разработка Synthetic Human Core Starter ("Project Bishop")

## Постановка задачи

После инцидента с андроидом на корабле «Постромо» компанией было принято решение создать единый **synthetic-human-core-starter**, который будет использоваться для реализации логики работы всех будущих моделей искусственных людей.

Стартер должен содержать в себе следующее:

1. Модуль приема и исполнения команд;
2. Возможность мониторинга текущей занятости андроида;
3. Возможность аудита любых действий андроида;
4. Модуль обработки ошибок.

Детальнее про каждую из поставленных задач ниже.

## Модуль приема и исполнения команд

_Андроиды должны четко понимать и исполнять поставленные задачи_

В компании принимается единый формат команд для андроидов, который будет содержать в себе следующие поля:

| Поле description | Тип                     | Ограничения                    | Описание                     | Пример                                               |
| ---------------- | ----------------------- | ------------------------------ | ---------------------------- | ---------------------------------------------------- |
| description      | String                  | До 1000 символов               | Описание исполняемой команды | Проверить состояние энергоблока космического корабля |
| priority         | Enum (COMMON, CRITICAL) | Должно быть значением Enum     | Приоритет выполнения команды | CRITICAL                                             |
| author           | String                  | До 100 символов                | Автор команды                | Лейтенант Эллен Рипли                                |
| time             | String                  | Должно соответствовать формату | Время назначения команды     | Строка со временем в формате ISO-8601                |

Необходимые детали реализации:

- Команды должны валидироваться в рамках указанных ограничений, в случае их некорректности должна быть возвращена соответствующая ошибка;
- Команды с приоритетом **CRITICAL** исполняются **моментально**, команды с приоритетом **COMMON** добавляются в **очередь**, которая обрабатывается **отдельно**;
- Очередь исполнения команд должна обрабатываться отдельным процессом;
- Учитывайте, что очередь команд не может быть бесконечной, ее переполнение должно возвращать соответствующую ошибку;
- Для первой версии в качестве исполнения команды достаточно просто выводить информацию о ней в лог.

## Возможность аудита любых действий андроида

_Каждое действие синтетика должно быть запротоколировано_

Необходимо реализовать аннотацию `@WaylandWatchingYou`, которая будет обеспечивать аудит выполняемых в софте андроида методов. В аудит должна отправляться информация о имени метода, передаваемых параметрах и результате его выполнения.

Аудит должен быть возможен в двух режимах (должно настраиваться):

- Отправка данных в указанный Kafka Topic;
- Вывод информации в консоль.

Методы, которые будут маркироваться данной аннотацией – ответственность разработчика софта на базе вашего стартера.

## Возможность мониторинга текущей занятости андроида

_Компании необходимо отслеживать эффективность работы каждого!_

В рамках стартера необходимо реализовать возможность публикации следующих метрик:

- Текущая занятость андроида (количество задач в очереди);
- Количество выполненных заданий для каждого автора.

## Модуль обработки ошибок

_Все ломается, компания должна знать детали!_

В первых версиях взаимодействие с андроидами предполагается с помощью REST API. В случае возникновения ошибок они должны возвращаться в едином формате и с корректными HTTP кодами.

## Дополнительные опции

_При желании вы можете расширить стартер любым функционалом, который вы посчитаете нужным. Но помните, все должно быть под контролем, инциденты нам более не нужны!_

# Самопроверка

Для проверки и будущей презентации полученного стартера руководству необходимо разработать простейший сервис-эмулятор синтетика **bishop-prototype**, который должен принимать команды с помощью REST API и полностью демонстрировать разработанный стартер.

# Технические рекомендации

Ниже перечислен стек технологий, который рекомендуется использовать для решения задачи:

- Spring Boot 3 – ключевой фреймворк для разработки;
- ThreadPoolExecutor – для реализации очереди задач;
- Kafka, AOP – для реализации задачи по аудиту;
- Actuator, Micrometer – для публикации метрик;
- `@ExceptionHandler` – для обработки ошибок.

**С Уважением,**  
дирекция по роботизации компании Wayland-Yutani

_Мы строим лучшие миры!_

# Послесловие автора

У данной задачи нет единственно верной реализации, многие принятые в ней решения могут стать поводом для дискуссии.

Цель задачи – познакомиться с новыми инструментами и попробовать применить их.

Отнеситесь к данной работе творчески 😊.

# О проекте (моя реализация)

## Оглавление

1. [Обзор](#обзор)
2. [Быстрый старт](#быстрый-старт)
3. [Пример реализации (Bishop Prototype)](#пример-реализации-bishop-prototype)
4. [Модули](#модули)
5. [Конфигурация](#конфигурация)
6. [API Reference](#api-reference)

## Обзор

**Synthetic Human Core Starter** - это базовый стартер для создания систем управления синтетическими людьми (андроидами). Основные возможности:

- Прием и исполнение команд с приоритетами
- Полный аудит всех действий
- Мониторинг состояния через метрики
- Стандартизированная обработка ошибок

## Быстрый старт

1. Добавьте зависимость в ваш проект:

```xml
<dependency>
    <groupId>io.github.UmbrellaLeaf5</groupId>
    <artifactId>synthetic-human-core-starter</artifactId>
    <version>1.0</version>
</dependency>
```

2. Создайте основной класс приложения:

```java
@SpringBootApplication
public class BishopMain {
    public static void main(String[] args) {
        SpringApplication.run(BishopMain.class, args);
    }
}
```

## Пример реализации (Bishop Prototype)

### Структура проекта

```
bishop-prototype/
├── src/
│   ├── main/java/io/github/UmbrellaLeaf5/
│   │   ├── api/               # API слои
│   │   ├── command/           # Логика обработки команд
│   │   └── BishopMain.java     # Точка входа
└── pom.xml                    # Конфигурация Maven
```

### Основные компоненты

#### 1. API Endpoint

```java
@RestController
@RequestMapping("/command")
public class BishopApiV1 {
    private final BishopCommandService commandService;

    @PostMapping
    public void processCommand(
            @RequestParam CommandType commandType,
            @RequestParam Initiator initiator) {
        commandService.runCommand(commandType, initiator);
    }
}
```

Пример запроса:

```
POST /command?commandType=ENGINE&initiator=WAYLAND_YUTANI_OFFICER
```

#### 2. Сервис обработки команд

```java
@Service
public class BishopCommandService {
    private final ThreadPoolCommandService commandService;

    @WaylandWatchingYou
    public void runCommand(CommandType commandType, Initiator initiator) {
        SynthCommand command = SynthCommand.builder()
                .description(chooseDescription(commandType))
                .author(initiator.name())
                .priority(choosePriority(initiator))
                .time(Instant.now().toString())
                .build();

        commandService.processCommand(command);
    }
}
```

#### 3. Модели данных

```java
public enum CommandType { ALERT, HELP, KILL, ENGINE }
public enum Initiator { REGULAR_HUMAN, WAYLAND_YUTANI_OFFICER }
```

#### 4. Обработка ошибок

```java
@RestControllerAdvice
public class BishopExceptionHandler extends SyntheticHumanGlobalExceptionHandler {
    @ExceptionHandler(UnavailableCommandException.class)
    public ErrorResponse handleUnavailableCommand(UnavailableCommandException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "COMMAND IS UNAVAILABLE");
    }
}
```

## Модули

### Команды

- `ThreadPoolCommandService` - обработка команд с приоритетами
- `SynthCommand` - модель команды

Пример создания команды:

```java
SynthCommand command = SynthCommand.builder()
    .description("Check systems")
    .priority(SynthCommandPriority.CRITICAL)
    .author("Ripley")
    .time(Instant.now().toString())
    .build();
```

### Аудит

Аннотация `@WaylandWatchingYou` для аудита методов:

```java
@WaylandWatchingYou
public void criticalOperation() {
    // Логика метода
}
```

### Метрики

Доступные эндпоинты:

- `/actuator` - список эндпоинтов
- `/actuator/health` - состояние системы
- `/actuator/metrics` - список метрик

и другие...

## Конфигурация

Пример `application.yml`:

```yaml
synth:
  core:
    command:
      pool-properties:
        min-size: 3
        max-size: 5
        queue-capacity: 10

    audit:
      mode: CONSOLE # или KAFKA

    metrics:
      busyness:
        initial-delay: 1s
        fixed-delay: 5s
```

## API Reference

### Основные endpoint-ы

| Метод | Путь     | Параметры              | Описание                  |
| ----- | -------- | ---------------------- | ------------------------- |
| POST  | /command | commandType, initiator | Отправка команды андроиду |

### Модели данных

#### CommandType

```java
public enum CommandType {
    ALERT,    // Аварийный сигнал
    HELP,     // Запрос помощи
    KILL,     // Команда отключения (ограничена)
    ENGINE    // Проверка двигателя
}
```

#### Initiator

```java
public enum Initiator {
    REGULAR_HUMAN,          // Обычный человек
    WAYLAND_YUTANI_OFFICER  // Офицер компании
}
```

## Лицензия

Данный стартер является собственностью компании Wayland-Yutani. Использование разрешено только санкционированным персоналом. ( :) )

---

_Мы строим лучшие миры!_  
© 2025 Wayland-Yutani Corporation. Все права (не) защищены.
