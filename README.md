# Лаб 9: Код хамралт — JaCoCo-гоор тестээ хөтлөх

F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест (2026)

- Нэр: TODO_NAME
- Оюутны код: TODO_STUDENT_CODE

## Орчин

`java -version`:

```
java version "17.0.12" 2024-07-16 LTS
Java(TM) SE Runtime Environment (build 17.0.12+8-LTS-286)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.12+8-LTS-286, mixed mode, sharing)
```

`mvn -version`:

```
Apache Maven 3.10.0 (c43a36b8d67be7e0805a411bc0898af1a51f5472)
Maven home: C:\Users\erhem\Documents\Batalgaa\apache-maven-3.10.0
Java version: 17.0.12, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-17
Default locale: en_US, platform encoding: Cp1252, time zone: Asia/Ulaanbaatar
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## Хэсэг A: Happy path тест ба эхний хэмжилт

5 happy-path тест бичсэн (`letterGradeA`, `letterGradeB`, `gpaTwoGrades`, `scholarshipFull`, `scholarshipHalf`).
Үр дүн (`results/jacoco-A.csv`, `results/mvn-verify-A.txt`: `Tests run: 5`):

| Хэмжүүр | Хамрагдсан | Дутуу | Хувь |
|---|---|---|---|
| Branch | 20 | 21 | 20/41 ≈ 48.8% |
| Line | 16 | 9 | 16/25 = 64% |

Ажиглалт: шар ромбо бүхий мөрүүд нь зөвхөн нэг талдаа хамрагдсан нөхцөлүүд, жишээ нь `letterGrade`-ийн
хүрээний шалгалт, `scholarship`-ийн `hasViolation` ба `&&` нөхцөлүүд. Улаан (огт хүрээгүй) мөрүүд нь
`IllegalArgumentException` шидэх бүх замууд, `letterGrade`-ийн C/D/F буцаалт, `gpa`-ийн `switch`-ийн C, D,
default тохиолдол, `scholarship`-ийн эцсийн `return 0`. Функц тус бүрийн Branch хамралт: `letterGrade` 41%,
`gpa` 54%, `scholarship` 50%.

## Хэсэг B: CFG ба Branch 100%

`scholarship()`-ийн CFG-г draw.io дээр өөрөө зурсан: `cfg.png`. Ирмэг бүрийг дайрах тест:

| Ирмэг | Тест |
|---|---|
| `gpa < 0` True → throw | `scholarshipNegativeGpaThrows` |
| `gpa > 4` True → throw | `scholarshipGpaAbove4Throws` |
| `credits < 0` True → throw | `scholarshipNegativeCreditsThrows` |
| `hasViolation` True → 0 | `scholarshipViolationZero` |
| `gpa >= 3.8` False → `gpa >= 3.4` | `scholarship70` |
| `credits >= 15` True → 100 | `scholarshipFull` |
| `credits >= 15` False → `gpa >= 3.4` | `scholarshipHighGpaFewCredits70` |
| `credits >= 12` True → 70 | `scholarship70` |
| `credits >= 12` False → `gpa >= 3.0` | `scholarshipGpa34FewCredits50` |
| `gpa >= 3.4` False → `gpa >= 3.0` | `scholarshipHalf` |
| `gpa >= 3.0` True → 50 | `scholarshipHalf` |
| `gpa >= 3.0` False → 0 | `scholarshipLowGpaZero` |

`letterGrade` ба `gpa`-д мөн `letterGradeC/D/F`, `letterGradeNegativeThrows`, `letterGradeAbove100Throws`,
`gpaAllGradeKinds`, `gpaNullThrows`, `gpaEmptyThrows` тестүүдийг нэмсэн.

Нийт **21 тест** шаардагдсан (`results/mvn-verify-B.txt`: `Tests run: 21`).
`results/jacoco-B.csv`-д `BRANCH_MISSED = 0` (41/41), `LINE_MISSED = 0` (25/25).

## Хэсэг C: Давталтын заагийн тест (0 / 1 / 2+)

| Давталт | Тест |
|---|---|
| 0 удаа | `gpaEmptyThrows`, `gpaNullThrows` (`assertThrows`) |
| 1 удаа | `gpaSingleGrade`: `gpa([95]) == 4.0` |
| 2+ удаа | `gpaManyGrades`: `gpa([95, 85, 75, 65]) == 2.5` |

Эдгээр тест хамралтын тоог өсгөөгүй, учир нь Branch хамралт аль хэдийн 100% байсан. Гэхдээ хамрал нь давталт
ажилласан эсэхийг л хэмждэг тул "яг нэг удаа" ба "олон удаа" ажиллах үеийн алдааг (нэгээр зөрөх, нийлбэрийг
буруу хуримтлуулах) зөвхөн эдгээр тест барина.

## Хэсэг D: Хамралт 100% — гэхдээ алдаа амьд

Зааг утгын 7 тест нэмсэн: `letterGrade(0, 59.99, 60, 69.99, 89.99, 90, 100)`. Нийт `Tests run: 30`.

`results/mvn-test-D-fail.txt`: нэг тест унасан (`Tests run: 30, Failures: 1`):

```
[ERROR] ScholarshipCalculatorTest.letterGradeBoundary60:144 expected: <D> but was: <F>
```

Алдаа: `letterGrade()` дотор `if (score > 60) return "D";` гэж байсан, спецификацид 60 нь "D" тул `>= 60`
байх ёстой. Засвар тусдаа commit-оор хийгдсэн: `Fix letterGrade: > 60 -> >= 60, spec says 60 is D`.
Засвараас хойш бүх 30 тест ногоон (`results/mvn-verify-final.txt`), `results/jacoco-final.csv`-д
`BRANCH_MISSED = 0`, `LINE_MISSED = 0`.

### Гол дүгнэлт

Branch хамралт 100% байсан ч `score > 60` гэсэн алдаа тестэд баригдаагүй. `> 60` ба `>= 60` нь яг ижил
салаануудыг (үнэн/худал) үүсгэдэг тул JaCoCo хоёуланг нь "хамрагдсан" гэж тооцдог, ялгаа нь зөвхөн 60 гэсэн
заагийн утгад л гарна. Миний тестүүд 65, 30 гэх мэт заагаас хол утгуудыг л шалгасан тул алдаа "үл үзэгдэгч"
байсан. `letterGrade(60)` тест `expected: <D> but was: <F>` гэж унаж алдааг илрүүлсэн. Хамрал нь аль мөр, салаа
ажилласныг хэмждэг боловч буцаасан утга зөв эсэхийг, ялангуяа зааг утгыг шалгадаггүй. Иймд хамрал бол зайлшгүй
хэрэгсэл боловч хангалттай нотолгоо биш, зааг утгын тест хамт хэрэгтэй.

## AI ашигласан тухай

AI туслах (Claude) ашигласан: (1) лабын заавар, starter төслийг уншиж алхам алхмаар чиглүүлсэн; (2) тестийн
файлын ноорог (A, B, C, D хэсгийн тестүүд) бичсэн; (3) миний зурсан CFG-г хянаж алдааг (`return 75`,
`credits` ромбуудын False сум) зааж өгсөн; (4) `letterGradeBoundary60` унасны дараа `> 60` мөрийг заасан;
(5) энэ README-ийн ноорогийг бичсэн. `mvn` командуудыг би өөрөө ажиллуулж, `results/` дахь бүх файл миний
машины бодит гаралт. CFG-г draw.io дээр би өөрөө зурсан.