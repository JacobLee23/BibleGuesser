# Software Requirements Specification (SRS)

## 1. Introduction

### 1.1 Purpose

The purposes of this document is to formally define the functional and non-functional requirements for the **BibleGuesser** software (the "Product").
This document serves as the authoritative reference for what the system must do, the constraints under which it operates, and the quality attributes it must satisfy.
This document is intended to provide a clear, unambiguous, and verifiable description of system behavior to guide design, implementation, testing and validation activities throughout the software development lifecycle.
This document does not describe system design or implementation details.

### 1.2 Intended Audience

This document is intended for the following audiences:

- *Software Engineers and Contributors*: To understand required system behavior and constraints prior to implementation.
- *Test and Quality Assurance Engineers*: To derive test cases, validation criteria, and acceptance tests.
- *Project Maintainers and Technical Leads*: To assess scope, prioritize work, and ensure alginment with project goals.
- *Stakeholders and Reviewers*: To gain a high-level understanding of system capabilities and limitations.

Each audience is expected to use this document as a reference for understanding required system behavior.
This document assumes a technical audience with familiarity in software engineering concepts

### 1.3 Intended Use

This document is intended to be used as:

- A baseline reference for system design and architectural decisions
- An input to backlog refinement and task decomposition
- A source of traceability between user needs, requirements, and implementation

This document is not intended to function as a user manual or implementation guide, but rather as a requirements-level contract that informs those downstream artifacts.

### 1.4 Product Scope

The Product is an interactive software system designed to test users' biblical knowledge through structured guessing-based gameplay.
BibleGuesser presents users with randomly selected verses from Scripture and challenges them to identify the corresopnding biblical references.
The system enables users to explore the Scriptures in an engaging manner through multiple game modes.

In scope:

- Random selection and presentation of biblical verses
- User input for guessing verse references
- Answer validation and scoring
- Multiple game modes with varying rules or difficulty levels
- Feedback to support learning and engagement

Out of scope:

- Theological interpretation or doctrinal explanation
- Creation or modification of biblical content by users
- Multiplayer interaction, social features, or competitive leaderboards
- Use as a formal academic or instructional biblical study tool

The Product is intended as a recreational and educational game that encourage familiarity with Scripture, not as a system for theological instruction, scholarly research, or authoritative biblical study.

### 1.5 Definitions and Acronyms

| Term | Definition |
| :--- | :--- |
| API | Application Programming Interface |
| CLI | Command-Line Interface |
| CSV | Comma-Separated Values |
| FR | Functional Requirement |
| GUI | Graphical User Interface |
| JSON | JavaScript Object Notation |
| NFR | Non-Functional Requirement |
| SRS | Software Requirements Specification |
| YAML | YAML Ain't Markup Language |

## 2. Overall Description

### 2.1 User Needs

The primary users of the Product require an engaging and reliable way to test and improve their knowledge of Scripture through interactive gameplay.
These needs include:

- *Accurate presentation of biblical content*, ensuring that verses and references are displayed correctly and consistently with the selected textual source.
- *Clear and intuitive gameplay mechanics*, allowing users to understand how to participate, submit guesses, and interpret results without prior instruction.
- *Meaningful feedback and scoring*, enabling users to assess the correctness of their responses and track their performance over time.
- *Variety in game modes and difficulty*, supporting users with different levels of biblical familiarity and preventing repetitive gameplay.
- *Consistency and fairness*, so that identical user actions under identical conditions yield predictable and explainable outcomes.

These user needs emphasize clarity, engagement, and educationl reinforcement rather than theological instruction, scholarly analysis, or authoritative biblical interpretation.

### 2.2 Assumptions and Dependencies

The Product is developed under the following assumptions and dependencies:

*Assumptions*

- Users possess basic familiarity with the structure of the Bible (e.g., books, chapters, and verses).
- Users are able to read and interpret biblical text presented in the selected language.
- User inputs are provided in the expected format and within reasonable bounds by the game rules.
- The Product is used in a non-real-time, single-user context without concurrent gameplay interactions.

*Dependencies*

- Availability of a complete and accurate digital Bible text from a predefined and authorized source.
- A supported runtime environment capable of handling text rendering, user input, and basic data storage.
- Local or embedded data storage for maintaining game state, scoring, and user progress across sessions.
- Standard libraries or frameworks required for random selection, string comparison, and input validation.

Changes to these assumptions or dependencies may require revisions to the system requirements or implementation approach.

## 3. System Features and Requirements

### 3.1 Functional Requirements

### 3.2 Non-Functional Requirements

#### 3.2.1 Performance

##### NFR-PERF-001

