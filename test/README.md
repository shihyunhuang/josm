JOSM Testing Project

What is the software project that you are testing? What is its purpose? Any other aspects that are relevant: size in terms of LOC, languages that it is written in, etc.
Document its build. What did you need to do to get it built and running?
Document the existing test cases (JUnit or otherwise). This should be a study of the existing testing practices and frameworks that are used already in the system. (This section might evolve as we learn more throughout the quarter.) How do you run them?
Partitioning: First, motivate the need for systematic functional testing and partition testing. Describe these concepts. Then, per-team member: select a feature that allows for partitioning. Specify your partitioning scheme and the partitions that compose that partitioning scheme (and boundaries when appropriate) in English — describe them in the document. Describe how those partitions are different. Provide representative input values, one for each partition in your scheme. Describe how you choose representative values (and perhaps boundary values, if appropriate) that match those partitions. Then, write new test cases in JUnit (using your partitions and representative values), and describe and document those test cases and how they run in the document.




https://josm.openstreetmap.de/wiki/Introduction


# Introduction #


JOSM (Java OpenStreetMap Editor) is a mature, extensible desktop application for editing OpenStreetMap (OSM) data. Written in Java 11+ and consisting of approximately 250K–350K lines of code, it was first released in 2007. 

JOSM offers a comprehensive graphical user interface that enables users to download, visualize, edit, validate, and upload geographic data to the OSM project.
















# Project Overview #

JOSM is a Java-based desktop editor for OpenStreetMap data. At a high level, the system separates domain data representation, editing logic, and user interaction to support complex map-editing workflows while keeping core behavior testable and maintainable.
## System Architecture ##
- UI Layer(Listening Event): The user performs an action (mouse/keyboard), which triggers a UI event or command.
- Function / Tools (Editing Pipeline): Editing tools interpret the UI event as a specific editing intent (e.g., create a node, draw a way, edit tags, merge/split objects) and execute the corresponding operation.
- Data Model: The editing operation applies changes to the in-memory representation of OpenStreetMap primitives (nodes, ways, relations) and their tags/metadata. This model is the system’s single source of truth.
- Validation and Data Quality Checks: Validation components read the updated data model to detect inconsistencies or suspicious states and produce warnings and/or suggested fixes.
- Extensibility (Plugin Mechanism): It is a cross-cutting extension point. Plugins can add new UI actions and panels, introduce new editing tools/operations, or contribute additional validation rules, while still reusing the same underlying data model and execution flow.


## Repository Framework ##
- src/: Primary Java source code for the JOSM application (core logic, data model, editing operations, UI, validation, and integration components).
- test/: Test code and supporting test resources (e.g., JUnit-based unit/functional tests and any associated fixtures or datasets).
- native/: Platform-specific artifacts for packaging and integrating JOSM on Windows/macOS/Linux
- nodist/: Project assets intended for development/maintenance only and excluded from official distribution packages (e.g., source assets, intermediate files, special-purpose materials).
- scripts/: Scripts that automate recurring tasks such as building, running checks, executing tests, and supporting maintenance/release workflows.
- tools/: Tool configurations and auxiliary utilities used to enforce code quality and consistency.
- resources/: Non-code assets loaded at runtime (icons/images, configuration files, localization strings, templates, and other bundled resources).


# Installation notes #

To run JOSM, you need:
* The JOSM .jar file, e.g., josm-tested.jar or josm-latest.jar
* Java Runtime Environment (JRE) 11, or later.


How to get Java Runtime Environment :
-----------------------------------
Windows and macOS: visit one of:
- https://www.azul.com/downloads/?package=jdk#download-openjdk
- https://bell-sw.com/pages/downloads/#mn \
and download the latest Java executable for their system.\
Linux: visit “http://www.oracle.com/technetwork/java/index.html”. There is a Linux binary installer, which you must execute from a console, or use the mechanism of your distribution's packaging system.

