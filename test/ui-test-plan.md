# UI Test Plan

This file records command-line UI test cases for the project. Each test case states its aim, console inputs, and exact expected output. The `ui-tests` JSON block at the end is the machine-readable version used by `.codex/skills/test-ui/scripts/run-ui-tests.py`.

All chatbot responses are wrapped in a horizontal line to make the console output easier to read.

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
____________________________________________________________
 Hello! I'm Baby.
 What can I do for you, your highness.
____________________________________________________________
____________________________________________________________
 Bye. I'll miss you.
____________________________________________________________
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
____________________________________________________________
 Hello! I'm Baby.
 What can I do for you, your highness.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Bye. I'll miss you.
____________________________________________________________
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
____________________________________________________________
 Hello! I'm Baby.
 What can I do for you, your highness.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
  [D][X] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
  [D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
 Bye. I'll miss you.
____________________________________________________________
```

## Test Case 4: Handle Incorrect Inputs

Aim: Verify that incorrect inputs show helpful error messages, do not crash the program, and do not add invalid tasks to the list.

Command:

```bash
javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby
```

Inputs:

```text

todo
todo read book
deadline /by Sunday
deadline return book
deadline return book /by Sunday
event meeting /from Mon
event /from Mon /to Tue
event meeting /from /to Tue
event meeting /from Mon /to
event meeting /from Mon /to Tue
mark
mark two
mark 99
mark 2
blah
list
bye
```

Expected output:

```text
____________________________________________________________
 Hello! I'm Baby.
 What can I do for you, your highness.
____________________________________________________________
____________________________________________________________
 OOPS! Please enter a command.
____________________________________________________________
____________________________________________________________
 OOPS! A todo needs a description. Try: todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS! A deadline needs a description before /by.
____________________________________________________________
____________________________________________________________
 OOPS! A deadline needs a description and /by. Try: deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS! An event needs an end time after /to.
____________________________________________________________
____________________________________________________________
 OOPS! An event needs a description before /from.
____________________________________________________________
____________________________________________________________
 OOPS! An event needs a start time after /from.
____________________________________________________________
____________________________________________________________
 OOPS! An event needs an end time after /to.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [E][ ] meeting (from: Mon to: Tue)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS! Please give me a task number to mark. Try: mark 1
____________________________________________________________
____________________________________________________________
 OOPS! Task numbers must be whole numbers. Try: mark 1
____________________________________________________________
____________________________________________________________
 OOPS! Task number 99 is not in your list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 OOPS! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][X] return book (by: Sunday)
 3.[E][ ] meeting (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
 Bye. I'll miss you.
____________________________________________________________
```

## Test Case 5: Delete Task And Renumber List

Aim: Verify that `delete` removes the selected task, reports the removed task, and renumbers the remaining tasks.

Command:

```bash
javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby
```

Inputs:

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
list
delete
delete two
delete 99
list
bye
```

Expected output:

```text
____________________________________________________________
 Hello! I'm Baby.
 What can I do for you, your highness.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] join sports club
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] borrow book
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 4.[T][X] join sports club
 5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: June 6th)
 3.[T][X] join sports club
 4.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
 OOPS! Please give me a task number to delete. Try: delete 1
____________________________________________________________
____________________________________________________________
 OOPS! Task numbers must be whole numbers. Try: delete 1
____________________________________________________________
____________________________________________________________
 OOPS! Task number 99 is not in your list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: June 6th)
 3.[T][X] join sports club
 4.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
 Bye. I'll miss you.
____________________________________________________________
```

## Test Case 6: Delete Boundary Cases

Aim: Verify that `delete` works for the first and last task, and rejects empty-list and lower-bound task numbers.

Command:

```bash
javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby
```

Inputs:

```text
todo first
todo second
delete 1
list
delete 1
list
delete 1
delete 0
delete -1
bye
```

Expected output:

```text
____________________________________________________________
 Hello! I'm Baby.
 What can I do for you, your highness.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] first
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
  [T][ ] second
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
  [T][ ] first
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] second
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
  [T][ ] second
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 OOPS! Task number 1 is not in your list.
____________________________________________________________
____________________________________________________________
 OOPS! Task number 0 is not in your list.
____________________________________________________________
____________________________________________________________
 OOPS! Task number -1 is not in your list.
____________________________________________________________
____________________________________________________________
 Bye. I'll miss you.
____________________________________________________________
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
    "expected_output": "____________________________________________________________\n Hello! I'm Baby.\n What can I do for you, your highness.\n____________________________________________________________\n____________________________________________________________\n Bye. I'll miss you.\n____________________________________________________________\n"
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
    "expected_output": "____________________________________________________________\n Hello! I'm Baby.\n What can I do for you, your highness.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] borrow book\n Now you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [D][ ] return book (by: Sunday)\n Now you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [E][ ] project meeting (from: Mon 2pm to: 4pm)\n Now you have 3 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][ ] borrow book\n 2.[D][ ] return book (by: Sunday)\n 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)\n____________________________________________________________\n____________________________________________________________\n Bye. I'll miss you.\n____________________________________________________________\n"
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
    "expected_output": "____________________________________________________________\n Hello! I'm Baby.\n What can I do for you, your highness.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] read book\n Now you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [D][ ] do homework (by: no idea :-p)\n Now you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Nice! I've marked this task as done:\n  [D][X] do homework (by: no idea :-p)\n____________________________________________________________\n____________________________________________________________\n OK, I've marked this task as not done yet:\n  [D][ ] do homework (by: no idea :-p)\n____________________________________________________________\n____________________________________________________________\n Bye. I'll miss you.\n____________________________________________________________\n"
  },
  {
    "name": "Handle Incorrect Inputs",
    "aim": "Verify that incorrect inputs show helpful error messages, do not crash the program, and do not add invalid tasks to the list.",
    "command": "javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby",
    "input": [
      "",
      "todo",
      "todo read book",
      "deadline /by Sunday",
      "deadline return book",
      "deadline return book /by Sunday",
      "event meeting /from Mon",
      "event /from Mon /to Tue",
      "event meeting /from /to Tue",
      "event meeting /from Mon /to",
      "event meeting /from Mon /to Tue",
      "mark",
      "mark two",
      "mark 99",
      "mark 2",
      "blah",
      "list",
      "bye"
    ],
    "expected_output": "____________________________________________________________\n Hello! I'm Baby.\n What can I do for you, your highness.\n____________________________________________________________\n____________________________________________________________\n OOPS! Please enter a command.\n____________________________________________________________\n____________________________________________________________\n OOPS! A todo needs a description. Try: todo read book\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] read book\n Now you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n OOPS! A deadline needs a description before /by.\n____________________________________________________________\n____________________________________________________________\n OOPS! A deadline needs a description and /by. Try: deadline return book /by Sunday\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [D][ ] return book (by: Sunday)\n Now you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n OOPS! An event needs an end time after /to.\n____________________________________________________________\n____________________________________________________________\n OOPS! An event needs a description before /from.\n____________________________________________________________\n____________________________________________________________\n OOPS! An event needs a start time after /from.\n____________________________________________________________\n____________________________________________________________\n OOPS! An event needs an end time after /to.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [E][ ] meeting (from: Mon to: Tue)\n Now you have 3 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n OOPS! Please give me a task number to mark. Try: mark 1\n____________________________________________________________\n____________________________________________________________\n OOPS! Task numbers must be whole numbers. Try: mark 1\n____________________________________________________________\n____________________________________________________________\n OOPS! Task number 99 is not in your list.\n____________________________________________________________\n____________________________________________________________\n Nice! I've marked this task as done:\n  [D][X] return book (by: Sunday)\n____________________________________________________________\n____________________________________________________________\n OOPS! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][ ] read book\n 2.[D][X] return book (by: Sunday)\n 3.[E][ ] meeting (from: Mon to: Tue)\n____________________________________________________________\n____________________________________________________________\n Bye. I'll miss you.\n____________________________________________________________\n"
  },
  {
    "name": "Delete Task And Renumber List",
    "aim": "Verify that delete removes the selected task, reports the removed task, and renumbers the remaining tasks.",
    "command": "javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby",
    "input": [
      "todo read book",
      "deadline return book /by June 6th",
      "event project meeting /from Aug 6th 2pm /to 4pm",
      "todo join sports club",
      "todo borrow book",
      "mark 1",
      "mark 2",
      "mark 4",
      "list",
      "delete 3",
      "list",
      "delete",
      "delete two",
      "delete 99",
      "list",
      "bye"
    ],
    "expected_output": "____________________________________________________________\n Hello! I'm Baby.\n What can I do for you, your highness.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] read book\n Now you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [D][ ] return book (by: June 6th)\n Now you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)\n Now you have 3 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] join sports club\n Now you have 4 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] borrow book\n Now you have 5 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Nice! I've marked this task as done:\n  [T][X] read book\n____________________________________________________________\n____________________________________________________________\n Nice! I've marked this task as done:\n  [D][X] return book (by: June 6th)\n____________________________________________________________\n____________________________________________________________\n Nice! I've marked this task as done:\n  [T][X] join sports club\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][X] read book\n 2.[D][X] return book (by: June 6th)\n 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)\n 4.[T][X] join sports club\n 5.[T][ ] borrow book\n____________________________________________________________\n____________________________________________________________\n Noted. I've removed this task:\n  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)\n Now you have 4 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][X] read book\n 2.[D][X] return book (by: June 6th)\n 3.[T][X] join sports club\n 4.[T][ ] borrow book\n____________________________________________________________\n____________________________________________________________\n OOPS! Please give me a task number to delete. Try: delete 1\n____________________________________________________________\n____________________________________________________________\n OOPS! Task numbers must be whole numbers. Try: delete 1\n____________________________________________________________\n____________________________________________________________\n OOPS! Task number 99 is not in your list.\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][X] read book\n 2.[D][X] return book (by: June 6th)\n 3.[T][X] join sports club\n 4.[T][ ] borrow book\n____________________________________________________________\n____________________________________________________________\n Bye. I'll miss you.\n____________________________________________________________\n"
  },
  {
    "name": "Delete Boundary Cases",
    "aim": "Verify that delete works for the first and last task, and rejects empty-list and lower-bound task numbers.",
    "command": "javac -d out/test-ui src/main/java/*.java && java -cp out/test-ui Baby",
    "input": [
      "todo first",
      "todo second",
      "delete 1",
      "list",
      "delete 1",
      "list",
      "delete 1",
      "delete 0",
      "delete -1",
      "bye"
    ],
    "expected_output": "____________________________________________________________\n Hello! I'm Baby.\n What can I do for you, your highness.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] first\n Now you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Got it. I've added this task:\n  [T][ ] second\n Now you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Noted. I've removed this task:\n  [T][ ] first\n Now you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][ ] second\n____________________________________________________________\n____________________________________________________________\n Noted. I've removed this task:\n  [T][ ] second\n Now you have 0 tasks in the list.\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n____________________________________________________________\n____________________________________________________________\n OOPS! Task number 1 is not in your list.\n____________________________________________________________\n____________________________________________________________\n OOPS! Task number 0 is not in your list.\n____________________________________________________________\n____________________________________________________________\n OOPS! Task number -1 is not in your list.\n____________________________________________________________\n____________________________________________________________\n Bye. I'll miss you.\n____________________________________________________________\n"
  }
]
```
