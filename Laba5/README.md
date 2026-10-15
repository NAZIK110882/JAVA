# Лабораторна робота №5 — Рефакторинг із паттернами GoF

**Предметна область:** система прокату автомобілів (Car Rental System).  
**База:** лабораторна №4. Код **не створено з нуля**: збережені `AbstractVehicle`, `Car`, `Serviceable`, `PricingStrategy`, `DailyPricingStrategy`, `HourlyPricingStrategy`, а класи `RentalOrder` та `Main` розширені й відрефакторені.

## Мета
Покращити створення тарифів (Factory Method), сповіщення про зміну статусу замовлення (Observer) та інтеграцію несумісного платіжного API (Adapter), не змінюючи формули розрахунків із ЛР №4.

## Що було в ЛР №4
- Абстрактний `AbstractVehicle` та конкретний `Car`.
- Інтерфейс `Serviceable`.
- Паттерн Strategy: `PricingStrategy`, `DailyPricingStrategy`, `HourlyPricingStrategy`.
- Клас `RentalOrder`, що використовує обрану стратегію та дозволяє змінювати її під час роботи.
- Збережена математика: подобова вартість `baseRate * days`, погодинна `(baseRate * 0.1) * hours`.

## Рівень 1 — Factory Method (породжувальний)
**Проблема:** у старому `Main.java` конкретні стратегії створювалися через `new DailyPricingStrategy()` / `new HourlyPricingStrategy()` у масиві, конструкторах та під час зміни стратегії. Клієнт залежав від конкретних класів.

**До:**
```java
PricingStrategy strategy = new DailyPricingStrategy();
order1.setPricingStrategy(new HourlyPricingStrategy());
```
**Після:**
```java
PricingStrategy strategy = PricingStrategyFactory.forType("daily").createStrategy();
order1.setPricingStrategy(PricingStrategyFactory.forType("hourly").createStrategy());
```
`PricingStrategyFactory` — абстрактний Creator із **фабричним методом** `createStrategy()`. Його перевизначають `DailyPricingFactory` і `HourlyPricingFactory`, які повертають об'єкти через спільний інтерфейс `PricingStrategy`. Додатковий статичний метод `forType(...)` централізовано обирає потрібний Creator за кодом (елемент Simple Factory).

**Результат:** клієнтський `Main` не знає конструкторів реалізацій тарифів. Для нового тарифу треба створити реалізацію `PricingStrategy`, новий Creator і додати код типу в `PricingStrategyFactory.forType`; `Main` і `RentalOrder` залишаться без змін, якщо тип передають параметром. Це **не** абсолютно «закрита» для змін фабрика: вибір типів у `forType` потрібно розширити.

## Рівень 2 — Observer (поведінковий)
**Проблема:** коли оренда переходить зі стану `NEW` у `PROCESSED`, а після платежу у `COMPLETED`, про це мають дізнатися і логування, і сповіщення клієнта. Прямі виклики двох різних служб усередині `RentalOrder` створили б жорстку залежність.

- **Subject:** `RentalOrder` містить список `OrderObserver`, має `addObserver`, `removeObserver`, приватний `changeStatus`.
- **Observer:** `OrderObserver.onStatusChanged(order, previous, current)`.
- **Concrete Observers:** `OrderLogObserver` та `CustomerNotificationObserver` — підписані **на одне** замовлення.
- Сповіщення є лише під час **реальної зміни** статусу. Для кожного слухача виняток обробляється окремо, щоб інші продовжували отримувати події. Поточний список обходиться у порядку реєстрації, але бізнес-логіка на цей порядок **не покладається**.
- Новий тип спостерігача можна додати без редагування `RentalOrder`.

## Рівень 3 — Adapter (структурний)
**Проблема:** додаток очікує `PaymentProcessor.pay(String orderId, double amountInDollars)`, а імітований сторонній, незмінюваний клас `LegacyPaymentGateway` надає `submitTransaction(String reference, long amountInCents)`.

**Рішення:** `LegacyPaymentAdapter implements PaymentProcessor` містить `LegacyPaymentGateway` (композиція) і перетворює долари в центи, назву методу й аргументи. Гроші конвертуються через `BigDecimal` з округленням до найближчого цента (HALF_UP); валідуються некоректні суми, а вихід за межі `long` призводить до винятку.

