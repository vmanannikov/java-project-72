[![Actions Status](https://github.com/vmanannikov/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/vmanannikov/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/c2bc58d574eae321bc4b/maintainability)](https://codeclimate.com/github/vmanannikov/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/c2bc58d574eae321bc4b/test_coverage)](https://codeclimate.com/github/vmanannikov/java-project-72/test_coverage)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://raw.githubusercontent.com/casid/jte/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/gg.jte/jte.svg)](https://central.sonatype.com/artifact/gg.jte/jte?smo=true)
[![Javalin](https://img.shields.io/maven-central/v/io.javalin/javalin?label=stable)](https://central.sonatype.com/artifact/io.javalin/javalin)

## Анализатор страниц

Анализатор страниц - веб-приложение, реализованное с использованием фреймворка [Javalin](https://javalin.io). Реализует принципы современной разработки веб-сайтов с использованием архитектуры MVC: обработка маршрутизации, обработчиков запросов и шаблонов JTE, а также взаимодействие с базой данных через ORM.

Пример работы [приложения](https://urls-checker.onrender.com)

### Технологии:

+ Java
+ Javalin
+ H2, PostgreSQL
+ Lombok
+ JUnit

## Требования:

### Для запуска приложения необходимо установить следующие зависимости:

+ JDK 21
+ Gradle 8.5
+ Javalin 6.1.3
+ Jte 3.1.10
+ Makefile

## Запуск локально

Клонируем проект

```bash
  git clone https://github.com/vmanannikov/java-project-72
```

Переходим в каталог проекта

```bash
  cd java-project-72/app
```

Собираем приложение

```bash
  make build
```

Запускаем

```bash
  make run
```