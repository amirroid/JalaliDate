# JalaliDate

[![Maven Central](https://img.shields.io/maven-central/v/io.github.amirroid/jalalidate?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.amirroid/jalalidate) ![Android](https://img.shields.io/badge/Android-3aab58?logo=android&logoColor=white) ![iOS](https://img.shields.io/badge/iOS-d32408?logo=apple&logoColor=white) ![Desktop](https://img.shields.io/badge/Desktop-097cd5) ![JS](https://img.shields.io/badge/JS-f7e025?logo=javascript&logoColor=black) ![Wasm](https://img.shields.io/badge/Wasm-00599C?logo=webassembly&logoColor=white)

📆 **JalaliDate** is a Kotlin Multiplatform library for handling Persian (Jalali) dates, with support
for formatting, parsing, and calendar conversions.

---

## Features

- Supports Kotlin Multiplatform (`android`, `jvm`, `ios`, `js`, `wasmJs`)
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
    dayTwoDigit()
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

| Symbol | Description         | Example (value)     |
|--------|---------------------|---------------------|
| `yyyy` | Full year           | `1402`              |
| `yy`   | Last two digits     | `02`                |
| `MMMM` | Full month name     | `مرداد` / `Mordad`  |
| `MMM`  | Short month name    | `مرد` / `Mor`       |
| `MM`   | 2-digit month       | `05`                |
| `M`    | 1 or 2-digit month  | `5`                 |
| `EEEE` | Full weekday name   | `شنبه` / `Saturday` |
| `EEE`  | Short weekday name  | `ش` / `Sat`         |
| `E`    | Weekday number      | `1`                 |
| `dd`   | 2-digit day         | `09`                |
| `d`    | 1 or 2-digit day    | `9`                 |
| `HH`   | Hour (24h, 2-digit) | `14`                |
| `H`    | Hour (24h)          | `14`                |
| `mm`   | Minutes (2-digit)   | `03`                |
| `m`    | Minutes             | `3`                 |
| `ss`   | Seconds (2-digit)   | `07`                |
| `s`    | Seconds             | `7`                 |

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

> **Note:**  
> You can change the `locale` in two ways:
>- **Globally** (the default locale is Persian):
>```kotlin
>   JalaliDateGlobalConfiguration.formatterLocale = Locale.ENGLISH
>```  
> - **Locally** when formatting a date:
>```kotlin
>   date.format {
>       applyLocale(Locale.ENGLISH)
>       byUnicodePattern("MMMM yyyy")
>   }
>```


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

---

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

<details>
<summary>Using plus and minus operators with DateTimeInterval</summary>

```kotlin
val date = JalaliDateTime(1402, 5, 10, 12, 30, 15)
date + 5.days - 2.months // JalaliDateTime with Jalali date 1402/03/15 and time 12:30:15
date - 1.years + 10.days // JalaliDateTime with Jalali date 1401/05/20 and time 12:30:15
```

</details>

---

<details>
<summary>Using copyGregorian and copyJalali</summary>

```kotlin
JalaliDateTime(1402, 5, 10, 14, 45, 30).copyGregorian(year = 2023, month = 8, day = 1)
// Returns JalaliDateTime corresponding to Gregorian 2023/08/01

JalaliDateTime(1402, 5, 10, 14, 45, 30).copyJalali(year = 1403, month = 6, day = 20)
// JalaliDateTime(year=1403, month=6, day=20, hour=14, minute=45, second=30)
```

</details>

---

<details>
<summary>Main properties of JalaliDateTime</summary>

```kotlin
val date = JalaliDateTime(1402, 7, 15, 10, 20, 30)

date.gregorianYear // 2023
date.gregorianMonth // 10
date.gregorianDay // 7

date.jalaliYear // 1402
date.jalaliMonth // 7
date.jalaliDay // 15

date.algorithm::class.simpleName // "KhayyamAlgorithm" or "BirashkAlgorithm"

date.hour // 10
date.minute // 20
date.second // 30

date.weekOfYear // 29
date.isJalaliLeapYear // false
date.isGregorianLeapYear // false

date.monthLength // 30
date.monthName.english // "Mehr"
date.monthName.persian // "مهر"

date.dayOfWeek() // DayOfWeek.SATURDAY
date.dayOfWeek(weekStartDay = DayOfWeek.SUNDAY) // DayOfWeek.SATURDAY
date.dayOfWeekNumber() // 1 (if week starts on Saturday)
```

</details>

---

## 📜 License

MIT

---

For any suggestions or issues, feel free to open
an [Issue](https://github.com/amirroid/JalaliDate/issues) or contribute via PR.