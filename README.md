## Budgethanterare
Budgethanteraren är ett konsolbaserat Java-program där användaren kan registrera inkomster och utgifter, beräkna saldo,
sammanställa transaktioner per kategori samt filtrera och sortera transaktioner.

Programmet är uppdelat i flera klasser med tydliga ansvarsområden.

### Budgethanterare uppdelning: 

1. BudgetService – hanterar beräkning av saldo, sammanställning per kategori, filtrering och sortering.
2. FileFormatException - hanterar felaktigt formaterade data i CSV-filen.
3. InvalidTransactionException – används för att hantera ogiltiga transaktioner.
4. Main - hanterar menystruktur och användarinteraktion.
5. Repository -lagrar och hämtar objekt samt filtrerar dem med hjälp av villkor.
6. Transaction - representerar en transaktion med datum, typ, kategori och belopp.
7. TransactionFileHandler - hanterar läsning från och skrivning till fil.
8. TransactionType - en enum som skiljer mellan inkomster och utgifter.
9. CSVTransactionStore - omvandlar transaktioner till och från CSV-format.

### Budgethanteraren menystruktur: 
1. Lägga till transaktioner 
2. Visa alla transaktioner
3. Visa saldo och sammanställning per kategori
4. Filtrera transaktioner 
5. Spara till fil
6. e. Avsluta programmet


### JUnit-tester & felsökning: 

#### JUnit-tester:
Jag använde JUnit 5 för att testa programmets viktigaste funktioner. Klasserna som jag testade var Repository, 
BudgetService och CSVTransactionStore.

Jag valde att testa dessa klasser eftersom de innehåller viktig funktionalitet för att lagra och filtrera data, 
beräkna saldo och hantera transaktioner i CSV-format.

#### Felsökning: 
Under utvecklingen stötte jag på flera fel och buggar. Jag upptäckte vissa av dem när jag testade programmet
genom att mata in olika värden i konsolen, medan andra upptäcktes genom JUnit-tester.

##### Bugg 1: Blanksteg i CSV-data

När jag testade inläsning av en CSV-rad med mellanslag efter kommatecknen, till exempel 2026-10-23, Mat, 300.0, UTGIFT, 
undersökte jag hur programmet hanterade blankstegen.

Jag använde testet fromCsv() för att kontrollera att CSV-raden kunde omvandlas till ett Transaction-objekt. 
Jag ändrade hanteringen av de inlästa fälten så att mellanslaget togs bort med .trim(). Därefter körde jag testet 
igen och kontrollerade att det passerade.

##### Bugg 2: Beräkning av totalt saldo

Jag testade metoden calculateTotal() i BudgetService med en inkomst på 4 000 kr och en utgift på 300 kr.
Det förväntade saldot var 3 700 kr. Jag undersökte hur inkomster och utgifter summerades i metoden. Efter att 
jag hade rättat beräkningen körde jag testet igen och kontrollerade att resultatet stämde med det förväntade saldot.

### Reflektion: 
Arbetet har gett mig bättre förståelse för hur man testar och felsöker ett Java-program. Jag använde JUnit 5 för att 
kontrollera att programmets viktigaste funktioner fungerade som förväntat och för att identifiera problem under 
utvecklingen. Jag har också fått öva på att dela upp programmet i flera klasser med tydliga ansvarsområden.

Jag använde en generisk Repository<T> för att kunna hantera objekt av olika typer och återanvända samma klass.
Stream API hjälpte mig att filtrera och sortera transaktioner samt beräkna summor per kategori. Det gjorde koden mer 
lättläst och minskade behovet av manuella loopar. Jag använde loggningsnivåerna SEVERE, INFO och FINE för att följa 
programmets arbete och underlätta felsökning. De hjälpte mig att skilja mellan allvarliga fel, allmän information och 
detaljerad felsökningsinformation. I framtiden vill jag förbättra loggningen vid felaktig användarinmatning och 
skriva fler tester för gränsfall och filfel.

Jag skulle också vilja bli bättre på att göra commits och pusha koden till GitHub regelbundet. Under arbetet glömde 
jag ibland att pusha mina commits och den senaste koden. Genom att göra det oftare kan jag följa kodens historik, 
spara olika versioner och enklare återställa tidigare kod om något går fel. Det gör det även lättare att dela kod och 
få feedback från andra. Det tar jag med mig till nästa projekt.