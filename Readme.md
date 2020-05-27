**Nim-Spiel für das Modul 'Programmierung interaktiver Systeme SS2020'**
<br>von Till Nitsche, 26. Mai 2020
<br>
<br>
# Inhalte
- Bedienungsanleitung
- Besonderheiten
- Aufzeichnungen über die Konsoleninteraktion

<br>
<br>

# Bedienungsanleitung
Die Bedienung der Anwendung gestaltet sich relativ intuitiv und sollte am Besten einfach selbst ausprobiert werden. Trotzdem möchte ich einige Besonderheiten kurz erläutern.

Man kann lediglich die je nach Menü angegebenen Zeichensequenzen verwenden, um die entsprechende Aktion auszulösen. Auf falsche Eingaben wird hingewiesen oder sie werden einfach ignoriert. Eingaben sind <b>nicht</b> case-sensitive. Außerdem wird immer nur der Beginn der eingegebenen Zeichenfolge ausgewertet (startsWith...). 

Man kann jederzeit über `b` ein Menü in der Aufrufhierarchie zurückgehen. Das Spiel lässt sich außerdem aus jedem Zustand heraus durch `q` oder `e` beenden. 
<br>
<br>

## Hauptmenü:
Bei Anwendungsstart landet man im Hauptmenü. Von hier aus erreicht man alle Folgemenüs:

- `n` oder `np` öffnet das Gamesetup (Nim oder NimPerfect)
- `t` öffnet den Testmodus

## Gamesetup:
Hier kann ein neues Spiel initialisiert werden. Das Menü entspricht der Auswahl des Spiels aus dem Hauptmenü (Nim oder NimPerfect). Nach Eingabe der Einstellung landet man direkt im Game.

- `[ENTER]` startet ein Spiel mit der Voreinstellung 3-4-5, gemäß Abgabebedingung
- `number-number-number` startet ein Spiel mit eigenen Einstellungen. Dazu können mehrere Spielreihen, dargestellt als Zahl, mit `-` getrennt voneinander eingegeben werden. Es können theoretisch beliebig viele Reihen sein. Nur für das Spiel Nim gibt es eine Begrenzung (mehr im Kapitel 'Besonderheiten')
- `r` startet ein zufälliges Spiel mit 2-5 Reihen und 1-7 Elementen je Reihe

## Game:
Im Spiel werden abwechselnd Spielzüge getätigt. Dabei steht wahlweise der automatische Zug der AI zur Verfügung oder der manuelle Zug. Sollte ein Spiel beendet sein und die `[ENTER]` - Taste gedrückt werden dann wird, anders als nach Vorgabe, nicht das Spiel neu gestartet. Stattdessen wurde eine Reset-Funktion integriert.

- `[ENTER]` lässt die AI einen automatischen (besten) Zug machen
- `row.number` führt einen eigenen Zug durch. Dabei muss als Erstes die Reihe und dann die Anzahl der zu entfernenden Elemente angegeben mit dem Trennzeichen `.` angegeben werden.
- `u` macht den letzten Zug rückgängig. Falls versucht wird einen Zug in der Startkonstellation rückgängig zu machen, wird der Nutzer darauf hingewiesen und es passiert nichts.
- `r` setzt das Spiel auf den definierten Startzustand zurück

## Test:
Der Testmodus lässt die beiden Nim-Implementationen gegeneinander antreten. Dazu werden 40 Spiele gespielt, bei denen jedes Mal Nim gegen NimPerfect spielen. Der jeweilige Beginner der Runde wird per Zufall bestimmt. Während das Spiel läuft werden, wie gefordert, immer die Rundenergebnisse in der Konsole ausgegeben. Außerdem wird angezeigt, ob ein Spiel perfekt gespielt wurde (erwartetes Ergebnis wird durch NimPerfect ermittelt). Sollte ein Spiel 'falsch' laufen wird der Testlauf abgebrochen und eine entsprechende Meldung ausgegeben.

