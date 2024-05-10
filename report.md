# Static Analysis Tools
The following sections contain the results obtained from using three static analysis tools: SpotBugs, PMD, and Google Error Prone. It also includes an evaluation of those results and fixes for relevant errors.

# Part 1

## SpotBugs Output:
### DatabaseManager.java
- Error: EI_EXPOSE_REP2 (Medium Priority)
  - Line 35: booksCollection
  - Line 36: usersCollection

### SystemManager.java
- Error: DM_DEFAULT_ENCODING (High Priority)
  - Line 19: new Scanner(InputStream)
  - Line 49: new Scanner(InputStream)

- Error: EI_EXPOSE_REP (Medium Priority)
  - Line 171: getAvailabilityList()
  - Line 175: getBookings()

- Error: EI_EXPOSE_REP2 (Medium Priority)
  - Line 15: databaseManager
  - Line 24: databaseManager
  - Line 28: scanner
  - Line 33: databaseManager
  - Line 37: scanner
  - Line 38: systemOut
  - Line 42: scanner
  - Line 44: databaseManager
  - Line 45: curUser
  - Line 54: scanner
  - Line 59: scanner
  - Line 60: systemOut
  - Line 179: curUser

### Overall:
- 18 Errors in the following categories (EI_EXPOSE_REP2, DM_DEFAULT_ENCODING, EI_EXPOSE_REP) with 2 High priority and 16 Medium priorities.

## PMD Output:
### SystemManager.java
- Error: Unnecessary import (Priority 4)
  - Line 6: Unused import 'com.mongodb.client.FindIterable'

### Overall:
- 1 Warning in the following category (UnnecessaryImport) with priority level 4 (1 is highest, 5 is lowest).

## Google ErrorProne Output:
### SystemManager.java
- Warning: DefaultCharset
  - Line 19: Implicit use of the platform default charset
  - Line 49: Implicit use of the platform default charset

- Warning: StringSplitter
  - Line 222, 288, 297, 316: String.split(String) has surprising behavior

### Overall:
- 6 Warnings in the following categories (DefaultCharset, StringSplitter).

# Part 2

