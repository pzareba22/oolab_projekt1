## Setup screen

### Parametry do wprowadzenia: 
- zasada ewolucyjna (zwykła lub magizna)
- mapa (zwinięta, nie lub obie)
- wysokość mapy
- szerokość mapy
- wysokość dżungli
- szerokość dżungli
- początkowa liczba zwierząt
- zysk energii z trawy
- minimalna energia do kopulacji
- częstotliwość odświeżania

## Co może się znajdować na mapie?
- zwierzę
- trawa
- dżungla (podłoga)
- sawanna (podłoga)

## Cykl dnia
- usunięcie martwych zwierząt z mapy
- ruchy zwierząt
- zjadanie roślin
- rozmnażanie zwierząt
- dodanie roślin

## Rozmnażanie
- Oba zwierzęta muszą mieć energię >= pewnej wartości (parametr startowy)
- Rodzice tracą 1/4 swojej energii na rzecz dziecka
- Podział genów rodziców jest proporcjonalny do ich udziału w energii dziecka
- Najpierw jest losowana strona, z której będą geny "silniejszego" rodzica

#### Gdzie powinno być zdefiniowane rozmnażanie:
- W klasie Animal (drugie zwierzę jako parametr, dziecko jako wartość zwracana, mapa wywołuje place) 
- W klasie Map (brzydko)
- W klasie SimulationEngine (po co xD)
#### Jak wybrać zwierzęta do rozmnażania i gdzie je wywołać?
- 2 Najsilniejsze zwierzęta na danym polu(jeśli mają wystarczającą energię)(jeśli więcej to losujemy)
- Wywołać w SimulationEngine

## Obserwowanie zwierzątka
#### Jakie parametry powinno się dać obserwować
- liczba dzieci
- liczba potomków
- dzień śmierci (RIP)
- genotyp

##TODO
- [x] Generowanie dżungli
- [x] Prostokąty zamiast obrazków
- [x] Generowanie trawy
- [x] Dodać wybór częstotliwości odświeżania
- [x] Uwzględnić ujemne licznby przy walidacji
- [x] Hashmapa list na zwierzątka
- [x] Usunąć energię potrzebną do rozmnażania jako parametr
- [x] Zaimplementować canMoveTo
- [x] Usuwanie starych kółek z mapy
- [x] Poprawić metodę show
- [x] Ruch zwierzątek
- [x] Jedzenie trawy przez zwierzątka
- [x] Energia zwierzątek (kolory)
- [x] SMIERC
- [x] Bug z brakiem listy w hashmapie (removeDead)
- [x] Bug - Zwierzątko może umrzeć tuż przed zjedzeniem trawy
- [x] Rozmnażanie zwierzątek
- [x] Zwierzątka
- [x] "Magiczność" mapy
- [x] Klikanie na zwierzątka
- [x] przechowywać liczbę zwierząt i trawy
- [ ] opisy wykresu
- [x] wykresy dla 2 plansz
- [x] Wykresy
- [ ] Zapisywanie do CSV
