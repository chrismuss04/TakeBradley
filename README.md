# OOP Tank Game — Extraction Shooter

 A 2D extraction shooter / tank war game built with JavaFX, showcasing Object-Oriented Design and multiple design patterns.

---

## Overview

This project is a merge of a classic tank war concept with modern extraction-shooter mechanics. Players control a tank or human/AI characters, loot weapons and gear, and try to survive raids by defeating enemies and safely extracting with their loot.

Key concepts implemented:

- **Object-Oriented Design (OOD)**: Encapsulation, inheritance, and polymorphism.
- **Design Patterns**: Factory, Strategy, Observer, Singleton, Decorator/Buff, Flyweight.
- **MVC Architecture**: Clear separation of Model, View, and Controller layers.
- **Game Mechanics**: AI behaviors, loot, extraction, inventory, and weapon management.

---

## Features

### Gameplay

- **Player-controlled Characters**
  - Move, fire weapons, pick up loot, and extract.
  - Equip different gear sets with buffs and debuffs.
  
- **AI Players**
  - Roam, loot, fight other players, and behave like NPC scavengers.
  - Spawn with random gear and weapons (P2, MP5, AK).
  
- **Scientists (NPCLootGuards)**
  - Stationary guards protecting loot containers.
  - Spawn with P2 or MP5 and Hazmat gear.
  
- **AITank (Bradley APC)**
  - High-health hostile vehicle.
  - Shoots rockets at players, drops high-value loot on death.
  
- **Loot & Inventory**
  - Weapons, ammo, gear sets, consumables, and containers (MilCrates, Barrels).
  - Inventory is temporary; stash persists across raids.
  
- **Extraction Zones**
  - Players can extract their loot to move items from their raid inventory to the persistent stash.

---

### Systems

- **Event System**: Handles entity deaths, loot spawns, and extractions using Observer pattern.
- **Buff/Decorator System**: Dynamically applies gear or temporary item buffs to players.
- **Flyweight System**: Shared immutable resources (sprites, audio clips, loot configs) to optimize memory usage.
- **Factories**: Create entities, weapons, and loot objects consistently.
- **Strategies**: AI movement and loot behaviors separated for clean SRP.

---

## Project Structure

/src 
- /app : Controllers: Game, Input, Extraction, Spawn
- /model : Game entities, loot, weapons, inventory
- /factories : Factories for tanks, loot, weapons
- /strategies : AI movement and loot behaviors
- /decorators : Buff/decorator system
- /flyweight : Shared sprites, loot configs, sounds
- /systems : Event system, game mechanics
- /view : Renderer, HUD, sprites

---

## Persistence

- Player stash is serialized to JSON.
- Raid inventory is temporary and cleared on death unless extracted.
- Auto-save triggers on loot extraction or stash modification.

---

## Dependencies

- Java 17+
- JavaFX 20+ (or matching version)
- Gson (or other JSON library for stash serialization)

---
