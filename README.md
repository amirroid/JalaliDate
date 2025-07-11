# JalaliDate [![Maven Central](https://img.shields.io/maven-central/v/io.github.amirroid/jalalidate?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.amirroid/jalalidate) ![Platform](https://img.shields.io/badge/Android-3aab58) ![Platform](https://img.shields.io/badge/Desktop-097cd5) ![Platform](https://img.shields.io/badge/IOS-d32408) ![Platform](https://img.shields.io/badge/JS-f7e025)

📆 A Kotlin Multiplatform library for handling Persian (Jalali) dates with formatting and parsing
support.
---

## Features

- Supports Kotlin Multiplatform (`android`, `jvm`, `ios`, `js`, etc.)
- Accurate conversion between Jalali and Gregorian calendars
- Two built-in calendar conversion algorithms
- Powerful and flexible formatting and parsing system
- Inspired by `SimpleDateFormat` but tailored for Jalali date logic

---

## 📦 Installation

```kotlin
// build.gradle.kts

sourceSets {
    val commonMain by getting {
        dependencies {
            implementation("io.github.amirroid:jalalidate:x.y.z")
        }
    }
}
```

---

## 🔁 Conversion Algorithms

This library includes **two built-in algorithms** for converting between Jalali and Gregorian
calendars:

| Algorithm Name     | Description                                                                                |
|--------------------|--------------------------------------------------------------------------------------------|
| `BirashkAlgorithm` | Based on the astronomical method by Dr. Ahmad Birashk — accurate and reliable              |
| `KhayyamAlgorithm` | Based on Khayyam–Jalali calendar rules — lightweight and approximate (used as the default) |

You can change the **default algorithm globally**:

```kotlin
JalaliDateGlobalConfiguration.convertAlgorithm = BirashkAlgorithm
```

Or specify per-instance:

```kotlin
val date =
    JalaliDateTime(jalaliYear = 1402, jalaliMonth = 5, jalaliDay = 9, algorithm = BirashkAlgorithm)
```

---

## 🛠️ Formatting

Format any `JalaliDateTime` instance using pattern strings:

```kotlin
val date = JalaliDateTime(1402, 5, 9, 14, 3, 7)
val formatted = date.format {
    byUnicodePattern("yyyy/MM/dd HH:mm:ss")
}
println(formatted) // 1402/05/09 14:03:07
```

You can also use manual formatting:

```kotlin
val formatted = date.format {
    year()
    chars("/")
    month()
    chars("/")
    day()
}
```

---

## ⏱️ Parsing

Parse back a string into a `JalaliDateTime`:

```kotlin
val formatter = JalaliDateTime.Format { byUnicodePattern("yyyy-MM-dd HH:mm:ss") }
// Or
val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy MMM dd")
val date = formatter.parse("1402/05/09")
println(date.jalaliYear) // 1402
```

Any missing field (like time) will default to current system time.

---

## 🧾 Pattern Reference

| Symbol | Description         | Example (value) |
|--------|---------------------|-----------------|
| `yyyy` | Full year           | `1402`          |
| `yy`   | Last two digits     | `02`            |
| `MMMM` | Full month name     | `مرداد`         |
| `MMM`  | Short month name    | `مرد`           |
| `MM`   | 2-digit month       | `05`            |
| `M`    | 1 or 2-digit month  | `5`             |
| `dd`   | 2-digit day         | `09`            |
| `d`    | 1 or 2-digit day    | `9`             |
| `HH`   | Hour (24h, 2-digit) | `14`            |
| `H`    | Hour (24h)          | `14`            |
| `mm`   | Minutes (2-digit)   | `03`            |
| `m`    | Minutes             | `3`             |
| `ss`   | Seconds (2-digit)   | `07`            |
| `s`    | Seconds             | `7`             |

You can also combine static characters using `chars(...)`:

```kotlin
val formatter = JalaliDateTimeFormatter()
    .year()
    .chars(" year - ")
    .dayOneDigit()
    .chars(" - ")
    .monthFullName()
```

Output:

```
1402 year - مرداد - 9
```

---

## 📚 Examples

<details>
<summary>Format with month name</summary>

```kotlin
val date = JalaliDateTime(1402, 5, 9)
val formatted = date.format {
    byUnicodePattern("yyyy dd MMMM")
}
// Output: 1402 مرداد 09
```

</details>

<details>
<summary>Parse with short month name</summary>

```kotlin
val input = "1402 مرد 09"
val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy dd MMM")
val date = formatter.parse(input)
println(date.jalaliMonth) // 5
```

</details>

---

## 📜 License

MIT

---

For any suggestions or issues, feel free to open
an [Issue](https://github.com/amirroid/JalaliDate/issues) or contribute via PR.