*Interactive Responsiveness*. The system shall present verse prompts, validate user guesses, and update scores within an acceptable time frame for interactive gameplay on a typical user workstation.

##### NFR-PERF-002

*Bounded Resource Usage*. The system shall operate using bounded memory proportional to the size of the active verse set and current session data.

##### NFR-PERF-003

*Performance Stability*. The system shall not exhibit noticeable performance degradation during extended gameplay sessions or repeated game runs under typical usage conditions.

#### 3.2.2 Security

##### NFR-SEC-001

*Local Execution*. The system shall not transmit user data or gameplay data over a network as part of normal operation.

##### NFR-SEC-002

*Input Trust Boundary*. The system shall treat all user-provided input as untrusted and shall validate input prior to processing.

##### NFR-SEC-003

*Execution Safety*. The system shall locally process all input and configuration parameters and shall not permit arbitrary code execution.

##### NFR-SEC-004

*File System Containment*. The system shall not read from or modify files outside of user-specified or system-designated output paths.

#### 3.2.3 Reliability

##### NFR-REL-001

*Deterministic Gameplay Logic*. The system shall produce deterministic outcomes for scoring and answer validation given identical verse selections and user inputs.

##### NFR-REL-002

*Graceful Failure*. The system shall fail gracefully when provided with invalid or unsupported inputs, providing clear and informative diagnostic messages.

##### NFR-REL-003

*Content Integrity*. The system shall detect and report missing, malformed, or inconsistent biblical text data required for gameplay.

##### NFR-REL-004

*Reproducibility Support*. The system shall log relevant session parameters alongside results to support reproducibility.

#### 3.2.4 Usability

##### NFR-USE-001

*Self-Describing Interface*. The system shall provide built-in help or usage output describing available game modes, input formats, and scoring rules.

##### NFR-USE-002

*Actionable Feedback*. Error messages and gameplay feedback shall be concise, informative, and actionable.

##### NFR-USE-003

*Sensible Defaults*. The system shall provide default settings that allow users to begin gameplay without prior configuration.

##### NFR-USE-004

*Format Consistency*. The system shall use consistent and clearly documented formats for verse display, reference notation, and user input.

#### 3.2.5 Scalability

##### NFR-SCAL-001

*Session Repeatability*. The system shall support repeated gameplay sessions without requiring application restart.

##### NFR-SCAL-002

*Feature Extensibility*. The system shall support the addition of new game modes or scoring rules without requiring architectural redesign.

##### NFR-SCAL-003

*Data Export Extensibility*. The system shall allow additional export formats to be added without mocifying core gameplay logic.

#### 3.2.6 Portability

##### NFR-PORT-001

*Platform Support*. The system shall operate on major operating systems that support the chosen implementation language.

##### NFR-PORT-002

*Dependency Discipline*. The system shall rely only on widely available runtimes and standard libraries where possible.

##### NFR-PORT-003

*Cross-Platform Consistency*. The system shall exhibit consistent gameplay behavior and scoring across supported platforms given identical inputs.

##### NFR-PORT-004

*Environmental Documentation*. The build, installation, and execution processes shall be documented for all supported environments.

#### 3.2.7 Maintainability

##### NFR-MAINT-001

*Separation of Concerns*. The system shall separate core gameplay logic from user interface handling, data storage, and input/output processing.

##### NFR-MAINT-002

*Automated Verification*. The system shall include automated tests for core gameplay logic, including answer validation and scoring.

##### NFR-MAINT-003

*Design Documentation*. The system shall document key design decisions, data sources, and assumptions regarding biblical content selection.

### 3.3 External Interface Requirements

These requirements define the system boundary and ensure that interactions with external entities are consistent, predictable, and well-documented.
The Product shall define its interactions with external entities as follows:

#### 3.3.1 User Interface Requirements

- Users shall provide session parameters via any one of the following interfaces:
    - A configuration file
    - A command-line interface (CLI)
    - A graphical user interface (GUI)
- The system shall present game prompts and feedback in the following ways:
    - Textual dispaly of verses or passages
    - Highlighting of correct answers and scoring summaries
    - Tabular or summary view of session progress and historical performance
- Error messages and status updates shall be presented via standard output or GUI notifications.

#### 3.3.2 Software Interface Requirements

- The system shall export user performance and session data in the following file formats:
    - Comma-separated values (CSV)
    - JavaScript Object Notation (JSON)
- Input and output formats shall adhere to defined schemas to ensure compatibility with external analysis tools.
- The system shall not rely on external APIs or third-party services for core gameplay functionality.

#### 3.3.3 Hardware Interface Requirements