**Чому не змінити Legacy-клас?** Він моделює зовнішню бібліотеку, яка може не належати команді та мати інших клієнтів. Adapter дозволяє використовувати її без змін. Для нової інтеграції достатньо іншого класу, що реалізує `PaymentProcessor`; `RentalOrder` не змінюється.

## Список файлів
| Файл | Роль |
| --- | --- |
| `AbstractVehicle.java`, `Car.java`, `Serviceable.java` | Модель автомобіля із ЛР №4 |
| `PricingStrategy.java`, `DailyPricingStrategy.java`, `HourlyPricingStrategy.java` | Strategy із ЛР №4 |
| `PricingStrategyFactory.java`, `DailyPricingFactory.java`, `HourlyPricingFactory.java` | Factory Method |
| `RentalOrder.java`, `RentalStatus.java`, `OrderObserver.java`, `OrderLogObserver.java`, `CustomerNotificationObserver.java` | Subject, стан та Observer |
| `PaymentProcessor.java`, `LegacyPaymentGateway.java`, `LegacyPaymentAdapter.java` | Цільовий API, сторонній API та Adapter |
| `Main.java` | Демонстрація трьох паттернів та Strategy |
| `RentalSystemTest.java` | Тести без зовнішніх бібліотек |

## Запуск (JDK 11+)
У терміналі перейдіть до папки із `.java`-файлами й виконайте:
```bash
javac -encoding UTF-8 *.java
java Main
```
Для автоматичних перевірок:
```bash
java RentalSystemTest
```
У Windows PowerShell та CMD команди ті самі. У VS Code відкрийте папку з файлами й запустіть `Main.java` (Extension Pack for Java та JDK мають бути встановлені).

Очікуване: фабрика поверне різні стратегії (500$ та 50$ для 100$ / 5 одиниць); оренда Toyota з денною ставкою 50$ на 5 днів — 250$; погодинна зміна стратегії для 5 годин — 25$; після `processOrder` обидва Observer бачать `NEW -> PROCESSED`; Adapter передасть **2500 центів** за замовлення `ORD-001`, після цього Observer повідомлять `PROCESSED -> COMPLETED`. Друга оренда Toyota на 12 годин — 60$ (як у ЛР №4).

## Як доведено незмінність функціональності
- Вхідні формули і реалізації двох стратегій не змінені.
- Старий трьохпараметровий конструктор `RentalOrder(Car, int, PricingStrategy)` залишено.
- Раніше підтримувана зміна стратегії `setPricingStrategy` працює до завершення замовлення.
- `RentalSystemTest` перевіряє значення 500, 50 і 25, роботу фабрики, двох спостерігачів, відписку, ізоляцію винятків, конвертацію 25$ у 2500 центів, завершення та невдалу оплату.
- **Свідома відмінність:** додані стани замовлення, перевірка додатної тривалості та заборона змін після завершення. Це нові бізнес-обмеження, не просто механічний рефакторинг. Додано також демонстраційну інтеграцію оплати.

## Git / PR
Виконуйте команди **в корені вашого існуючого репозиторію**, а не створюйте новий репозиторій:
```bash
git switch main
git pull
git switch -c lr5/refactoring-patterns
# перенесіть ці .java і README.md у потрібну директорію поточного репозиторію
git add .
git commit -m "Lab 5: refactor car rental with Factory Method, Observer and Adapter"
git push -u origin lr5/refactoring-patterns
```
На GitHub відкрийте **Pull Request**: `lr5/refactoring-patterns` → `main` до дедлайну. Ці команди та цей архів **не створюють PR автоматично**.

## Коротко для захисту
1. **Рефакторинг** — покращення внутрішньої структури, без зміни наявних результатів. Показати однакові формули й тести. Новий платіжний сценарій і стани — окремі розширення.
2. **Factory Method** — створення стратегій через абстрактний метод Creator, реалізацію якого обирає підклас; `forType` — централізований диспетчер, подібний до Simple Factory.
3. **Observer** — підписники отримують події через інтерфейс, `RentalOrder` не знає їхніх конкретних класів.
4. **Adapter** — перетворює метод і суми; `LegacyPaymentGateway` залишився незмінним.
5. **Поєднання** — фабрика створює Strategy для замовлення, Observer слухає зміну статусу замовлення, Adapter забезпечує оплату для переходу в `COMPLETED`.
6. **Де не застосовано паттерн:** для `Car` один тип транспорту, тому додаткова фабрика автомобілів поки не має практичного сенсу.
