# MT Manager Pro

מנהל קבצים לאנדרואיד בהשראת MT Manager - עם דגש על:
1. **שמירת שמות קבצים מקוריים** בזמן חילוץ/דחיסת ZIP (כולל עברית/יוניקוד)
2. **חיפוש חכם** - מוצא קבצים קשורים/כפולים בכל המכשיר, גם לפי hash וגם לפי שם
3. **פתיחה עם אפליקציה מותקנת** - שיתוף/פתיחת קבצים דרך בורר האפליקציות של אנדרואיד
4. **עריכה ובנייה מחדש** - פותחים ZIP גדול, עורכים קבצים בפנים, ולוחצים "סיום" לקבלת ZIP מוכן

## מבנה הפרויקט (multi-module)

```
mt-manager-pro/
├── app/                    # האפליקציה הראשית (UI, Activities)
├── core-fileindex/         # אינדקס קבצים עם Room (SQLite)
├── core-search/            # לוגיקת החיפוש החכם
└── feature-editor/         # עורך טקסט/קבצים
```

## למה זה פותר את בעיית "השם נחתך"?

`ZipInputStream` הרגיל של Java לא תמיד מפרש נכון את דגל ה-UTF-8 בכותרת
ה-ZIP entry, מה שגורם לשמות עם עברית/תווים מיוחדים להיחתך או להתעוות.
הפרויקט משתמש בספריית **zip4j** עם `Charset.forName("UTF-8")` מפורש -
זה שומר על השם המקורי המדויק, גם בעת חילוץ וגם בעת דחיסה מחדש.

ראו: `app/src/main/java/com/mtmanagerpro/zipeditor/ZipExtractor.kt`

## איך עובד החיפוש החכם

1. שירות רקע (`WorkManager`) סורק את המכשיר ובונה טבלת אינדקס
   (נתיב, שם, hash, גודל) בטבלת Room/SQLite
2. כשעורכים קובץ, `SmartFileSearch` מחפש קבצים קשורים:
   - לפי **hash זהה** → עותקים מדויקים גם אם השם שונה
   - לפי **שם דומה** → LIKE query, אפשר לשדרג בעתיד ל-FTS4/FTS5 לחיפוש fuzzy מלא

ראו: `core-search/src/main/java/com/mtmanagerpro/search/SmartFileSearch.kt`

## זרימת עריכת ZIP מקצה לקצה

```
בחירת קובץ ZIP
      │
      ▼
ZipExtractor.extract()  →  חילוץ לתיקיית cache עם שמות מקוריים
      │
      ▼
EditorActivity  →  עריכת קובץ/ים בתוך הארכיון
      │
      ▼
ZipExtractor.repack()  →  בניית ZIP חדש, מוכן להורדה/שיתוף/התקנה
```

## הרצה

1. פתחו את התיקייה ב-Android Studio (Hedgehog ומעלה)
2. Sync Gradle
3. Run על מכשיר/אמולטור עם API 24+

## מה עוד חסר (TODO להמשך פיתוח)

- [ ] Layout XML מלא למסכי MainActivity/EditorActivity (RecyclerView לרשימת קבצים)
- [ ] עורך הקסה (Hex editor) לקבצים בינאריים
- [ ] שדרוג חיפוש לפי FTS4/FTS5 עבור fuzzy matching אמיתי
- [ ] מודול נפרד ל-decompile/recompile APK (REAndroid/ARSCLib) - זהו פיצ'ר
      מתקדם ומורכב יותר, מומלץ להוסיף כמודול נפרד אחרי שה-MVP עובד
- [ ] חתימת APK (v2/v3 signing) + zipalign לפני התקנה מחדש
- [ ] בקשת הרשאות runtime (MANAGE_EXTERNAL_STORAGE) בזמן ריצה

## רישיון

MIT - שנו לפי הצורך.
