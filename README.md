# Календарь 2026 — Android-клиент

Тонкая WebView-обёртка вокруг веб-клиента календаря.
Приложение открывает сервер календаря и работает полностью через него.

## Адрес сервера

Зашит в `app/src/main/java/ru/xapepam/calendar/MainActivity.java`:

```java
private static final String SERVER_URL = "http://192.168.10.204:8081/";
```

Рассчитано на работу **только в локальной сети**. HTTP (не HTTPS) разрешён через
`android:usesCleartextTraffic="true"` в манифесте.

## Сборка

Локально нужны **JDK 17** и **Android SDK** (через Android Studio он подтягивается сам).

Из Android Studio: открыть папку проекта → Run / Build → Build Bundle(s) / APK(s) → Build APK(s).

Из командной строки:

```bash
./gradlew assembleDebug
```

Готовый APK: `app/build/outputs/apk/debug/app-debug.apk`.

Установка на телефон (по USB, включена отладка):

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Что умеет

- Открывает веб-клиент календаря (`/`).
- Показывает прогресс загрузки и панель ошибки, если сервер недоступен,
  с кнопкой «Повторить».
- При возврате на экран перечитывает состояние (`loadState`), чтобы видеть
  изменения, сделанные с другого устройства.
- Кнопка «Назад» внутри WebView работает как навигация назад.
