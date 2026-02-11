JOSM Testing Project

Group- 15
Shih-Yun Huang
Chung-Ping Chen

# Finite Models
Finite models are useful for testing because they provide a clear and structured representation of a system’s behavior. A finite model describes a system in terms of a limited set of states, the events or inputs that trigger changes, and the transitions between those states.

By explicitly defining all possible states and transitions, finite models enable systematic test design. Test cases can be constructed to ensure state coverage, where each state is visited at least once, and transition coverage, where each valid transition is exercised. This reduces the likelihood of missing important behaviors compared to ad-hoc testing.

In addition, finite models help identify edge cases and invalid transitions. By specifying which transitions are allowed and which are not, testers can design tests that verify the system responds correctly to unexpected or illegal inputs. Overall, finite models improve test completeness, clarity, and confidence in system correctness, especially for state-dependent and interactive components.
—


# Feature/Component Selection
## ONE: Undo / Redo Command History by Shih-Yun Huang
### Summary
The Undo / Redo command history feature in JOSM lends itself well to a finite state machine model because the system’s behavior depends on the current history position (how many actions have been executed vs. undone) and whether there are available commands to undo or redo. User actions such as performing an edit, undoing, redoing, or clearing the history trigger well-defined transitions between a finite set of states. The behavior is non-trivial because a new edit after undo must discard the redo branch, making it suitable for functional testing.

### Finite State Model of the Undo/Redo Workflow
#### States
- **Clean (0,0)**  
  No commands exist in history. `undo` is disabled and `redo` is disabled.
- **UndoOnly (1,0)**  
  At least one executed command exists. `undo` is enabled and `redo` is disabled.
- **RedoOnly (0,1)**  
  No executed commands are available to undo, but at least one undone command exists. `redo` is enabled and `undo` is disabled.
- **UndoandRedo (1,1)**  
  Both executed and undone commands exist. `undo` is enabled and `redo` is enabled.

---

#### Events and Transitions
- **e1: doCommand** (user)  
  Records a new edit command.  

- **e2: undo** (user)  
  Undoes the most recent executed command (moves history pointer backward).  

- **e3: redo** (user)  
  Reapplies the most recently undone command (moves history pointer forward).  

- **e4: cleanHistory** (system/user)  
  Clears/reset the command history. 


#### State Diagram:


### New Functional test
Test file: `UndoRedoFsmTest.java`  
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/UndoRedoFsmTest.java
This suite validates the Undo/Redo history behavior using the FSM model where the observable state is `(canUndo, canRedo)`

#### Test coverage (with method permalinks)
- Test case 1: add_then_undo_sets_redo
https://github.com/shihyunhuang/josm/blob/ce5e81187d30d6360836928aaa46fb296b7150a0/test/unit/org/openstreetmap/josm/UndoRedoFsmTest.java#L75
Covers the basic transition chain **S0 (EMPTY) --ADD--> S1 (UNDO_ONLY) --UNDO-->  S2 (REDO_ONLY) --REDO --> S1 (UNDO_ONLY)** by performing `add()`,  `undo()` and `redo()`. 

- Test case 2: add_from_redoOnly_clears_redo
https://github.com/shihyunhuang/josm/blob/ce5e81187d30d6360836928aaa46fb296b7150a0/test/unit/org/openstreetmap/josm/UndoRedoFsmTest.java#L96
 Covers the core history rule **“new command clears redo”**: Transition chain  **S0 (EMPTY) --ADD--> S1 (UNDO_ONLY) --UNDO-->  S2 (REDO_ONLY) --ADD--> S1 (UNDO_ONLY)**.  

- Test case 3: two_adds_then_undo_sets_undo_and_redo
https://github.com/shihyunhuang/josm/blob/ce5e81187d30d6360836928aaa46fb296b7150a0/test/unit/org/openstreetmap/josm/UndoRedoFsmTest.java#L111
Covers **S0 (EMPTY) --ADD--> S1 (UNDO_ONLY) --UNDO-->  S3 (UNDO_AND_REDO_AVAILABLE)** by performing two `add()` operations and then one `undo()`. At the same time, covers **S3 (UNDO_AND_REDO_AVAILABLE) --ADD--> S1 (UNDO_ONLY)** by add() operation.