How to launch
-------------
Windows: launch by double-clicking on the .jar file. If this does not work, open a command shell and type "java -jar josm-latest.jar"  in the directory that holds the file. (Please replace josm-latest.jar with the name of your .jar file, if you aren't using the latest version.)

Linux: open a shell, go to the file directory and type "java -jar josm-latest.jar" to launch. If this does not work, try to set your JAVA_HOME variable to the java executable location (the root location, not the bin).

macOS users just click on the .jar file icon.


# Existing Testing Infrastructure #

- data/: Resources used for some tests
- lib/: Libraries needed for (some of) the tests, including JUnit
- unit/: Unit tests that focused on core logic.
	- actions/: 	Tests for action logic triggered by menus/shortcuts
	- command/: 	Tests for edit commands and undo/redo behavior
	- data/: 	Tests core domain/model for OSM primitives
	- gui/: 		Tests for GUI-adjacent logic that is still unit-testable
	- io/: 		Tests for parsing/serialization and IO-related logic
	- plugins/: 	Tests for the plugin framework
	- spi/: 		Tests for service-provider interfaces and extension contracts
	- testutils/: 	Shared fixtures, mocks, and helpers used by other tests
	- tools/: 	Tests for general-purpose utility classes
- functional/: 	End-to-end or cross-module tests that exercise real workflows.
	- data/: 	Tests that tests end-to-end behavior in the data layer
	- gui/: 		Tests closer to user workflows in the UI
	- io/: 		Tests for IO pipelines
	- tools/:	Tests that validate higher-level tool behavior across modules
- performance/: Non-functional tests measuring timing/throughput for critical paths to 
   detect performance regressions.
	- data/:  Performance benchmarks for data-layer operations
	- gui/: Rendering (“map paint”) performance benchmarks, measuring how quickly JOSM can style and draw map data, guarding against regressions in the visualization pipeline.
	- io/: 	IO performance benchmarks for loading/parsing/saving through real code paths, tracking regressions in file/network-related processing.


Feature Selection for Partition Scheme


-  josm/test/unit/org/openstreetmap/josm/data/coor/LatLonTest.java
-   josm/test/unit/org/openstreetmap/josm/data/validation/tests/TagCheckerTest












# Motivation: Why Functional and Partition Testing?

JOSM is a large GUI-based system that already includes unit, functional, and performance
tests. Functional testing focuses on validating user-visible behavior of actions, which
cannot be fully covered by unit tests alone.

Partition testing systematically reduces the large input space of GUI actions and
dataset states into representative cases while maintaining strong fault-detection.
Selection-related actions (e.g., SelectAllAction and SelectByInternalPointAction)
naturally exhibit distinct behaviors based on dataset state, making them suitable
for partition-based functional testing.














# New Partition Test Cases


## One. Partition Testing for SelectAllAction
Created by Chung-Ping Chen



### 1. Test File Location

The partition test for `SelectAllAction` is implemented in the following file:

jsom/test/unit/org/openstreetmap/josm/actions/SelectAllActionPartitionTest.java


This test was added as a standalone test class and does not modify or replace any existing tests.

---

### 2. Tested Feature

The tested feature is the **Select All action** (`SelectAllAction`) in JOSM.

This action selects all available OSM primitives in the current editing dataset.
Its behavior depends on the **state of the dataset**, rather than explicit input parameters provided by the user.

---

### 3. Input Domain

The input domain of `SelectAllAction` is the **state of the current DataSet**, specifically whether the dataset
contains any OSM primitives.

---

### 4. Partitioning Scheme

The input space is partitioned based on the state of the dataset at the time
the action is invoked. The following partitions are defined:

- `testSelectAllEmptyDataset()` → Partition P1  
- `testSelectAllNonEmptyDataset()` → Partition P2

| Partition | Description |
|----------|-------------|
| P1 | Empty dataset (no OSM primitives) |
| P2 | Non-empty dataset (one or more OSM primitives) |

In P1 (empty dataset), invoking the action should result in no change to the
selection state, since there are no selectable objects.

In P2 (non-empty dataset), invoking the action should select all primitives
present in the dataset. The selection size should equal the number of primitives.



---
### 5. Selected Partition and Representative Input

The implemented test focuses on **Partition P1 (Empty dataset)**.

Representative input:

```
DataSet empty = new DataSet();
```

This representative value corresponds to the boundary case where no primitives are available for selection
 
---

### 6. Rationale for Partition Selection
The empty dataset represents a critical boundary condition.
 If not handled correctly, the Select All action could cause null pointer exceptions
 or unexpected behavior when no primitives exist.
By testing this partition, the test ensures that the action safely handles the absence of selectable objects and completes without throwing exceptions.


---

### 7. Test Case Description
The JUnit test verifies that executing SelectAllAction on an empty dataset:
Does not throw exceptions


Does not select any objects

---

## TWO. Partition Testing for DeleteAction
Created by Chung-Ping Chen

---

### 1. Test File Location

The partition test for `DeleteAction` is implemented in the following file:

jsom/test/unit/org/openstreetmap/josm/actions/mapmode/DeleteActionPartitionTest.java



This test was added as a new test class and does not alter any existing deletion-related tests.

---

### 2. Tested Feature

The tested feature is the static method:
``` DeleteAction.checkAndConfirmOutlyingDelete(...) ```



This method determines whether a user should be warned before deleting objects
that may have external references not yet downloaded.
It represents core functional logic independent of GUI interaction.

---

### 3. Input Domain

The input domain consists of a collection of `OsmPrimitive` objects to be deleted.\
The behavior of the method depends on the **types of primitives** present in this collection.

---

### 4. Partitioning Scheme

The input domain is partitioned based on primitive type composition:

| Partition | Description |
|----------|-------------|
| P1 | Collection contains only Node primitives |
| P2 | Collection contains only Way primitives |
| P3 | Collection contains only Relation primitives |
| P4 | Collection contains Node and Way primitives |
| P5 | Collection contains Node, Way, and Relation primitives |

These partitions are mutually exclusive and collectively exhaustive
with respect to the relevant input domain.

---

## 5. Representative Inputs

Representative inputs were selected as follows:

| Partition | Representative Input |
|----------|---------------------|
| P1 | One Node |
| P2 | One Way |
| P3 | One Relation |
| P4 | One Node and one Way |
| P5 | One Node, one Way, and one Relation |

Each representative input is the minimal combination required
to trigger the corresponding logical branch.

---

## 6. Rationale for Partition Selection

The partitioning scheme was derived directly from the conditional logic of the implementation,
which checks for the presence of nodes, ways, and relations independently.

Each partition corresponds to a distinct execution path and user-facing warning message.
By testing all partitions, the test ensures full branch coverage of the deletion confirmation logic.

---

## 7. Test Case Description

Each partition is implemented as an individual JUnit test case.
The tests verify that `checkAndConfirmOutlyingDelete` executes correctly
for all primitive type combinations without throwing unexpected exceptions.

## THREE. Partition Testing for LatLon Validity
Created by Shih-Yun Huang

---

### 1. Test File Location

The partition test for `LatLon` validity is implemented in the following file:

josm/test/unit/org/openstreetmap/josm/data/coor/LatLonTest.java

This test was added as new test methods within the existing test class and does not modify or replace any existing coordinate-related tests.

---

### 2. Tested Feature

The tested feature is the coordinate validity check provided by the `LatLon` class, specifically:

LatLon.isValid()

This method determines whether a latitude–longitude pair represents a valid geographic coordinate within the allowable bounds defined by JOSM.

---

### 3. Input Domain

The input domain consists of numeric latitude and longitude values:

- Latitude (lat)
- Longitude (lon)

The validity of a `LatLon` object depends on whether these values fall within the accepted geographic ranges:

- Latitude range: [-90, 90]
- Longitude range: [-180, 180]

In addition to normal numeric values, Java double inputs may include special floating-point values such as NaN and ±Infinity, which are also part of the effective input domain.

---

### 4. Partitioning Scheme

The input domain is partitioned based on coordinate validity and numeric edge cases.

Latitude partitions:

| Partition | Description |
|----------|-------------|
| P1 | Latitude within valid range [-90, 90] (including boundaries) |
| P2 | Latitude below lower bound (lat < -90) |
| P3 | Latitude above upper bound (lat > 90) |
| P4 | Latitude is NaN |
| P5 | Latitude is ±Infinity |

Longitude partitions:

| Partition | Description |
|----------|-------------|
| Q1 | Longitude within valid range [-180, 180] (including boundaries) |
| Q2 | Longitude below lower bound (lon < -180) |
| Q3 | Longitude above upper bound (lon > 180) |
| Q4 | Longitude is NaN |
| Q5 | Longitude is ±Infinity |

These partitions are mutually exclusive and collectively exhaustive with respect to the validity behavior of LatLon.isValid().

---

### 5. Representative Inputs

Representative inputs were selected to exercise boundary conditions, out-of-range values, and special numeric cases.

| Partition | Representative Input |
|----------|---------------------|
| P1 / Q1 | new LatLon(37.0, -122.0) |
| P1 | new LatLon(-90.0, 0.0) |
| P1 | new LatLon(90.0, 0.0) |
| Q1 | new LatLon(0.0, -180.0) |
| Q1 | new LatLon(0.0, 180.0) |
| P2 | new LatLon(-90.0001, 0.0) |
| P3 | new LatLon(90.0001, 0.0) |
| Q2 | new LatLon(0.0, -180.0001) |
| Q3 | new LatLon(0.0, 180.0001) |
| P4 | new LatLon(Double.NaN, 0.0) |
| Q4 | new LatLon(0.0, Double.NaN) |
| P5 | new LatLon(Double.POSITIVE_INFINITY, 0.0) |
| Q5 | new LatLon(0.0, Double.NEGATIVE_INFINITY) |

Each representative input is the minimal value required to trigger the corresponding partition behavior.

---

### 6. Rationale for Partition Selection

The partitioning scheme was derived directly from the numeric constraints and comparison logic used to validate geographic coordinates.

Boundary values (±90 latitude and ±180 longitude) are common sources of inclusive versus exclusive comparison errors.  
Just-outside boundary values verify that invalid coordinates are correctly rejected.  
NaN and Infinity represent special floating-point cases that can lead to incorrect validation results if not explicitly tested.

By covering these partitions, the test ensures robust handling of both typical and edge-case coordinate inputs.

---

### 7. Test Case Description

Each partition is implemented as part of new JUnit test methods added to LatLonTest.java.

The tests verify that LatLon.isValid():

- Returns true for valid coordinates, including boundary values
- Returns false for out-of-range coordinates
- Returns false for coordinates containing NaN or ±Infinity

These test cases strengthen existing coverage by applying systematic partition testing to coordinate validation logic.

## FOUR. Partition Testing for TagChecker.isTagInPresets
Created by Shih-Yun Huang

---

### 1. Test File Location

The partition test for `TagChecker.isTagInPresets` is implemented in the following file:

josm/test/unit/org/openstreetmap/josm/data/validation/tests/TagCheckerTest.java

This test was added as a new test method within the existing test class and does not modify or replace any existing validation-related tests.

---

### 2. Tested Feature

The tested feature is the static method:

TagChecker.isTagInPresets(String key, String value)

This method determines whether a given key–value tag pair exists in the JOSM preset definitions.
It represents core validation logic independent of GUI interaction.

---

### 3. Input Domain

The input domain consists of tag key–value pairs:

- Tag key (String)
- Tag value (String)

The behavior of the method depends on whether the provided key and value
are defined in the preset database used by JOSM.

---

### 4. Partitioning Scheme

The input domain is partitioned based on the existence of the key and value in presets:

| Partition | Description |
|----------|-------------|
| P1 | Key exists in presets and value exists for that key |
| P2 | Key exists in presets but value does not exist |
| P3 | Key does not exist in presets |

These partitions are mutually exclusive and collectively exhaustive
with respect to the behavior of TagChecker.isTagInPresets.

---

### 5. Representative Inputs

Representative inputs were selected to exercise each partition:

| Partition | Representative Input |
|----------|---------------------|
| P1 | key = "amenity", value = "restaurant" |
| P2 | key = "amenity", value = "this_value_should_not_exist" |
| P3 | key = "this_key_should_not_exist", value = "foo" |

Each representative input is the minimal example required
to trigger the corresponding logical outcome.

---

### 6. Rationale for Partition Selection

The partitioning scheme was derived directly from the lookup logic of the implementation,
which first checks for the existence of a tag key and then validates the associated value.

Each partition corresponds to a distinct validation outcome:
a fully valid tag, an invalid value for a known key, and an entirely unknown key.
By testing all partitions, the test ensures correct behavior across all meaningful tag lookup scenarios.

---

### 7. Test Case Description

Each partition is implemented within a single JUnit test method.

The test verifies that TagChecker.isTagInPresets:

- Returns true when both key and value exist in presets
- Returns false when the key exists but the value does not
- Returns false when the key does not exist in presets

The test also confirms that preset initialization completes successfully
and that no unexpected exceptions are thrown during tag validation.





