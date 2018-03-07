# Android Makers Stand

## Simon

[Guideline](https://docs.google.com/presentation/d/1tLJ2PW7Q7T12ithRcHKtkafyTgvN5T6JVElZZzVzyk0/)

### New Game
- A player, clicking on white button
- Store in Firebase:

```
player: {
  name: '<random name>',
  id: '<unique id>',
  start: <timestamp at start>
}
```

### End of Game
- A player, loosing round
- Store in Firebase:

```
score: {
 <player id>: {
  name: '<player name>',
  count: <number of color found>,
  time: <elapsed time since beginning in seconds>
 }
}
```