- Test case 4: clean_to_s0
https://github.com/shihyunhuang/josm/blob/ce5e81187d30d6360836928aaa46fb296b7150a0/test/unit/org/openstreetmap/josm/UndoRedoFsmTest.java#L132
Covers the reset transition to **S0** by invoking `clean()` after history has been created.

#### Notes (minimal)
- `UndoRedoHandler` is the system under test (it owns the undo/redo stacks).  
- `NoOpCommand` is a minimal concrete `Command` used to trigger handler behavior without requiring UI or complex dataset fixtures.



## TWO: Layer Visibility & Active State FSM by Pete Chen
### Summary
The Layer Visibility and Active State management feature in JOSM lends itself well to a finite state machine model because the system’s behavior depends on two observable properties of a layer: whether it is visible and whether it is the active layer.
User actions such as activating a layer, switching the active layer, hiding a layer, or showing a layer trigger well-defined transitions between a finite set of states.
The behavior is non-trivial because visibility and active state are independent dimensions. A layer may remain active even after being hidden, which contradicts an intuitive assumption that “hidden implies inactive.” This makes the feature suitable for systematic functional testing using a finite state model.
### Finite State Model of the Layer Workflow
#### States
The system state is modeled as:
``` State = (Visible, Active) ```

Where:
-Visible ∈ {true, false}
-Active ∈ {true, false}


The four possible states are:
S0 (Hidden & Inactive) : Visible = false, Active = false
S1 (Visible & Inactive) : Visible = true, Active = false
S2 (Visible & Active) : Visible = true, Active = true
S3 (Hidden & Active) : Visible = false, Active = true
### Events and Transitions
e2: setActiveLayer(layer) (user/system) : Sets a layer as active.
e3: switchActiveLayer(otherLayer) (user/system) : Changes the active layer to another layer.
e4: hide (setVisible(false)) (user) : Hides the layer without affecting active state.
e5: show (setVisible(true)) (user) : Makes the layer visible again.

#### State Diagram:




### New Funcional Test

Test file: ```LayerStateFSMTest.java ```
Location:``` test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java ```
[https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/](https://github.com/shihyunhuang/josm/blob/ce5e81187d30d6360836928aaa46fb296b7150a0/test/unit/org/openstreetmap/josm/)gui/layer/LayerStateFSMTest.java

#### Test coverage
-Test Case 1: testSetActiveLayer

Covers transition: S1 (Visible & Inactive) --setActive--> S2 (Visible & Active)
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java#L26
A visible inactive layer becomes active when ``` setActiveLayer() ``` is invoked.

-Test Case 2: testSwitchActiveLayer

Covers transition: S2 --switchActive--> S1
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java#L37
When another layer becomes active, the original layer loses active status but remains visible.

-Test Case 3: testHideLayerKeepsActive
Covers transition: S2 --hide--> S3
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java#L53
An active layer remains active even after being hidden.
This validates that visibility and active state are independent.

-Test Case 4: testHiddenLayerCanBeActive
Covers transition: S0 --setActive--> S3
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java#L67
A hidden layer can become active.This confirms that “Hidden ⇒ Inactive” is not enforced by the system.

-Test Case 5: testHideLayerFromVisibleInactive
Covers transition: S1 --hide--> S0
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java#L80
A visible inactive layer becomes hidden and remains inactive.

-Test Case 6: testShowHiddenLayer
Covers transition: S0 --show--> S1
https://github.com/shihyunhuang/josm/blob/master/test/unit/org/openstreetmap/josm/gui/layer/LayerStateFSMTest.java#L97

A hidden inactive layer becomes visible but does not automatically become active.
