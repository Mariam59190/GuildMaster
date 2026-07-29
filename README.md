# GuildMaster

Application console de gestion de guilde d'aventuriers, développée en Java (projet de rattrapage).

## Fonctionnalités

- Recrutement de personnages (Guerrier, Mage, Soigneur — héritant de la classe abstraite `Character`)
- Interface `Combatant` (`attack`, `isAlive`) implémentée par tous les personnages
- Équipements (`Weapon`, `Armor`) modifiant la puissance de combat
- Constitution d'équipes (3 à 5 membres) et envoi en quête
- Résolution de quête avec part d'aléatoire, gain d'expérience et d'or, montée de niveau
- Historique complet des événements de la guilde
- Statistiques calculées avec l'API Stream : niveau moyen, répartition par classe, top 3 richesse/puissance, taux de réussite des quêtes
- Persistance CSV (sauvegarde à la fermeture, rechargement au démarrage, gestion des fichiers absents/corrompus sans plantage)
- 3 exceptions personnalisées : `InvalidCharacterException`, `InvalidTeamException`, `CharacterNotFoundException`, `CorruptedSaveFileException`
- Tests unitaires JUnit 5

## Prérequis

- Java 17+
- Maven 3.8+

## Lancer le projet

```bash
mvn compile exec:java -Dexec.mainClass="com.guildmaster.main.Main"
# ou après packaging :
mvn package
java -jar target/guildmaster.jar
```

## Lancer les tests

```bash
mvn test
```

## Structure du projet

```
src/main/java/com/guildmaster/
├── model/         Character (abstraite), Warrior, Mage, Healer
├── interfaces/     Combatant
├── equipment/       Equipment, Weapon, Armor
├── exception/      Exceptions personnalisées
├── guild/          Guild, Team
├── quest/          Quest, QuestResult
├── persistence/     GuildPersistence (CSV)
├── statistics/      GuildStatistics (Streams)
└── main/           Main (menu console)
```

## Auteur

Keita Walamoko
