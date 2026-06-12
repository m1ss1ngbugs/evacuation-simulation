*Przeczytaj w innych językach: [English](README.md)*

# Symulator Ewakuacji Budynku

## Spis treści

* [Informacje ogólne](#informacje-ogólne)
* [Główne funkcjonalności](#główne-funkcjonalności)
* [Technologie](#technologie)
* [Jak uruchomić?](#jak-uruchomić?)
* [Bardziej szczegółowe informacje o modułach](#bardziej-szczegółowe-informacjep-o-modułach)
* [Widok aplikacji](#widok-aplikacji)
* [Autorzy](#autorzy)
* [Prowadzący](#prowadzący)

## Informacje ogólne
<details>
<summary>Kliknij tutaj, aby zobaczyć ogólne informacje o <b>Projekcie</b>!</summary>
<b>Zaawansowany symulator ewakuacji tłumu z zamkniętych pomieszczeń. </b>.
Aplikacja modeluje zachowania ludzi w czasie ewakuacji z budynku z dynamicznie rozpowszechniającymi zagrożeniami (pożar, zadymienie)
z wykorzystaniem sztucznej inteligencji napisanej dla różnych typów agentów, maszyn stanów oraz algorytmów szukania drogi. 
Projekt posiada rozbudowany interfejs graficzny pozwalający na konfigurację kilkudziesięciu parametrów w czasie rzeczywistym.
</details>

## Główne funkcjonalności

* **Strategia nawigacji A*:** Agenci wykorzystują algorytm A* do znajdowania najkrótszej drogi do wyjścia. Dynamicznie oceniają koszt przejścia przez strefy
  zagrożone w razie blokady przejścia przez jakiekolwiek zagrożenie czy przeszkodę.
* **Trzy profile psychologiczne agentów:**
  * **Liderzy (Leaders):** Odporni na panikę, wyznaczają optymalne ścieżki ucieczki.
  * **Zwykli (Followers):** Podążają za liderami, reagują na tłum (czynnik stadny).
  * **Spanikowani (Panicked):** Podejmują irracjonalne decyzje i poruszają się chaotycznie, gdy poziom stresu przekroczy pewną granicę.
* **Dynamiczne Zagrożenia:** Model propagowania się ognia i dymu są dynamiczne i zbliżone do rzeczywistości, zadają obrażenia i ograniczają pole widzenia.
* **GUI:** Interfejs jest przyjazny dla użytkownika, pozwala zadać aż 22 początkowe parametry symulacji,
  od ilości agentów początkowych różnych typów do ich różnych charakterystyk. Pozwala też na załadowanie swojej własnej mapy z pliku tekstowego.
  Pozwala to na symulowanie różnych scenariuszy ewakuacji z różnych budynków.
* **Statystyki na żywo:** Symulacja na bieżąco zbiera różne statystyki, co pozwala na analizę przeprowadzonej ewakuacji. Pozwala też na zgienerowanie heatmapy
  (jest tworzona automatycznie po zakończeniu symulacji).

## Technologie

Projekt został zbudowany przy użyciu następujących technologii:
* **Java** (wersja 21)
* **JavaFX** (wersja 21 / interfejs projektowany w SceneBuilder)
* **Gradle** (narzędzie do budowania projektu)

## Jak uruchomić?

Aby uruchomić symulację na swoim lokalnym komputerze, postępuj zgodnie z poniższymi krokami:

1. **Sklonuj repozytorium:**
   ```commandline
   git clone [https://github.com/TwojaNazwa/evacuation-simulation.git](https://github.com/TwojaNazwa/evacuation-simulation.git)
   ```
2. **Przejdź do folderu z projektem:**
   ```commandline
   cd evacuation-simulation
   ```
3. **Uruchom projekt za pomocą Gradle:**
  * Na systemie Windows:
  ```commandline
  gradlew.bat run
  ```
  * Na systemie Linux / macOS:
  ```commandline
  ./gradlew run
  ```

Uwaga!
Do symulacji można załadować swoją mapę w postaci zwykłego pliku tekstowego. Taki plik musi być w postaci pliku z symbolami, gdzie '0' - przeszkoda, 
'#' - ściana, 'E' - wyjście, każdy inny symbol jest traktowany jako podłoga.
(przykładowe mapy dodane do projektu: "mapa1.txt", "mapa2.txt", "mapa3.txt", "mapa4.txt", "map_test.txt")

## Bardziej szczegółowe informacje o modułach
* src.main.java.evacuation.sim. (główny, jedyny moduł projektu)
  * agent. (pakiet agentów symulacji)
    * hazard.
      * Fire (agent, główne zagrożenie zabijające ewakuantów)
      * Hazard (klasa abstrakcyjna, jest rozszerzana przez Fire i Smoke)
      * Smoke (agent, zagrożenie wtórne zmniejszające widoczność i słabo zadające obrażenia)
    * human.
      * Evacuee (klasa abstrakcyjna rozszerzana przez poniższych ewakuantów w pakiecie)
      * Follower (agent, ewakuant, ucieka razem z tłumem)
      * Leader (agent, ewakuant, odporni na panikę, prowadzą za sobą)
      * Panicked (agent, ewakuant, łatwiej wpadają w panikę)
    * Agent (klasa abstrakcyjna, którą rozszerzają wszystkie klasy odpowiadające za agentów w symulacji)
    * Damageable (interfejs, który implementują agenci, które muszą otrzymywać obrażenia)
    * Desk (agent, tworzy przeszkodę na drodze ewakuantów, może być zniszczony przez zagrożenia)
  * core. (główny pakiet, serce symulacji)
    * Simulation (główna klasa, łączy całą symulację)
    * Statistics (klasa zbierająca dane do analizy z całej symulacji)
  * event. (pakiet z observerem)
    * SimEvent (pomocnicza klasa dla przesyłania komunikatów)
    * SimObserver (interfejs implementowany przez nasłuchiwaczy)
    * SimSubject (interfejs implementowany przez nadajniki)
  * factory. (fabryka agentów)
    * AgentFactory (klasa-fabryka, odpowiada za tworzenie nowych agentów za pomocą ich builderów)
    * AgentRandomizer (klasa instrumentalna losującal parametry dla tworzonych agentów)
  * gui. (interfejs graficzny symulacji)
    * GuiApplication (uruchamia GUI, tworzy główne okno)
    * SimulationController (klasa odpowiadająca za logikę dla elementów GUI (zbieranie danych wejściowych ze sliderów, wyświetlanie statystyk i podobne))
  * model. (model świata (MVC))
    * BaseType (enum, odpowiada za podstawowy typ komórki)
    * DynamicState (enum, odpowiada za typ komórki w zależności od zagrożenia)
    * Direction (enum, odpowiada za przechowywania możliwych kierónków na komurki sąsiadujące)
    * Board (klasa odpowiadająca za kompozycję komórek, główna plansza symulacji, przechowywuję mapę i współrzędne agentów)
    * Cell (klasa komórki - podstawowy element planszy)
  * routing. (część kodu odpowiedzialna za strategii poruszania się)
    * PathfindingStrategy (interfejs odpowiadający za dostęp do różnych strategii znalezienia drogi)
    * AStarPathfinder (klasa odpowiadająca za bezpośredni algorytm znalezienia drogi)
* src.main.java.resources.
  * main_layout.fxml (plik typu .xml dla JavaFX odpowiadający za layout i style GUI)

## Widok aplikacji

![Interfejs GUI](app/assets/GuiInterface.png)
*Główny panel konfiguracyjny z suwakami.*

![Symulacja](app/assets/Simulation.png)
*Ewakuacja agentów w trakcie pożaru.*

## Autorzy

Projekt realizowany w ramach zajęć laboratoryhnych z Programowania Obiektowego.
    
    Heorhii Yartsev (293562)

    Bartłomiej Krajewski (******)

## Prowadzący

    dr inż. Paweł Majewski
    pawel.majewski@pwr.edu.pl
