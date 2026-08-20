# UI Test Plan

This file records command-line UI test cases for the project. Each test case states its aim, console inputs, and exact expected output. The `ui-tests` JSON block at the end is the machine-readable version used by `.codex/skills/test-ui/scripts/run-ui-tests.py`.

## Test Case 1: Exit

Aim: Verify that the program starts, shows the greeting, accepts `bye`, and exits with the farewell message.

Command:

```bash
javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby
```

Inputs:

```text
bye
```

Expected output:

```text
Hello! I'm Baby.
What can I do for you, your highness.
Bye. I'll miss you.
```

## Test Case 2: Add Task Types And List

Aim: Verify that `todo`, `deadline`, and `event` create the correct task types and display their extra date/time text as strings.

Command:

```bash
javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby
```

Inputs:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:

```text
Hello! I'm Baby.
What can I do for you, your highness.
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Bye. I'll miss you.
```

## Test Case 3: Mark And Unmark Typed Tasks

Aim: Verify that typed tasks can still be marked and unmarked while keeping their type-specific display format.

Command:

```bash
javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby
```

Inputs:

```text
todo read book
deadline do homework /by no idea :-p
mark 2
unmark 2
bye
```

Expected output:

```text
Hello! I'm Baby.
What can I do for you, your highness.
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 2 tasks in the list.
Nice! I've marked this task as done:
 [D][X] do homework (by: no idea :-p)
OK, I've marked this task as not done yet:
 [D][ ] do homework (by: no idea :-p)
Bye. I'll miss you.
```

## Machine-Readable Test Cases

```json ui-tests
[
  {
    "name": "Exit",
    "aim": "Verify that the program starts, shows the greeting, accepts bye, and exits with the farewell message.",
    "command": "javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby",
    "input": [
      "bye"
    ],
    "expected_output": "Hello! I'm Baby.\nWhat can I do for you, your highness.\nBye. I'll miss you.\n"
  },
  {
    "name": "Add Task Types And List",
    "aim": "Verify that todo, deadline, and event create the correct task types and display their extra date/time text as strings.",
    "command": "javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby",
    "input": [
      "todo borrow book",
      "deadline return book /by Sunday",
      "event project meeting /from Mon 2pm /to 4pm",
      "list",
      "bye"
    ],
    "expected_output": "Hello! I'm Baby.\nWhat can I do for you, your highness.\nGot it. I've added this task:\n  [T][ ] borrow book\nNow you have 1 tasks in the list.\nGot it. I've added this task:\n  [D][ ] return book (by: Sunday)\nNow you have 2 tasks in the list.\nGot it. I've added this task:\n  [E][ ] project meeting (from: Mon 2pm to: 4pm)\nNow you have 3 tasks in the list.\nHere are the tasks in your list:\n1.[T][ ] borrow book\n2.[D][ ] return book (by: Sunday)\n3.[E][ ] project meeting (from: Mon 2pm to: 4pm)\nBye. I'll miss you.\n"
  },
  {
    "name": "Mark And Unmark Typed Tasks",
    "aim": "Verify that typed tasks can still be marked and unmarked while keeping their type-specific display format.",
    "command": "javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby",
    "input": [
      "todo read book",
      "deadline do homework /by no idea :-p",
      "mark 2",
      "unmark 2",
      "bye"
    ],
    "expected_output": "Hello! I'm Baby.\nWhat can I do for you, your highness.\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\nGot it. I've added this task:\n  [D][ ] do homework (by: no idea :-p)\nNow you have 2 tasks in the list.\nNice! I've marked this task as done:\n [D][X] do homework (by: no idea :-p)\nOK, I've marked this task as not done yet:\n [D][ ] do homework (by: no idea :-p)\nBye. I'll miss you.\n"
  }
]
```