## Similarities:
- Both Spotbugs and Google Error Prone flagged issues related to reliance on default encoding, highlighting potential problems with charset handling.
- Spotbugs and Google Error Prone both identified issues related to exposing internal representations, suggesting potential vulnerabilities.
- All three tools flagged issues related to code quality, such as unused imports (PMD) and surprising behavior of methods (Google Error Prone).
- Spotbugs, PMD, and Google Error Prone all highlighted potential vulnerabilities in the codebase, indicating areas that require attention to improve security.
- All tools provided actionable insights to improve code quality. (Google Error Prone also suggested specific code changes in the form of a `Did you mean: <code change suggestion>` statement with each warning while the other two tools only pointed out the errors but not code changes to fix those errors.

## Differences:
- While all tools provided actionable insights to improve code quality, only Google Error Prone suggested specific code changes in the form of a `Did you mean: <code change suggestion>` statement with each warning while the other two tools only pointed out the errors/warnings but no code changes to fix those errors.
- Spotbugs focused on potential vulnerabilities related to exposing internal representations and default encoding.

## Discrepancies:
- SpotBugs identified a specific problem related to exposing internal representation (EI_EXPOSE_REP2) in the DatabaseManager class, which PMD and ErrorProne did not detect.
- PMD flagged an unnecessary import in the SystemManager class, which SpotBugs and SpotBugs did not report.
- Google Error Prone warned about surprising behavior of the String.split(String) method.

# Part 3

The SystemManager class appears to be the most problematic class in the system due to its large size, lack of separation of concerns, complex methods, insufficient encapsulation, and limited error handling. This class takes on multiple responsibilities, including user authentication, book management, and user interaction. The class is tightly coupled with other components, making it harder to understand, maintain, and modify.
To improve the design and maintainability of the system, it would be beneficial to refactor the SystemManager class by breaking it down into smaller, more focused classes, improving encapsulation, enhancing error handling, and considering the use of appropriate design patterns.

# Part 4

## 10 issues:

1. **SpotBugs - EI_EXPOSE_REP2 in DatabaseManager:**
   - Category: EI_EXPOSE_REP2 (Medium Priority)
   - File Name: DatabaseManager.java
   - Line Number: 35, 36
   - Description: Storing externally mutable objects (MongoCollection) into DatabaseManager.booksCollection and DatabaseManager.usersCollection may expose internal representation.
   - Characterization: Given that the collections are intentionally modified within the DatabaseManager class, the exposure of internal representation through these fields is intentional and not a problem because one of the main jobs of DatabaseManager class is to codify those collections.

2. **PMD - Unnecessary import in SystemManager:**
   - Category: Unnecessary import (Priority 4)
   - File Name: SystemManager.java
   - Line Number: 6
   - Description: Unused import of 'com.mongodb.client.FindIterable'.
   - Characterization: This is an actual problem as it clutters the codebase and increases compilation time without adding any functionality.
   - Fix: Removing the unused import statement.

3. **SpotBugs - DM_DEFAULT_ENCODING in SystemManager:**
   - Category: DM_DEFAULT_ENCODING (Medium Priority)
   - File Name: SystemManager.java
   - Line Number: 42
   - Description: Found a call to a method which will perform a byte to String (or String to byte) conversion, and will assume that the default platform encoding is suitable. This will cause the application behavior to vary between platforms.
   - Characterisation: This is an actual problem because relying on the default encoding can lead to inconsistent behavior across different platforms.
   - Fix: Use an alternative API and specify a charset name or Charset object explicitly.

4. **SpotBugs - EI_EXPOSE_REP in SystemManager:**
   - Category: EI_EXPOSE_REP (Medium Priority)
   - File Name: SystemManager.java
   - Line Number: 28
   - Description: Returning a reference to a mutable object value stored in one of the object's fields exposes the internal representation of the object.
   - Characterization: This is a potential problem depending on how the returned mutable object is used.
   - Fix: If necessary, return a new copy of the mutable object instead of the original reference.

5. **SpotBugs - EI_EXPOSE_REP in SystemManager:**
   - Category: EI_EXPOSE_REP (Medium Priority)
   - File Name: SystemManager.java
   - Line Number: 171
   - Description: getAvailabilityList() may expose internal representation by returning SystemManager.availabilityList.
   - Characterization: This is a potential problem because exposing the internal representation of an object can lead to unintended modifications and break encapsulation.
   - Fix: Instead of returning a direct reference to the mutable availabilityList, return a new copy of the object.

6. **SpotBugs - EI_EXPOSE_REP in SystemManager:**
   - Category: EI_EXPOSE_REP (Medium Priority)
   - File Name: SystemManager.java
   - Line Number: 175
   - Description: getBookings() may expose internal representation by returning SystemManager.bookings.
   - Characterisation: This is a potential issue because exposing the internal representation of an object violates encapsulation principles.
   - Fix: To address this issue, return a new copy of the mutable bookings object instead of the original reference.

7. **Google Error Prone - DefaultCharset in SystemManager:**
   - Category: DefaultCharset
   - File Name: SystemManager.java
   - Line Number: 19, 49
   - Description: Implicit use of the platform default charset, which can result in differing behavior between JVM executions or incorrect behavior if the encoding of the data source doesn't match expectations.
   - Characterization: This is an actual problem as it can lead to unpredictable behavior, especially when dealing with input/output operations.
   - Fix: Replace the implicit use of the platform default charset with an explicit charset specification.

8. **Google Error Prone - StringSplitter in SystemManager:**
   - Category: StringSplitter
   - File Name: SystemManager.java
   - Line Number: 222
   - Description: String.split(String) has surprising behavior.
   - Characterization: This warning indicates potential issues with the behavior of the String.split(String) method, which may lead to unexpected results.
   - Fix: An aspect of the behavior of the one-argument split method can be surprising.

9. **Spotbugs - EI_EXPOSE_REP in DatabaseManager Method:**
   - Category: EI_EXPOSE_REP (Medium Priority)
   - File Name: SystemManager.java
   - Line Number: 38
   - Description: Method in SystemManager class may expose internal representation by returning a reference to a mutable object stored in one of the class's fields.
   - Characterization: The line in the SystemManager constructor provided does not inherently exhibit an EI_EXPOSE_REP issue. This is a false positive.

10. **Spotbugs - EI_EXPOSE_REP in SystemManager Method:**
   - Category: EI_EXPOSE_REP (Medium Priority)
   - File Name: SystemManager.java
   - Line Number: 179
   - Description: Method in SystemManager class may expose internal representation by returning a reference to a mutable object stored in one of the class's fields.
   - Characterization: The SystemManager class is designed to allow external manipulation of the curUser field, exposing the internal representation of the User object is intentional and necessary for the functionality of the system.
