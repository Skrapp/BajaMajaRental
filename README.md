# BajaMajaRental

Uppgift under utbildningen Systemutveckling Java till kursen Databaser och test. Uppgiften gick ut på att skapa en 
uthyrningsplattform där man via hibernate ska kunna hatera medlemmar, uthyrningsobjekt och uthyrningar. 

Detta projekt simulerar en uthyrningsplattform för ett företag som hyr ut BajaMajor och dess tillbehör.

Huvudsyftet med uppgiften är att träna på:
- **Hibernate/ORM** för persistens och databashantering
- **Testning** (enhetstester och relevanta integrationstester)
- **Kodstruktur** och separation av ansvar (lagerindelning, tydliga beroenden och återanvändbar logik)

## ✅ Funktionalitet
Användaren använder en konsollbaserad meny för att hantera funktionerna i programmet

Exempel på vad plattformen hanterar:
- kunder/medlemmar(skapa, lista och filtrera)
- uthyrningsobjekt (skapa, lista och filtrera t.ex. BajaMajor och tillbehör)
- uthyrningar (skapa, lista och återlämna)

## ⭐ Kom igång
Använder MySQL lokalt, se över så hibernate.properties är rätt inställd för dig. I resource\schema finns init.sql och 
insert.sql för att bygga upp basdata.

Entry-point: Main.java

## ✍️ Författare

Uppgift skapad för utbildningssyfte av Sara Nilsson. 

LinkedIn: https://www.linkedin.com/in/sara-nilsson-774402220/

Github: https://github.com/Skrapp
