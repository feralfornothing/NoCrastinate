# NoCrastinate - Dokumentacja Techniczna

## 1. Opis aplikacji
### Cel: NoCrastinate służy do zwiększenia produktywności, zarządzania listą zadań i przeglądania osiągniętych statystyk.
### Grupa docelowa: uczniowie i studenci (organizacja zadań domowych, czasu wolnego i egzaminów); osoby aktywne zawodowo, chcące uporządkować swoje zadania. 
### Funkcjonalności: tworzenie, edycja i usuwanie zadań; ustawianie przypomnień z powiadomieniami push; śledzenie statystyk zadań ukończonych; przejrzysty interfejs; lokalne przechowywanie danych; responsywny layout.

## 2. Wybrana technologia
### Język programowania: Kotlin - oferuje czytelną, zwięzłą składnię, bazując na istniejących bibliotekach Java.

## 3. Architektura aplikacji

### Schemat struktury projektu

**Struktura katalogów:**

- **app/src/main/java/com/example/nocrastinate/**
  - **ui/** - Fragmenty i Activities
    - `MainActivity.kt`
    - `TasksFragment.kt`
    - `CompletedFragment.kt`
    - `SettingsFragment.kt`
  
  - **adapters/** - Adaptery RecyclerView
    - `TaskAdapter.kt`
  
  - **models/** - Modele danych
    - `Task.kt`
  
  - **data/** - Warstwa dostępu do danych
    - `TaskManager.kt`
  
  - **utils/** - Klasy pomocnicze
    - `NotificationHelper.kt`
    - `SwipeToDeleteHelper.kt`
  
  - **receivers/** - Broadcast Receivers
    - `NotificationReceiver.kt`

- **app/src/main/res/** - Zasoby
  - `layout/` - Pliki XML layoutów
  - `navigation/` - Graf nawigacji
  - `menu/` - Menu bottom navigation
  - `values/` - Kolory, wymiary, stringi
  - `drawable/` - Grafiki i drawable
  - `mipmap-*/` - Ikony aplikacji

## 4. Opis zaimplementowanych funkcjonalności
- 1 - dodawanie zadań - implementacja z zastosowaniem `TasksFragment.kt` oraz `add.setOnClickListener`, zapisanie do `active_tasks.json`.
- 2 - usuwanie zadań - zastosowanie `SwipeToDeleteHelper.kt` i wyróżnienie czerwonym tłem podczas usuwania czynności.
- 3 - oznaczanie zadań jako ukończonych - zastosowany na liście checkbox przenosi daną czynność z listy `active_tasks.json` do `completed_tasks.json`.
- 4 - ustawianie przypomnień za pomocą `NotificationHelper.kt` oraz `NotificationReceiver.kt`.
- 5 - wizualne rozróżnienie kategorii  "zadania", "ukończone" oraz "statystyki". 
- 6 - responsywność - wspieranie orientacji pionowej i poziomej, rozróżnione wymiary dla różnych rozmiarów wyświetlaczy urządzeń mobilnych.
- 7 - dostępność - zachowanie normy kontrastu według WCAG 2.1 oraz minimalnego rozmiaru 48dp dla klikalnych elementów interfejsu.

## 5. Zrzuty ekranu - do uzupełnienia.

## 6. Napotkane problemy
- 1 - zbędne powtórzenia kodu dotyczącego funkcjonalności swipe-to-delete - wyeliminowane przez stworzenie nowego pliku Kotlin 'SwipeToDeleteHelper.kt'.
- 2 - stworzenie oprogramowania responsywnego - modyfikacja layoutu dla tabletów 7" (values/values-sw600dp/dimens.xml) oraz 10" (values/values-sw720dp/dimens.xml).
- 3 - utworzenie layoutu dla orientacji poziomej (/layout_land/fragment_tasks.xml).

## 7. Możliwości rozwoju
- 1 - podział zadań na kategorie.
- 2 - wprowadzenie alternatywnych motywów kolorystycznych.
- 3 - zaawansowane śledzenie i/lub eksportowanie statystyk.
- 4 - synchronizacja w chmurze, dla współdzielenia zadań między tabletemm a smartfonem.
- 5 - integracja z aplikacjami systemowymi (iCal, Google Calendar).