- `[ENTER]` lässt Nim gegen NimPerfect insgesamt 40 simulierte Spiele spielen und alle Ergebnisse ausgeben

<br>
<br>
<br>

# Besonderheiten
- Bei der Erstellung eines Spiels der Klasse Nim muss man beim selbstdefinierten Setup darauf achten, dass man das Board nicht zu groß wählt, sowohl in Anzahl der Reihen als auch Anzahl der Elemente. Die Klasse Nim kann sehr große Boards erstellen, die noch schnell laden (z.B. 40-40-20). Es gibt allerdings auch Boards mit sehr sehr langen Ladezeiten von einigen Minuten (z.B. 400-400-200). Darüber hinaus gibt es auch ungültige Eingaben, da Nim seine Boards als Hashcodes abspeichert, der die Boardgröße aufgrund der Codierung limitiert. Boardeingaben mit einer unmöglichen Größe (z.B. 4000-4000-2000) werden schon bei der Eingabe abgewiesen und mit einem entsprechenden Hinweis ausgestattet. Generell gilt: `ceil(ln(größtesElement)) < Integer.BIT_SIZE`

- Nim und NimPerfect sind beide <b>immutabel</b> umgesetzt
- Der Minimax-Algorithmus ist durch eine Implementierung mit Hashtable sehr schlank ausgefallen. Eine Art von Alpha-beta-pruning wurde dadurch eingebracht, dass das erste beste Ergebnis mit einem Filter in `firstOrNull` zurückgegeben wird und die restlichen Teilbäume nicht mehr durchsucht. Ich habe mich gegen eine generische Implementierung entschieden, die auch für TicTacToe funktioniert, da ich für beide Spiele eine, wie ich finde, viel coolere Variante gefunden habe :)

# Aufzeichnungen
Hier einige Aufzeichnungen aus der Interaktion mit der Anwendung in der Konsole:

<details>
    <summary>Hauptmenü:</summary>

    ++++++++++++++++++++
    nim main menu
    ++++++++++++++++++++

    please choose one of the following actions

    [n] = start game: nim           [np] = start game: nimPerfect
    [t] = launch test mode          [q/e] = quit

    -----------------------------

</details>

<details>
    <summary>Gamesetup: Nim</summary>

        Nim with the minimax algorithm!


        [ENTER]          Start a predefined game with the initial state 3-4-5
    [number-number-number]  Start a game with your own settings
            [r]            Start a random game with 2-5 rows and 1-7 sticks per row
            [b]            back to previous menu
            [q/e]           quit

    -----------------------------

</details>

<details>
    <summary>Gamesetup: NimPerfect:</summary>

    Nim with XOR - no chance for you


        [ENTER]          Start a predefined game with the initial state 3-4-5
    [number-number-number]  Start a game with your own settings
            [r]            Start a random game with 2-5 rows and 1-7 sticks per row
            [b]            back to previous menu
            [q/e]           quit

    -----------------------------

</details>

<details>
    <summary>Game:</summary>

    p2 turn
    I
    I I I I
    I I I I I

    [ENTER]     ai makes a best move      [row.number]  make your own move
        [u]       undo move                     [r]       reset game
        [b]       back to previous menu        [q/e]      quit

    -----------------------------

</details>

<details>
    <summary>Test:</summary>

        Round 38
    estimated winner: player1
    actual winner: player1 (nim perfect)
    ---correct---
    Round 39
    estimated winner: player1
    actual winner: player1 (nim perfect)
    ---correct---
    Round 40
    estimated winner: player1
    actual winner: player1 (nim)
    ---correct---


    test mode

    [ENTER]  nim vs nimPerfect: play 40 game simulations
    [b]    back to previous menu
    [q/e]   quit

    -----------------------------
    result: score nim -> 22, score nimPerfect -> 18

</details>