- The system shall operate entirely in software without requiring direct interaction with hardward sensors or external devices.
- Standard input/output peripherals (e.g., keyboard, mouse, display) shall suffice for user interaction.

#### 3.3.4 Communication Interface Requirements

- The system shall function offline and shall not require network connectivity for its primary computations.
- Any optional export, backup, or reporting operations shall occur locally on the host machine.

### 3.4 System Features

The Product shall provide the following core features to support interactive Scripture-based gameplay and user engagement:

#### 3.4.1 Session Configuration

- **Description**: The system shall enable the user to specify various session configurations.
- **Functional Requirements**:
    - The system shall enable the user to specify the Bible version to be used
    - The system shall enable the user to specify the scope of the Bible from which verses are to be randomly selected.
    - The system shall enable the user to specify the number of surrounding verses to be provided as context for each verse that is prompted.
    - The system shall enable the user to specify the number of passages with which to prompt the user.
    - The system shall enable the user to specify a time limit for each verse that is prompted.

#### 3.4.2 Verse Prompting

- **Description**: The system shall present passages from the specified Bible version as prompts for user guessing.
- **Functional Requirements**:
    - The system shall present passages comprising a randomly selected verse and the specified number of surrounding verses as context.
    - The system shall present the user with one passage at a time.
    - The system shall select verses uniformly at random across the specified scope.
    - The system shall ensure that all the verses prompted in a session are unique.

#### 3.4.3 User Input and Answer Validation

- **Description**: The system shall accept then validate guesses from the user for the correct reference of the prompted verse.
- **Functional Requirements**:
    - The system shall accept user input in standard notation (`<book> <chapter>:<verse>`).
    - The system shall check user input against the canonical reference for correctness.
    - The system shall provide the user immediate feedback indicating whether the guess is correct or incorrect.

#### 3.4.4 Scoring and Progress Tracking

- **Description**: The system shall score each session according to the accuracy of user guesses.
- **Functional Requirements**:
    - The system shall score a user guess based on correctness.
    - The system shall score a user guess based on proximity.
    - The system shall locally record session-level and cumulative scores.
    - The system shall enable the user to view performance summaries, including 

#### 3.4.5 Data Export and Persistence

- **Description**: The system shall allow the user to save and export session and performance data.
- **Functional Requirements**:
    - The system shall locally store session data for retrieval in future sessions.
    - The system shall enable the user to export data in CSV or JSON format for offline review or analysis.

## 4. Other Requirements

### 4.1 Database Requirements

The Product does not require a persistent database for core functionality.
All gameplay data shall be maintained in-memory during execution.
If session results or user performance data are stored, they shall be written to user-specified files in supported export formats (e.g., CSV, JSON).
No assumptions are made regarding database management systems, schemas, or long-term data persistence.

### 4.2 Legal and Regulatory Requirements

The Product is not intended for use in regulated domains such as medical, financial or safety-critical systems.
As such, no industry-specific regulatory compliance is required.
The software shall comply with applicable open-source licensing terms for any third-party libraries used and shall not knowingly infringe on intellectual property rights.
The use of biblical text shall respect copyright and licensing restrictions of the selected textual source.

### 4.3 Internationalization and Localization

The Product shall use English as its primary language for all user-facing output, including documentation, game prompts, and error reporting.
Internationalization and localization support are not required for the initial release.
The system design shall not preclude future localization or support for additional languages if needed.

### 4.4 Risk Management (FMEA Matrix)

Formal failure modes and effects analysis (FMEA) is not required for this Product due to its non-safety-critical nature.
Potential risks are limited to:
- Incorrect validation of user input
- Errors in verse selection or scoring logic
- Loss of session data due to file export or storage issues
These risks shall be mitigated through input validation, thorough testing of game logic, clear documentation of assumptions, and explicit user feedback on scoring and session status.

## 5. Appendices

### 5.1 Glossary

*Bible*: the sacred scriptures of Christians comprising the Old Testament and the New Testament.

*Book*: a major division of a treatise or literary work.

*Canon*: an authoritative list of books accepted as Holy Scripture.

*Chapter*: a main division of a book.

*Passage*: a usually brief portion of a written work or speech that is relevant to a point under discussion or noteworthy for content or style.

*Scripture*: the books of the Bible.

*Verse*: one of the short divisions into which a chapter of scripture (such as the Bible, Torah, or Quran) is traditionally divided.

*Version*: a translation of the Bible or a part of it.

### 5.2 Use Cases and Diagrams

### 5.3 To Be Determined (TBD) List

There are no outstanding items at the time of the version of this document.
All requirements and design decisions necessary for the current scope of the Product have been defined.

This section is retained for future use should additional uncertainty or deferred decisions arise during subsequent revisions.